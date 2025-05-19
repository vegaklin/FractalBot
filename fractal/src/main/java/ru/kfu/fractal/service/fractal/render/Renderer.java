package ru.kfu.fractal.service.fractal.render;

import ru.kfu.fractal.dto.GenerateFractalRequest;
import ru.kfu.fractal.service.fractal.model.FractalImage;
import ru.kfu.fractal.service.fractal.model.Rect;

@FunctionalInterface
public interface Renderer {
    FractalImage render(Rect world, FractalImage canvas, GenerateFractalRequest request);
}