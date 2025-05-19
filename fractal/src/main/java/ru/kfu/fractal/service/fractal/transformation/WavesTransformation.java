package ru.kfu.fractal.service.fractal.transformation;

import org.springframework.stereotype.Component;
import ru.kfu.fractal.service.fractal.model.Point;

@Component
public class WavesTransformation implements Transformation {

    @Override
    public String getName() {
        return "Waves";
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        double newX = x + (Math.sin(y * y) / 2);
        double newY = y + (Math.sin(x * x) / 2);
        return new Point(newX, newY);
    }
}
