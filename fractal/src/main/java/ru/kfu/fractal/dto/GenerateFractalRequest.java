package ru.kfu.fractal.dto;

import java.util.List;

public record GenerateFractalRequest(
        Long affineCount,
        Long symmetryCount,
        Long samples,
        Long iterations,
        Long imageWidth,
        Long imageHeight,
        Double rectX,
        Double rectY,
        Double rectWidth,
        Double rectHeight,
        List<String> transformationTypes
) {}


