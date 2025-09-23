package com.example.blackjack.config;

import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

@WritingConverter
public class UUIDToStringConverter implements Converter<UUID, String> {
    @Override public String convert(@NonNull UUID source) { return source.toString(); }
}
