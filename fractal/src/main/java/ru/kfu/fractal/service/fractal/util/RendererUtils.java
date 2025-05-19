package ru.kfu.fractal.service.fractal.util;


import lombok.experimental.UtilityClass;
import ru.kfu.fractal.dto.GenerateFractalRequest;
import ru.kfu.fractal.service.fractal.model.FractalImage;
import ru.kfu.fractal.service.fractal.model.Pixel;
import ru.kfu.fractal.service.fractal.model.Point;
import ru.kfu.fractal.service.fractal.model.Rect;
import ru.kfu.fractal.service.fractal.transformation.AffineTransformation;
import ru.kfu.fractal.service.fractal.transformation.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@UtilityClass
public class RendererUtils {

    public static Point randomPointInRect(Rect world, ThreadLocalRandom random) {
        return new Point(
                world.x() + random.nextDouble() * world.width(),
                world.y() + random.nextDouble() * world.height()
        );
    }

    public static List<AffineTransformation> generateRandomAffineTransformations(Long affineCount) {
        List<AffineTransformation> affineTransformations = new ArrayList<>();
        for (long i = 0L; i < affineCount; i++) {
            AffineTransformation transformation = new AffineTransformation(
                    AffineCoefficientUtils.generateRandomAffineCoefficients(ThreadLocalRandom.current())
            );
            affineTransformations.add(transformation);
        }
        return affineTransformations;
    }

    public static <T> T randomTransformation(List<T> transformations, ThreadLocalRandom random) {
        int randomIndex = random.nextInt(transformations.size());
        return transformations.get(randomIndex);
    }

    public static Pixel calculateCoordinates(
            FractalImage image,
            Rect world,
            Point point
    ) {
        return image.pixel(
                (int) ((point.x() - world.x()) / world.width() * image.width()),
                (int) ((point.y() - world.y()) / world.height() * image.height())
        );
    }

    public static List<Point> generateSymmetricPoints(Point point, Long symmetryCount) {
        List<Point> symmetricPoints = new ArrayList<>();
        double angleStep = 2 * Math.PI / symmetryCount;

        for (long i = 0; i < symmetryCount; i++) {
            double currentAngle = i * angleStep;
            double x = point.x() * Math.cos(currentAngle) - point.y() * Math.sin(currentAngle);
            double y = point.x() * Math.sin(currentAngle) + point.y() * Math.cos(currentAngle);
            symmetricPoints.add(new Point(x, y));
        }

        return symmetricPoints;
    }

    public static List<Transformation> getTransformationsForUse(List<Transformation> transformations, GenerateFractalRequest request) {
        List<String> namesToUse = request.transformationTypes();

        return transformations.stream()
                .filter(t -> namesToUse.contains(t.getName()))
                .collect(Collectors.toList());
    }

    public static List<String> getAllTransformationNames(List<Transformation> transformations) {
        return transformations.stream()
                .map(Transformation::getName)
                .collect(Collectors.toList());
    }
}