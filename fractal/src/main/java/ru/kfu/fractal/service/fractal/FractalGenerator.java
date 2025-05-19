package ru.kfu.fractal.service.fractal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kfu.fractal.dto.GenerateFractalRequest;
import ru.kfu.fractal.service.fractal.image.correction.ImageProcessor;
import ru.kfu.fractal.service.fractal.image.save.ImageSaver;
import ru.kfu.fractal.service.fractal.model.FractalImage;
import ru.kfu.fractal.service.fractal.model.Rect;
import ru.kfu.fractal.service.fractal.render.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FractalGenerator {

    private final Renderer renderer;

    private final ImageProcessor imageProcessor;
    private final ImageSaver imageSaver;

    public byte[] generateNewFractal(GenerateFractalRequest request) {
        Rect world = new Rect(
                request.rectX(),
                request.rectY(),
                request.rectWidth(),
                request.rectHeight()
        );

        FractalImage canvas = FractalImage.create(
                Math.toIntExact(request.imageWidth()),
                Math.toIntExact(request.imageHeight())
        );

        FractalImage image = renderer.render(world, canvas, request);

        imageProcessor.process(image);
        return imageSaver.save(image);
    }
}
