package com.multicampus.gamesungcoding.a11ymarketserver.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TrimmedStringConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData.trim();
    }
}
