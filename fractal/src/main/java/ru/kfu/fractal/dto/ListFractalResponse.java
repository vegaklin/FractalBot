package ru.kfu.fractal.dto;

import java.util.List;

public record ListFractalResponse(
        List<FractalResponse> fractalResponseList
) {}
