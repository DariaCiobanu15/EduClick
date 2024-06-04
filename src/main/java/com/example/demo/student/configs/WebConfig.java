package com.example.demo.student.configs;
import com.example.demo.student.helpers.StringToJsonObjectConverter;
import com.example.demo.student.helpers.JsonObjectToStringConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JsonObjectToStringConverter jsonObjectToStringConverter;
    private final StringToJsonObjectConverter stringToJsonObjectConverter;

    public WebConfig(JsonObjectToStringConverter jsonObjectToStringConverter, StringToJsonObjectConverter stringToJsonObjectConverter) {
        this.jsonObjectToStringConverter = jsonObjectToStringConverter;
        this.stringToJsonObjectConverter = stringToJsonObjectConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(jsonObjectToStringConverter);
        registry.addConverter(stringToJsonObjectConverter);
    }
}
