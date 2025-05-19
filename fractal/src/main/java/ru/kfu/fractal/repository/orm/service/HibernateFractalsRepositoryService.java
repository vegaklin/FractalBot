package ru.kfu.fractal.repository.orm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kfu.fractal.repository.FractalsRepositoryService;
import ru.kfu.fractal.repository.orm.entity.Fractal;
import ru.kfu.fractal.repository.orm.repository.HibernateFractalsRepository;

@Service
@RequiredArgsConstructor
public class HibernateFractalsRepositoryService implements FractalsRepositoryService {

    private final HibernateFractalsRepository fractalsRepository;

    @Override
    @Transactional
    public Long addFractalImage(byte[] image) {
        Fractal fractal = new Fractal();
        fractal.setImage(image);
        Fractal savedFractal = fractalsRepository.save(fractal);
        return savedFractal.getId();
    }
}