package com.multicampus.gamesungcoding.a11ymarketserver.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (var converter : converters) {
            if (converter instanceof AbstractJackson2HttpMessageConverter jacksonConverter) {
                var supportedMediaTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
                
                supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);

                jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
            }
        }
    }
}
