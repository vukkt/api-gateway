package com.ed.api_gateway.controller;

import com.ed.api_gateway.config.DynamicRouteConfig;
import com.ed.api_gateway.dto.RouteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/dynamic-routes")
public class DynamicRouteController {
    @Autowired
    private DynamicRouteConfig dynamicRouteConfig;

    @PostMapping("/add")
    public void addDynamicRoute(@RequestBody RouteDTO routeDTO) {
        dynamicRouteConfig.buildRouteDefinition(routeDTO);
    }

    @DeleteMapping("/remove/{routeId}")
    public void removeDynamicRoute(@PathVariable String routeId) {
        dynamicRouteConfig.removeRoute(routeId);
    }

    @GetMapping("/routes")
    public Flux<RouteDefinition> getAllRoutes() {
        return dynamicRouteConfig.getRoutes();
    }
}
