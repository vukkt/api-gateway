package com.ed.api_gateway.config;

import com.ed.api_gateway.dto.RouteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
public class DynamicRouteConfig {
    private static final Logger logger = LoggerFactory.getLogger(DynamicRouteConfig.class);

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    public void buildRouteDefinition(RouteDTO routeDTO) {
        logger.info("Incoming request path: {}", routeDTO.getPaths());
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(routeDTO.getId());
        routeDefinition.setUri(URI.create(routeDTO.getUri()));

        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName("Path");
        int i = 0;
        for (String path : routeDTO.getPaths()) {
            predicateDefinition.addArg("_genkey_" + i, path);
            i++;
        }
        routeDefinition.getPredicates().add(predicateDefinition);

//        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe(
                result -> logger.info("Route added successfully"),
                error -> logger.error("Error adding route", error),
                () -> logger.info("Route addition completed")
        );

        logger.info("Added route: {}", routeDefinition);

    }

    public void removeRoute(String routeId) {
        routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
    }

    public Flux<RouteDefinition> getRoutes() {
        return routeDefinitionLocator.getRouteDefinitions();
    }
}