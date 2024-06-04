package com.example.demo.student.helpers;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.convert.converter.Converter;

public class StringBase64ToByteArrayConverter implements Converter<String, byte[]> {
    @Override
    public byte[] convert(String source) {
        System.out.println("StringBase64ToByteArrayConverter.convert");
        return Base64.decodeBase64(source);
    }
}
