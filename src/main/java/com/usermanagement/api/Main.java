package com.usermanagement.api;

import com.usermanagement.api.config.RootConfig;
import com.usermanagement.api.service.HelloService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) throws Exception {
        var context = new AnnotationConfigApplicationContext(RootConfig.class);
        DataSource ds = context.getBean(DataSource.class);
        try (var conn = ds.getConnection()) {
            System.out.println("Connected to: " + conn.getMetaData().getURL());
        }
        context.close();
    }
}