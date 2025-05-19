package ru.kfu.bot.dto;

public record FractalResponse(
        byte[] image,
        GenerateFractalRequest generateFractalRequest
) {}
