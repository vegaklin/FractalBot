package ru.kfu.fractal.dto;

public record FractalResponse(
        byte[] image,
        GenerateFractalRequest generateFractalRequest
) {}
