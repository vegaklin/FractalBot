package ru.kfu.bot.service.command;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kfu.bot.client.FractalClient;
import ru.kfu.bot.exception.FractalClientException;
import ru.kfu.bot.service.TelegramMessenger;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements CommandHandler {

    private final TelegramMessenger telegramMessenger;

    private final FractalClient scrapperClient;

    @Override
    public String commandName() {
        return "/start";
    }

    @Override
    public void handle(Long chatId, String message) {
        log.info("Processing '/start' command for chatId {}", chatId);
        try {
            scrapperClient.registerChat(chatId).block();

            log.info("Successfully registered chat {}", chatId);
            telegramMessenger.sendMessage(
                    chatId,
                    "Добро пожаловать! Это бот для генерации фракталов.\nДля получения списка доступных команд, введите /help");
        } catch (FractalClientException e) {
            log.error("Error while registering chat {}", chatId, e);
            telegramMessenger.sendMessage(chatId, "Ошибка при регистрации чата: " + e.getMessage());
        }
    }
}