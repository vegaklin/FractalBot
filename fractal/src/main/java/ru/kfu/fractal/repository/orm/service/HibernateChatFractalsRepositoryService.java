package ru.kfu.fractal.repository.orm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kfu.fractal.dto.FractalResponse;
import ru.kfu.fractal.dto.GenerateFractalRequest;
import ru.kfu.fractal.dto.ListFractalResponse;
import ru.kfu.fractal.repository.ChatFractalsRepositoryService;
import ru.kfu.fractal.repository.orm.entity.Chat;
import ru.kfu.fractal.repository.orm.entity.ChatFractal;
import ru.kfu.fractal.repository.orm.entity.Configuration;
import ru.kfu.fractal.repository.orm.entity.Fractal;
import ru.kfu.fractal.repository.orm.repository.HibernateChatFractalsRepository;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HibernateChatFractalsRepositoryService implements ChatFractalsRepositoryService {

    private final HibernateChatFractalsRepository chatFractalsRepository;

    @Override
    @Transactional
    public Long addFractal(Long chatId, Long fractalId, Long configurationId) {
        ChatFractal chatFractal = new ChatFractal();
        Chat chat = new Chat();
        chat.setId(chatId);
        Fractal fractal = new Fractal();
        fractal.setId(fractalId);
        Configuration configuration = new Configuration();
        configuration.setId(configurationId);

        chatFractal.setChat(chat);
        chatFractal.setFractal(fractal);
        chatFractal.setConfiguration(configuration);
        chatFractal.setCreateTime(OffsetDateTime.now());

        ChatFractal savedChatFractal = chatFractalsRepository.save(chatFractal);
        return savedChatFractal.getId();
    }

    @Override
    @Transactional
    public FractalResponse getFractalById(Long chatFractalId) {
        ChatFractal chatFractal = chatFractalsRepository.findById(chatFractalId)
                .orElse(null);

        if (chatFractal == null) {
            return null;
        }

        Configuration config = chatFractal.getConfiguration();
        GenerateFractalRequest request = new GenerateFractalRequest(
                config.getAffineCount(),
                config.getSymmetryCount(),
                config.getSamples(),
                config.getIterations(),
                config.getImageWidth(),
                config.getImageHeight(),
                config.getRectX(),
                config.getRectY(),
                config.getRectWidth(),
                config.getRectHeight(),
                config.getTransformationTypes()
        );

        return new FractalResponse(
                chatFractal.getFractal().getImage(),
                request
        );
    }

    @Override
    @Transactional
    public ListFractalResponse getLastTreeFractalsByChatId(Long chatId) {
        List<ChatFractal> chatFractals = chatFractalsRepository.findTop3ByChatIdOrderByCreateTimeDesc(chatId);

        List<FractalResponse> fractalResponses = chatFractals.stream()
                .map(chatFractal -> {
                    Configuration config = chatFractal.getConfiguration();
                    GenerateFractalRequest request = new GenerateFractalRequest(
                            config.getAffineCount(),
                            config.getSymmetryCount(),
                            config.getSamples(),
                            config.getIterations(),
                            config.getImageWidth(),
                            config.getImageHeight(),
                            config.getRectX(),
                            config.getRectY(),
                            config.getRectWidth(),
                            config.getRectHeight(),
                            config.getTransformationTypes()
                    );
                    return new FractalResponse(
                            chatFractal.getFractal().getImage(),
                            request
                    );
                })
                .collect(Collectors.toList());
        Collections.reverse(fractalResponses);

        return new ListFractalResponse(fractalResponses);
    }
}