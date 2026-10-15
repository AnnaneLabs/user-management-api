package com.usermanagement.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.usermanagement.api.service")
@ComponentScan(basePackages = {
        "com.usermanagement.api.service",
        "com.usermanagement.api.mapper"
})
@Import({PersistenceConfig.class, SecurityConfig.class})
public class RootConfig {
}