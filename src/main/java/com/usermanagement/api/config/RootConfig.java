package com.usermanagement.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.usermanagement.api.service")
public class RootConfig {
}