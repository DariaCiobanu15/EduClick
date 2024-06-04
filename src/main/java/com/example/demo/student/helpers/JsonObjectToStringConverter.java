package com.example.demo.student.helpers;

import com.couchbase.client.java.json.JsonObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class JsonObjectToStringConverter implements Converter<JsonObject, String> {
    @Override
    public String convert(JsonObject source) {
        return source.toString();
    }
}

