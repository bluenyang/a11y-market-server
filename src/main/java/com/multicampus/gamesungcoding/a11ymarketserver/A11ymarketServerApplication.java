package com.multicampus.gamesungcoding.a11ymarketserver;

import com.multicampus.gamesungcoding.a11ymarketserver.common.config.properties.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CorsProperties.class)
public class A11ymarketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(A11ymarketServerApplication.class, args);
    }

}
