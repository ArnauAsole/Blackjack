package com.example.blackjack.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;

// importa tus converters:
import com.example.blackjack.config.UUIDToStringConverter;
import com.example.blackjack.config.StringToUUIDConverter;

@Configuration
public class R2dbcConfig {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return R2dbcCustomConversions.of(
                MySqlDialect.INSTANCE,
                List.of(new UUIDToStringConverter(), new StringToUUIDConverter())
        );
    }
}
