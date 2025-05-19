package ru.kfu.fractal.service.fractal.render;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kfu.fractal.dto.GenerateFractalRequest;
import ru.kfu.fractal.service.fractal.model.FractalImage;
import ru.kfu.fractal.service.fractal.model.Pixel;
import ru.kfu.fractal.service.fractal.model.Point;
import ru.kfu.fractal.service.fractal.model.Rect;
import ru.kfu.fractal.service.fractal.transformation.AffineTransformation;
import ru.kfu.fractal.service.fractal.transformation.Transformation;
import ru.kfu.fractal.service.fractal.util.RendererUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class FractalRenderer implements Renderer {

    @Value("${fractal.threads_count}")
    private int threadsCount;

    private final List<Transformation> transformations;

    public static final int STEPS_FOR_CORRECTION = 20;
    public static final int THREAD_WAITING_TIME = 15;

    @Override
    public FractalImage render(Rect world, FractalImage canvas, GenerateFractalRequest request) {
        FractalImage image = FractalImage.create(canvas.width(), canvas.height());
        List<AffineTransformation> affineTransformations = RendererUtils.generateRandomAffineTransformations(request.affineCount());

        renderSamples(image, world, affineTransformations, request);
        return image;
    }

    public void renderSamples(
            FractalImage image,
            Rect world,
            List<AffineTransformation> affineTransformations,
            GenerateFractalRequest request
    ) {
        List<Transformation> variations = RendererUtils.getTransformationsForUse(transformations, request);

        var executor = Executors.newFixedThreadPool(threadsCount);
        try {
            for (int i = 0; i < request.samples(); i++) {
                executor.execute(() -> renderOneSample(image, world, affineTransformations, request, variations));
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(THREAD_WAITING_TIME, TimeUnit.MINUTES)) {
                    executor.shutdownNow();
                    if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                        log.error("The threads didn't finish on time!");
                    }
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
                log.error("The threads were interrupted", e);
            }
        }
    }

    private void renderOneSample(
            FractalImage image,
            Rect world,
            List<AffineTransformation> affineTransformations,
            GenerateFractalRequest request,
            List<Transformation> variations
    ) {
        Point point = RendererUtils.randomPointInRect(world, ThreadLocalRandom.current());

        for (int step = -STEPS_FOR_CORRECTION; step < request.iterations(); step++) {
            AffineTransformation affineTransformation = RendererUtils.randomTransformation(
                    affineTransformations,
                    ThreadLocalRandom.current()
            );
            Transformation transformation = RendererUtils.randomTransformation(variations, ThreadLocalRandom.current());

            point = affineTransformation.apply(point);
            point = transformation.apply(point);

            if (step > 0) {
                processSymmetricPoints(image, world, point, affineTransformation, request);
            }
        }
    }

    private void processSymmetricPoints(
            FractalImage image,
            Rect world,
            Point point,
            AffineTransformation affineTransformation,
            GenerateFractalRequest request
    ) {
        List<Point> symmetricPoints = RendererUtils.generateSymmetricPoints(point, request.symmetryCount());
        for (Point symmetricPoint : symmetricPoints) {
            if (world.contains(symmetricPoint)) {
                Pixel pixel = RendererUtils.calculateCoordinates(image, world, symmetricPoint);
                if (pixel != null) {
                    synchronized (pixel) {
                        pixel.pixelProcessing(affineTransformation.affineCoefficient().color());
                    }
                }
            }
        }
    }
}
