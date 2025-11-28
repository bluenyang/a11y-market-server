package com.multicampus.gamesungcoding.a11ymarketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.multicampus.gamesungcoding.a11ymarketserver.common.properties")
public class A11ymarketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(A11ymarketServerApplication.class, args);
    }

}
