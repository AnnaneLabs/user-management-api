package com.usermanagement.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.usermanagement.api.service")
@Import(PersistenceConfig.class)
public class RootConfig {
}