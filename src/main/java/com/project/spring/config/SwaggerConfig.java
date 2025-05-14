package com.project.spring.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean
    OpenAPI swaggerHeader() {
			return new OpenAPI()
					.info((new Info())
					.description("Services for the Explore California Relational Database.")
					.title(StringUtils.substringBefore(getClass().getSimpleName(), "$"))
					.version("3.0.0"));
	}
}
