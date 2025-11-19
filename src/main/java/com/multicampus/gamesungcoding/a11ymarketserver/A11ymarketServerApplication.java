package com.multicampus.gamesungcoding.a11ymarketserver;

import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.CorsProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CorsProperties.class, JwtProperties.class})
public class A11ymarketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(A11ymarketServerApplication.class, args);
    }

}
