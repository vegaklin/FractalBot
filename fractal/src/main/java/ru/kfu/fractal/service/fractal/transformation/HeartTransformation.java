package ru.kfu.fractal.service.fractal.transformation;

import org.springframework.stereotype.Component;
import ru.kfu.fractal.service.fractal.model.Point;

@Component
public class HeartTransformation implements Transformation {

    @Override
    public String getName() {
        return "Heart";
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        double firstPart = Math.sqrt(x * x + y * y);
        double secondPart = firstPart * Math.atan(y / x);
        double newX = firstPart * Math.sin(secondPart);
        double newY = -firstPart * Math.cos(secondPart);
        return new Point(newX, newY);
    }
}
