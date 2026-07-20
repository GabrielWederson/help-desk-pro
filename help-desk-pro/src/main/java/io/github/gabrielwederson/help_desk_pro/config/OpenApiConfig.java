package io.github.gabrielwederson.help_desk_pro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Help Desk Pro")
                        .version("v1")
                        .description("Help Desk REST API for ticket management")
                        .termsOfService("https://github.com/GabrielWederson")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/GabrielWederson")));
    }

}
