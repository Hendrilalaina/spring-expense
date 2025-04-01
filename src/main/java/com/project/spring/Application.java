package com.project.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication

public class Application {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		SpringApplication.run(Application.class, args);
	}

    /**
     * @return
     */
    @Bean
    OpenAPI swaggerHeader() {
			return new OpenAPI()
					.info((new Info())
					.description("Services for the Explore California Relational Database.")
					.title(StringUtils.substringBefore(getClass().getSimpleName(), "$"))
					.version("3.0.0"));
	}
}
