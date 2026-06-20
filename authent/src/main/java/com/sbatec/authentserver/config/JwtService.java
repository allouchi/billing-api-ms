package com.sbatec.authentserver.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60; // 60 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24; // 1 jour

    private final KeyLoader keyLoader;


    public JwtService(KeyLoader keyLoader) {
        this.keyLoader = keyLoader;
    }


    /**
     *
     * @return
     */
    public RSAKey rsaJwk() {
        return new RSAKey.Builder(keyLoader.loadPublicKey())
                .algorithm(JWSAlgorithm.RS256)   // 🔥 important
                .keyID("key-1")
                .build();
    }

    /**
     *
     * @param username
     * @return
     */
    public String generateToken(String username, long expiration) {

        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuer("auth-service")
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiration)) // 1h
                .claim("role", "USER")
                .header()
                .add("kid", "key-1")
                .and()
                .signWith(keyLoader.loadPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    /**
     *
     * @param username
     * @return
     */
    public String generateAccessToken(String username) {
        return generateToken(username, ACCESS_TOKEN_EXPIRATION);
    }

    /**
     *
     * @param username
     * @return
     */
    public String generateRefreshToken(String username) {
        return generateToken(username, REFRESH_TOKEN_EXPIRATION);
    }


    /**
     * Valide le Refresh Token et extrait le username.
     *
     * @param refreshToken Le token de rafraîchissement envoyé par le client
     * @return Le username si le token est valide, sinon null
     */
    public String validateAndExtractUsername(String refreshToken) {
        try {
            // Dans JJWT v0.12+, la syntaxe utilise Jwts.parser()
            Claims claims = Jwts.parser()
                    // 1. On utilise la CLÉ PUBLIQUE pour vérifier la signature RS256
                    .verifyWith(keyLoader.loadPublicKey())
                    // 2. Sécurité : On s'assure que l'émetteur est bien notre service d'authentification
                    .requireIssuer("auth-service")
                    .build()
                    // 3. Décodage et vérification (l'expiration est vérifiée automatiquement ici)
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            // Si on arrive ici, le token est valide, intègre et non expiré.
            return claims.getSubject();

        } catch (JwtException | IllegalArgumentException e) {
            // Le token est expiré, la signature est fausse (kid ou clé invalide), etc.
            System.err.println("Échec de la validation du Refresh Token : " + e.getMessage());
            return null;
        }
    }

}