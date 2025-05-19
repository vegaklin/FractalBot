package ru.kfu.bot.dto;

import java.util.List;

public record ListFractalResponse(
        List<FractalResponse> fractalResponseList
) {}
