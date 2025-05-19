package ru.kfu.fractal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kfu.fractal.dto.ApiErrorResponse;
import ru.kfu.fractal.exception.ChatNotFoundException;
import ru.kfu.fractal.exception.FractalNotFoundException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(MethodArgumentNotValidException ex) {
        log.error("Error processing request parameters", ex);

        ApiErrorResponse response = new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({FractalNotFoundException.class, ChatNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex) {
        log.warn("Not found exception", ex);

        ApiErrorResponse response = new ApiErrorResponse(
                "Элемент не найден",
                "404",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}