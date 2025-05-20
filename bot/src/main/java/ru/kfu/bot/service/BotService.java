package ru.kfu.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {

    private final TelegramBot telegramBot;

    private final BotAsyncProcessor botAsyncProcessor;

    @PostConstruct
    public void init() {
        log.info("Initializing Telegram bot service");
        setTelegramListener();
        setTelegramMenuCommands();
    }

    private void setTelegramListener() {
        telegramBot.setUpdatesListener(updates -> {
            log.info("Received updates from Telegram API");
            try {
                updates.forEach(botAsyncProcessor::processUpdate);
            } catch (RuntimeException e) {
                log.error("Error while processing updates", e);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void setTelegramMenuCommands() {
        try {
            log.info("Setting bot menu commands");
            telegramBot.execute(new SetMyCommands(
                    new BotCommand("/start", "регистрация пользователя"),
                    new BotCommand("/help", "вывод списка доступных команд"),
                    new BotCommand("/generate", "начать генерацию фрактала"),
                    new BotCommand("/random", "сгененрировать слуйчайный фрактал"),
                    new BotCommand("/history", "показать список последних пяти фракталов")));
            log.info("Bot menu commands successfully set.");
        } catch (RuntimeException e) {
            log.error("Failed to set bot commands", e);
        }
    }
}
