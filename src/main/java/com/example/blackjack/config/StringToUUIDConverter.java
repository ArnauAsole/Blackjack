package com.example.blackjack.config;

import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

@ReadingConverter
public class StringToUUIDConverter implements Converter<String, UUID> {
    @Override public UUID convert(@NonNull String source) { return UUID.fromString(source); }
}
