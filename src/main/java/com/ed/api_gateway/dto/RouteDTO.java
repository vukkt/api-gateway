package com.ed.api_gateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteDTO {

private String id;
private String uri;
private List<String> paths;
}
