package com.multicampus.gamesungcoding.a11ymarketserver.common.config;

import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {
    
    private final S3StorageProperties s3Properties;

}
