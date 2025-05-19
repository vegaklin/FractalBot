package ru.kfu.fractal.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kfu.fractal.dto.FractalResponse;
import ru.kfu.fractal.dto.GenerateFractalRequest;
import ru.kfu.fractal.dto.ListFractalResponse;
import ru.kfu.fractal.dto.ListTransformationTypes;
import ru.kfu.fractal.exception.ChatNotFoundException;
import ru.kfu.fractal.exception.FractalNotFoundException;
import ru.kfu.fractal.repository.ChatFractalsRepositoryService;
import ru.kfu.fractal.repository.ChatsRepositoryService;
import ru.kfu.fractal.repository.ConfigurationsRepositoryService;
import ru.kfu.fractal.repository.FractalsRepositoryService;
import ru.kfu.fractal.service.fractal.FractalGenerator;
import ru.kfu.fractal.service.fractal.transformation.Transformation;
import ru.kfu.fractal.service.fractal.util.RendererUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FractalService {

    private final ChatsRepositoryService chatsRepositoryService;
    private final FractalsRepositoryService fractalsRepositoryService;
    private final ConfigurationsRepositoryService configurationsRepositoryService;
    private final ChatFractalsRepositoryService chatFractalsRepositoryService;

    private final FractalGenerator fractalGenerator;

    private final List<Transformation> transformations;

    @Transactional
    public void registerChat(Long userId) {
        log.info("Registering chat with chatId: {}", userId.toString());

        chatsRepositoryService.registerChat(userId);
    }

    public ListTransformationTypes getAllTransformations() {
        return new ListTransformationTypes(RendererUtils.getAllTransformationNames(transformations));
    }

    public ListFractalResponse getLastFractals(Long userId) {
        Long chatId = chatsRepositoryService.findIdByUserId(userId);
        if (chatId == null) {
            log.error("Id not found for userId while getLastFractals: {}", userId);
            throw new ChatNotFoundException("Id не найдена для userId: " + userId);
        }

        return chatFractalsRepositoryService.getLastTreeFractalsByChatId(chatId);
    }

    public FractalResponse generateFractal(Long userId, GenerateFractalRequest request) {
        Long chatId = chatsRepositoryService.findIdByUserId(userId);
        if (chatId == null) {
            log.error("Id not found for userId while generateFractal: {}", userId);
            throw new ChatNotFoundException("Id не найдена для userId: " + userId);
        }

        byte[] image = fractalGenerator.generateNewFractal(request);

        Long fractalId = fractalsRepositoryService.addFractalImage(image);
        Long configurationId = configurationsRepositoryService.addFractalConfiguration(request);

        Long chatFractalId = chatFractalsRepositoryService.addFractal(chatId, fractalId, configurationId);

        FractalResponse fractalResponse = chatFractalsRepositoryService.getFractalById(chatFractalId);
        if (fractalResponse == null) {
            log.error("Fractal not found for chatFractalId while generateFractal: {}", chatFractalId);
            throw new FractalNotFoundException("Fractal не найден для id: " + chatFractalId);
        }

        return fractalResponse;
    }
}
