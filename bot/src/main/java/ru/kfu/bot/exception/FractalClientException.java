package ru.kfu.bot.exception;

import lombok.Getter;
import ru.kfu.bot.dto.ApiErrorResponse;

@Getter
public class FractalClientException extends RuntimeException {

    private final ApiErrorResponse apiErrorResponse;

    public FractalClientException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.description());
        this.apiErrorResponse = apiErrorResponse;
    }
}