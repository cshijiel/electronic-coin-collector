package com.roc.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author apple
 */
@SpringBootApplication
@EnableScheduling
public class HuobiRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(HuobiRestApplication.class, args);
    }
}
