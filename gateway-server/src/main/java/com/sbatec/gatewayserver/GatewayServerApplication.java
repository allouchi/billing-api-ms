package com.sbatec.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public DiscoveryClientRouteDefinitionLocator locator(ReactiveDiscoveryClient rdc, DiscoveryLocatorProperties dlp) {
        // 1. Instancier une définition pour le filtre TokenRelay
        FilterDefinition tokenRelayFilter = new FilterDefinition();
        tokenRelayFilter.setName("TokenRelay"); // Doit correspondre exactement au nom de la GatewayFilterFactory

        // 2. Initialiser la liste si elle est vide ou immuable, puis ajouter le filtre
        if (dlp.getFilters() == null) {
            dlp.setFilters(new ArrayList<>());
        }
        dlp.getFilters().add(tokenRelayFilter);

        // 3. Retourner le locator avec les propriétés modifiées
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }

}
