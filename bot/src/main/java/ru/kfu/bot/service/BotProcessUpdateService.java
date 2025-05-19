package ru.kfu.bot.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kfu.bot.service.command.CommandHandler;
import ru.kfu.bot.service.model.BotState;
import ru.kfu.bot.service.state.StateMachine;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotProcessUpdateService {

    private final TelegramMessenger telegramMessenger;

    private final List<CommandHandler> commandHandlers;
    private final StateMachine stateMachine;

    void process(Long chatId, String message) {
        log.info("Processing message '{}' from chatId {}", message, chatId);

        if (stateMachine.getBotState(chatId) != BotState.DEFAULT) {
            log.info(
                    "ChatId {} is in state {}, delegating to TrackStateMachine",
                    chatId,
                    stateMachine.getBotState(chatId));
            stateMachine.trackProcess(chatId, message);
            return;
        }
        for (CommandHandler commandHandler : commandHandlers) {
            if (message.startsWith(commandHandler.commandName())) {
                log.info(
                        "Command '{}' detected for chatId {}, processing with {}",
                        message,
                        chatId,
                        commandHandler.getClass().getSimpleName());
                commandHandler.handle(chatId, message);
                return;
            }
        }

        log.warn("Unknown command '{}' received from chatId {}", message, chatId);
        telegramMessenger.sendMessage(chatId, "Неизвестная команда. Введите /help для просмотра доступных команд");
    }
}