package ru.kfu.fractal.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kfu.fractal.dto.FractalResponse;
import ru.kfu.fractal.dto.GenerateFractalRequest;
import ru.kfu.fractal.dto.ListFractalResponse;
import ru.kfu.fractal.dto.ListTransformationTypes;
import ru.kfu.fractal.service.FractalService;

@Slf4j
@RestController
@RequestMapping("/fractals")
@RequiredArgsConstructor
public class FractalController {

    private final FractalService scrapperService;

    @GetMapping("/transformations")
    public ResponseEntity<ListTransformationTypes> handleGetTransformationTypes() {
        log.info("Received request to get all transformations");
        ListTransformationTypes listTransformationTypes = scrapperService.getAllTransformations();
        return ResponseEntity.ok(listTransformationTypes);
    }

    @GetMapping
    public ResponseEntity<ListFractalResponse> handleGetLastFractals(@RequestHeader("Tg-Chat-Id") @NotNull Long tgChatId) {
        log.info("Received request to get last fractals for chatId: {}", tgChatId.toString());
        ListFractalResponse response = scrapperService.getLastFractals(tgChatId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<FractalResponse> handleGenerateFractal(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody @Valid GenerateFractalRequest request) {
        log.info("Received request to generate fractal for chatId: {}", tgChatId.toString());
        FractalResponse response = scrapperService.generateFractal(tgChatId, request);
        return ResponseEntity.ok(response);
    }
}