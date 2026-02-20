package com.multicampus.gamesungcoding.a11ymarketserver.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.filterIsInstance<AbstractJackson2HttpMessageConverter>().forEach { converter ->
            converter.supportedMediaTypes = converter.supportedMediaTypes + MediaType.APPLICATION_OCTET_STREAM
        }
    }
}
