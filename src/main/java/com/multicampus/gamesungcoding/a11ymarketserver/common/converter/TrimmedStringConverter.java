package com.multicampus.gamesungcoding.a11ymarketserver.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TrimmedStringConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        // attribute가 null인 경우, NullPointerException 방지
        if (attribute == null) {
            return null;
        } else {
            return attribute.trim();
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        // dbData null인 경우, NullPointerException 방지
        if (dbData == null) {
            return null;
        } else {
            return dbData.trim();
        }
    }
}
