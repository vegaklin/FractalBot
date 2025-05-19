package ru.kfu.bot.service.command;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kfu.bot.service.TelegramMessenger;
import ru.kfu.bot.service.cache.state.UserStateRepository;
import ru.kfu.bot.service.model.BotState;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateCommand implements CommandHandler {

    private final TelegramMessenger telegramMessenger;

    private final UserStateRepository inMemoryUserStateRepository;

    @Override
    public String commandName() {
        return "/generate";
    }

    @Override
    public void handle(Long chatId, String message) {
        log.info("Processing '/generate' command for chatId {}", chatId);

        inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_AFFINE_COUNT);
        log.info("Set bot state to AWAITING_AFFINE_COUNT for chatId {}", chatId);

        telegramMessenger.sendMessage(chatId, "Введите количетво афинных преоброзований для фрактала (пример: 10):");
    }
}