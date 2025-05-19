package ru.kfu.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "fractal", ignoreUnknownFields = false)
public record FractalConfig(@NotEmpty String url) {}