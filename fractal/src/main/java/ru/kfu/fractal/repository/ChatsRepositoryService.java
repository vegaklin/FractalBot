package ru.kfu.fractal.repository;

public interface ChatsRepositoryService {
    void registerChat(Long telegramChatId);
    Long findIdByUserId(Long telegramChatId);
}
