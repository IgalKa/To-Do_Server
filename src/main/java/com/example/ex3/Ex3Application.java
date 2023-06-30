package com.example.ex3;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ex3Application {

    public static void main(String[] args) {
        SpringApplication.run(Ex3Application.class, args);
        ThreadContext.clearAll();
    }

}
