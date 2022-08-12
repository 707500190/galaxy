package com.sun.content.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sun.*"})
public class ContentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApiApplication.class, args);
    }

}
