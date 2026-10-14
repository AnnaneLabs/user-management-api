package com.usermanagement.api;

import com.usermanagement.api.config.RootConfig;
import com.usermanagement.api.service.HelloService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(RootConfig.class);
        System.out.println(context.getBean(HelloService.class).hello());
    }
}