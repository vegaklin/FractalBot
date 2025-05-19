package ru.kfu.fractal.service.fractal.model;

public record FractalImage(Pixel[] data, int width, int height) {

    public static FractalImage create(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive.");
        }
        Pixel[] data = new Pixel[width * height];
        for (int i = 0; i < width * height; i++) {
            data[i] = new Pixel(0, 0, 0, 0, 0);
        }
        return new FractalImage(data, width, height);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && x < width
            && y >= 0 && y < height;
    }

    public Pixel pixel(int x, int y) {
        if (!contains(x, y)) {
            return null;
        }
        return data[y * width + x];
    }
}
