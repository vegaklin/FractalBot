package ru.kfu.fractal.service.fractal.transformation;

import ru.kfu.fractal.service.fractal.model.Point;

public interface Transformation {
    String getName();
    Point apply(Point p);
}
