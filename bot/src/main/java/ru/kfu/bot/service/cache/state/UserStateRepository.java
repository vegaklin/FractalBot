package ru.kfu.bot.service.cache.state;

import ru.kfu.bot.service.model.BotState;

public interface UserStateRepository {
    BotState getState(long chatId);

    void setState(long chatId, BotState state);

    void clear(long chatId);
}