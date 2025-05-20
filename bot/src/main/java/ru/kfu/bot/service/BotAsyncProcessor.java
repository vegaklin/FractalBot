package ru.kfu.bot.service;


import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotAsyncProcessor {

    private final BotProcessUpdateService botProcessUpdateService;

    @Async
    public void processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        String message = update.message().text();

        log.info("Async processing update: chatId={}, message={}", chatId, message);
        botProcessUpdateService.process(chatId, message);
    }
}