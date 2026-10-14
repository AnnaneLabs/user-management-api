package com.usermanagement.api.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public String hello() {
        return "Spring created me, I was not created with new!";
    }
}