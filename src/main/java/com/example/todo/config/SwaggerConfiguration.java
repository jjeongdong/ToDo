package com.example.todo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
info = @Info(title = "TODO API 명세서",
description = "TODO API 명세서",
version = "v1"))
@Configuration
public class SwaggerConfiguration {

}