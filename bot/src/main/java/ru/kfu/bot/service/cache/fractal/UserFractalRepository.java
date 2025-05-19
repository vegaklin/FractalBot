package ru.kfu.bot.service.cache.fractal;

import java.util.List;

public interface UserFractalRepository {
    void setAffineCount(Long chatId, Long affineCount);
    Long getAffineCount(Long chatId);

    void setSymmetryCount(Long chatId, Long symmetryCount);
    Long getSymmetryCount(Long chatId);

    void setSamples(Long chatId, Long samples);
    Long getSamples(Long chatId);

    void setIterations(Long chatId, Long iterations);
    Long getIterations(Long chatId);

    void setImageWidth(Long chatId, Long imageWidth);
    Long getImageWidth(Long chatId);

    void setImageHeight(Long chatId, Long imageHeight);
    Long getImageHeight(Long chatId);

    void setRectX(Long chatId, Double rectX);
    Double getRectX(Long chatId);

    void setRectY(Long chatId, Double rectY);
    Double getRectY(Long chatId);

    void setRectWidth(Long chatId, Double rectWidth);
    Double getRectWidth(Long chatId);

    void setRectHeight(Long chatId, Double rectHeight);
    Double getRectHeight(Long chatId);

    void setTransformationTypes(Long chatId, List<String> transformationTypes);
    List<String> getTransformationTypes(Long chatId);

    void clear(Long chatId);
}