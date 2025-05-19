package ru.kfu.fractal.service.fractal.image.correction;

import ru.kfu.fractal.service.fractal.model.FractalImage;

@FunctionalInterface
public interface ImageProcessor {
    void process(FractalImage image);
}