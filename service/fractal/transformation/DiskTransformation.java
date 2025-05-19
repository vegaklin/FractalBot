package ru.kfu.fractal.service.fractal.transformation;

import org.springframework.stereotype.Component;
import ru.kfu.fractal.service.fractal.model.Point;

@Component
public class DiskTransformation implements Transformation {

    @Override
    public String getName() {
        return "Disk";
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        double firstPart = (1 / Math.PI) * Math.atan(y / x);
        double secondPart = Math.PI * Math.sqrt(x * x + y * y);
        double newX = firstPart * Math.sin(secondPart);
        double newY = firstPart * Math.cos(secondPart);
        return new Point(newX, newY);
    }
}
