package com.multicampus.gamesungcoding.a11ymarketserver.common.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class TrimmedStringConverter : AttributeConverter<String?, String?> {
    override fun convertToDatabaseColumn(attribute: String?) = attribute?.trim()

    override fun convertToEntityAttribute(dbData: String?) = dbData?.trim()
}

