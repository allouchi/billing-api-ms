package com.sbatec.facture.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("=====FeignClientInterceptor=====");

        // On récupère l'authentification depuis le contexte de sécurité de Spring
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getCredentials() != null) {
            String token = authentication.getCredentials().toString();

            // Assurez-vous que le token commence bien par "Bearer "
            if (!token.startsWith("Bearer ")) {
                token = "Bearer " + token;
            }

            requestTemplate.header("Authorization", token);
            log.info("JWT Token propagé avec succès via SecurityContext !");
        } else {
            log.warn("Impossible de propager le JWT : Aucun contexte de sécurité trouvé.");
        }
    }
}