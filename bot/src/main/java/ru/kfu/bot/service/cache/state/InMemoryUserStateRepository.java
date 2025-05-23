package ru.kfu.bot.service.cache.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.kfu.bot.service.model.BotState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class InMemoryUserStateRepository implements UserStateRepository {
    private final Map<Long, BotState> userStates = new ConcurrentHashMap<>();

    @Override
    public BotState getState(long chatId) {
        BotState state = userStates.getOrDefault(chatId, BotState.DEFAULT);
        if (state != BotState.DEFAULT) {
            log.info("Fetched state for chatId: {} - State: {}", chatId, state);
        } else {
            log.info("No state found for chatId: {}. Returning default state.", chatId);
        }
        return state;
    }

    @Override
    public void setState(long chatId, BotState state) {
        userStates.put(chatId, state);
        log.info("Set state for chatId: {} - State: {}", chatId, state);
    }

    @Override
    public void clear(long chatId) {
        userStates.remove(chatId);
        log.info("Cleared data for chatId: {}", chatId);
    }
}
