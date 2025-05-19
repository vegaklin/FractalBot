package ru.kfu.fractal.service.fractal.image.correction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kfu.fractal.service.fractal.model.FractalImage;
import ru.kfu.fractal.service.fractal.model.Pixel;

@Component
public class GammaCorrection implements ImageProcessor {

    @Value("${fractal.gamma}")
    private double gamma;

    @Override
    public void process(FractalImage image) {
        double max = 0.0;

        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.pixel(x, y);
                if (pixel != null) {
                    if (pixel.getHitCount() != 0) {
                        pixel.setNormal(Math.log10(pixel.getHitCount()));
                        max = Math.max(max, pixel.getNormal());
                    }
                }
            }
        }

        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.pixel(x, y);
                if (pixel != null) {
                    pixel.setNormal(pixel.getNormal() / max);
                    pixel.setR((int) (pixel.getR() * Math.pow(pixel.getNormal(), (1.0 / gamma))));
                    pixel.setG((int) (pixel.getG() * Math.pow(pixel.getNormal(), (1.0 / gamma))));
                    pixel.setB((int) (pixel.getB() * Math.pow(pixel.getNormal(), (1.0 / gamma))));
                }
            }
        }
    }
}