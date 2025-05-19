package ru.kfu.fractal.service.fractal.transformation;

import org.springframework.stereotype.Component;
import ru.kfu.fractal.service.fractal.model.Point;

@Component
public class SwirlTransformation implements Transformation {

    @Override
    public String getName() {
        return "Swirl";
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        double r = Math.sqrt(x * x + y * y);
        double theta = Math.atan2(y, x) + r * r;
        double newX = r * Math.cos(theta);
        double newY = r * Math.sin(theta);
        return new Point(newX, newY);
    }
}
