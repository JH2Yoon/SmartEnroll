package com.example.smartenroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartEnrollApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartEnrollApplication.class, args);
    }

}
