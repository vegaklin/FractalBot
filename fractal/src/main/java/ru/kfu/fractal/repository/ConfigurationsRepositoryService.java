package ru.kfu.fractal.repository;

import ru.kfu.fractal.dto.GenerateFractalRequest;

public interface ConfigurationsRepositoryService {
    Long addFractalConfiguration(GenerateFractalRequest configuration);
}
