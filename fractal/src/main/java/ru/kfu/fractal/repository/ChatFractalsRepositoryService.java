package ru.kfu.fractal.repository;

import ru.kfu.fractal.dto.FractalResponse;
import ru.kfu.fractal.dto.ListFractalResponse;

public interface ChatFractalsRepositoryService {
    Long addFractal(Long chatId, Long fractalId, Long configurationId);
    FractalResponse getFractalById(Long chatFractalId);
    ListFractalResponse getLastTreeFractalsByChatId(Long chatId);
}
