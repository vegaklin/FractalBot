package ru.kfu.fractal.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kfu.fractal.service.FractalService;

@Slf4j
@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class TelegramChatController {

    private final FractalService fractalService;

    @PostMapping("/{id}")
    public ResponseEntity<String> handleRegisterChat(@PathVariable @NotNull Long id) {
        log.info("Received request to register chat with id: {}", id.toString());
        fractalService.registerChat(id);
        return ResponseEntity.ok("Чат зарегистрирован");
    }
}