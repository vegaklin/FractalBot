package ru.kfu.fractal.service.fractal.transformation;

import ru.kfu.fractal.service.fractal.model.AffineCoefficient;
import ru.kfu.fractal.service.fractal.model.Point;

public record AffineTransformation(AffineCoefficient affineCoefficient) implements Transformation {

    @Override
    public String getName() {
        return "Affine";
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        double newX = x * affineCoefficient.a() + y * affineCoefficient.b() + affineCoefficient.c();
        double newY = x * affineCoefficient.d() + y * affineCoefficient.e() + affineCoefficient.f();
        return new Point(newX, newY);
    }
}
