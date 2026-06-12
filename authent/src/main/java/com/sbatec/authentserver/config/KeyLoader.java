package com.sbatec.authentserver.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Getter
@Component
public class KeyLoader {


    @Value("classpath:keys/public.pem")
    private Resource publicKeyResource;

    @Value("classpath:keys/private.pem")
    private Resource privateKeyResource;

    @Bean
    public RSAPrivateKey loadPrivateKey() {

        try (InputStream is = privateKeyResource.getInputStream()) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            key = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

            KeyFactory kf = KeyFactory.getInstance("RSA");

            return (RSAPrivateKey) kf.generatePrivate(spec);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RSAPublicKey loadPublicKey() {

        try (InputStream is = publicKeyResource.getInputStream()) {

            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            key = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

            return (RSAPublicKey) KeyFactory
                    .getInstance("RSA")
                    .generatePublic(spec);

        } catch (Exception e) {
            throw new RuntimeException("Erreur chargement clé publique", e);
        }
    }

}