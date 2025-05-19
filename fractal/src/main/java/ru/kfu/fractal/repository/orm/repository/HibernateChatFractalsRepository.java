package ru.kfu.fractal.repository.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kfu.fractal.repository.orm.entity.ChatFractal;

import java.util.List;
import java.util.Optional;

@Repository
public interface HibernateChatFractalsRepository extends JpaRepository<ChatFractal, Long> {
    Optional<ChatFractal> findById(Long id);
    List<ChatFractal> findTop3ByChatIdOrderByCreateTimeDesc(Long chatId);
}