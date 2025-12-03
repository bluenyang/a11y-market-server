package com.multicampus.gamesungcoding.a11ymarketserver.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Properties {
    private String redirectUri;
    private String signupUri;
}
