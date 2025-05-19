package ru.kfu.bot.service.command;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kfu.bot.service.TelegramMessenger;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelpCommand implements CommandHandler {

    private final TelegramMessenger telegramMessenger;

    @Override
    public String commandName() {
        return "/help";
    }

    @Override
    public void handle(Long chatId, String message) {
        log.info("Processing '/help' command for chatId {}", chatId);

        telegramMessenger.sendMessage(
                chatId,
                """
            /start - регистрация пользователя
            /help - вывод списка доступных команд
            /generate - начать генерацию фрактала
            /random - сгененрировать слуйчайный фрактал
            /history - показать список последних трех фракталов и их конфигураций""");

        log.info("Help message sent to chatId {}", chatId);
    }
}