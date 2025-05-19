package ru.kfu.fractal.service.fractal.image.save;

import ru.kfu.fractal.service.fractal.model.FractalImage;

@FunctionalInterface
public interface ImageSaver {
    byte[] save(FractalImage image);
}