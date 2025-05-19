package ru.kfu.fractal.repository.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kfu.fractal.repository.orm.entity.Chat;

@Repository
public interface HibernateChatsRepository extends JpaRepository<Chat, Long> {
    Chat findByTelegramChatId(Long telegramChatId);
}