package ru.kfu.fractal.repository.orm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kfu.fractal.dto.GenerateFractalRequest;
import ru.kfu.fractal.repository.ConfigurationsRepositoryService;
import ru.kfu.fractal.repository.orm.entity.Configuration;
import ru.kfu.fractal.repository.orm.repository.HibernateConfigurationsRepository;

@Service
@RequiredArgsConstructor
public class HibernateConfigurationsRepositoryService implements ConfigurationsRepositoryService {

    private final HibernateConfigurationsRepository configurationsRepository;

    @Override
    @Transactional
    public Long addFractalConfiguration(GenerateFractalRequest configuration) {
        Configuration configEntity = new Configuration();
        configEntity.setAffineCount(configuration.affineCount());
        configEntity.setSymmetryCount(configuration.symmetryCount());
        configEntity.setSamples(configuration.samples());
        configEntity.setIterations(configuration.iterations());
        configEntity.setImageWidth(configuration.imageWidth());
        configEntity.setImageHeight(configuration.imageHeight());
        configEntity.setRectX(configuration.rectX());
        configEntity.setRectY(configuration.rectY());
        configEntity.setRectWidth(configuration.rectWidth());
        configEntity.setRectHeight(configuration.rectHeight());
        configEntity.setTransformationTypes(configuration.transformationTypes());

        Configuration savedConfig = configurationsRepository.save(configEntity);
        return savedConfig.getId();
    }
}