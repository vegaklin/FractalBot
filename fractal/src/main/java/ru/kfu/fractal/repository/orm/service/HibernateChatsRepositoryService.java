package ru.kfu.fractal.repository.orm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kfu.fractal.repository.ChatsRepositoryService;
import ru.kfu.fractal.repository.orm.entity.Chat;
import ru.kfu.fractal.repository.orm.repository.HibernateChatsRepository;

@Service
@RequiredArgsConstructor
public class HibernateChatsRepositoryService implements ChatsRepositoryService {

    private final HibernateChatsRepository chatsRepository;

    @Override
    @Transactional
    public void registerChat(Long telegramChatId) {
        Chat chat = new Chat();
        chat.setTelegramChatId(telegramChatId);
        chatsRepository.save(chat);
    }

    @Override
    @Transactional
    public Long findIdByUserId(Long telegramChatId) {
        Chat chat = chatsRepository.findByTelegramChatId(telegramChatId);
        return chat != null ? chat.getId() : null;
    }
}
