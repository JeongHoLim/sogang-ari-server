package com.ari.sogang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SogangApplication {

    public static void main(String[] args) {
        SpringApplication.run(SogangApplication.class, args);
    }

}
