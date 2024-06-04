package com.example.demo.student.configs;

import com.example.demo.student.helpers.StringBase64ToByteArrayConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.context.support.ConversionServiceFactoryBean;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ConversionConfig {

    @Bean
    public GenericConversionService conversionService() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new StringBase64ToByteArrayConverter());
        return conversionService;
    }

    @Bean
    public ConversionServiceFactoryBean conversionServiceFactoryBean() {
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        Set<Object> converters = new HashSet<>();
        converters.add(new StringBase64ToByteArrayConverter());
        factoryBean.setConverters(converters);
        return factoryBean;
    }
}

