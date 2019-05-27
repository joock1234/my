package com.blue.admin.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationStart {
    public static void main(String[] args) {
        //SpringApplication.run(ApplicationStart.class, args);
        SpringApplication app = new SpringApplication(ApplicationStart.class);
        app.setWebEnvironment(true);
        Set<Object> set = new HashSet<Object>();
        set.add("classpath:applicationContext.xml");
        app.setSources(set);
        app.run(args);
    }
}
