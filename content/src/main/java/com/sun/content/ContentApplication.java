package com.sun.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = {"com.sun.content.*"})
public class ContentApplication {
    @Bean
    public ThreadPoolExecutor queryThreadPoolExecutor() {
        return new ThreadPoolExecutor(50, 100,
                600, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
    }
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }

}
