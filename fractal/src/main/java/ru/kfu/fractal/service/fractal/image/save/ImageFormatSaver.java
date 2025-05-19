package ru.kfu.fractal.service.fractal.image.save;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kfu.fractal.service.fractal.model.FractalImage;
import ru.kfu.fractal.service.fractal.model.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class ImageFormatSaver implements ImageSaver {

    @Override
    public byte[] save(FractalImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.pixel(x, y);
                Color color = new Color(pixel.getR(), pixel.getG(), pixel.getB());
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            log.error("File converting error: ", e);
        }

        return baos.toByteArray();
    }
}