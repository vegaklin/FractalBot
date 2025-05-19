package ru.kfu.fractal.service.fractal.transformation;

import org.springframework.stereotype.Component;
import ru.kfu.fractal.service.fractal.model.Point;

@Component
public class PolarTransformation implements Transformation {

    @Override
    public String getName() {
        return "Polar";
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        double newX = Math.atan(y / x) / Math.PI;
        double newY = Math.sqrt(x * x + y * y) - 1;
        return new Point(newX, newY);
    }
}
