package com.example.demo.student.helpers;

import com.couchbase.client.java.json.JsonObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToJsonObjectConverter implements Converter<String, JsonObject> {
    @Override
    public JsonObject convert(String source) {
        return JsonObject.fromJson(source);
    }
}
