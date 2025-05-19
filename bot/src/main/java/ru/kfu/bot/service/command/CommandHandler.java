package ru.kfu.bot.service.command;

public interface CommandHandler {
    String commandName();

    void handle(Long chatId, String message);
}
