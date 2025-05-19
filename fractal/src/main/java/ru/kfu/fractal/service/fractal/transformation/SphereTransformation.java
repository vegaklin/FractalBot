package ru.kfu.fractal.service.fractal.transformation;

import org.springframework.stereotype.Component;
import ru.kfu.fractal.service.fractal.model.Point;

@Component
public class SphereTransformation implements Transformation {

    @Override
    public String getName() {
        return "Sphere";
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        double newX = x / (x * x + y * y);
        double newY = y / (x * x + y * y);
        return new Point(newX, newY);
    }
}
