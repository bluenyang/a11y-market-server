package com.multicampus.gamesungcoding.a11ymarketserver.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "toss.payment")
@Getter
@Setter
public class TossPaymentProperties {
    private String secretKey;
}
