package com.example.blackjack.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocGroupsConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")        // nombre del grupo (puedes llamarlo como quieras)
                .pathsToMatch("/**")    // expone todos los endpoints de tu app
                .build();
    }
}
