package com.multicampus.gamesungcoding.a11ymarketserver.common.properties;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "app.s3")
public class S3StorageProperties {
    @NotBlank(message = "S3 bucket name must not be blank")
    private String bucket;

    private String cdnUrl;
}
