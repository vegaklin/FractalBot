package ru.kfu.bot.service.cache.fractal;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class InMemoryUserFractalRepository implements UserFractalRepository {
    private final Map<Long, Long> userAffineCount = new ConcurrentHashMap<>();
    private final Map<Long, Long> userSymmetryCount = new ConcurrentHashMap<>();
    private final Map<Long, Long> userSamples = new ConcurrentHashMap<>();
    private final Map<Long, Long> userIterations = new ConcurrentHashMap<>();
    private final Map<Long, Long> userImageWidth = new ConcurrentHashMap<>();
    private final Map<Long, Long> userImageHeight = new ConcurrentHashMap<>();
    private final Map<Long, Double> userRectX = new ConcurrentHashMap<>();
    private final Map<Long, Double> userRectY = new ConcurrentHashMap<>();
    private final Map<Long, Double> userRectWidth = new ConcurrentHashMap<>();
    private final Map<Long, Double> userRectHeight = new ConcurrentHashMap<>();
    private final Map<Long, List<String>> userTransformationTypes = new ConcurrentHashMap<>();

    @Override
    public void setAffineCount(Long chatId, Long affineCount) {
        userAffineCount.put(chatId, affineCount);
        log.info("Set affineCount for chatId: {} to {}", chatId, affineCount);
    }

    @Override
    public Long getAffineCount(Long chatId) {
        return userAffineCount.get(chatId);
    }

    @Override
    public void setSymmetryCount(Long chatId, Long symmetryCount) {
        userSymmetryCount.put(chatId, symmetryCount);
        log.info("Set symmetryCount for chatId: {} to {}", chatId, symmetryCount);
    }

    @Override
    public Long getSymmetryCount(Long chatId) {
        return userSymmetryCount.get(chatId);
    }

    @Override
    public void setSamples(Long chatId, Long samples) {
        userSamples.put(chatId, samples);
        log.info("Set samples for chatId: {} to {}", chatId, samples);
    }

    @Override
    public Long getSamples(Long chatId) {
        return userSamples.get(chatId);
    }

    @Override
    public void setIterations(Long chatId, Long iterations) {
        userIterations.put(chatId, iterations);
        log.info("Set iterations for chatId: {} to {}", chatId, iterations);
    }

    @Override
    public Long getIterations(Long chatId) {
        return userIterations.get(chatId);
    }

    @Override
    public void setImageWidth(Long chatId, Long imageWidth) {
        userImageWidth.put(chatId, imageWidth);
        log.info("Set imageWidth for chatId: {} to {}", chatId, imageWidth);
    }

    @Override
    public Long getImageWidth(Long chatId) {
        return userImageWidth.get(chatId);
    }

    @Override
    public void setImageHeight(Long chatId, Long imageHeight) {
        userImageHeight.put(chatId, imageHeight);
        log.info("Set imageHeight for chatId: {} to {}", chatId, imageHeight);
    }

    @Override
    public Long getImageHeight(Long chatId) {
        return userImageHeight.get(chatId);
    }

    @Override
    public void setRectX(Long chatId, Double rectX) {
        userRectX.put(chatId, rectX);
        log.info("Set rectX for chatId: {} to {}", chatId, rectX);
    }

    @Override
    public Double getRectX(Long chatId) {
        return userRectX.get(chatId);
    }

    @Override
    public void setRectY(Long chatId, Double rectY) {
        userRectY.put(chatId, rectY);
        log.info("Set rectY for chatId: {} to {}", chatId, rectY);
    }

    @Override
    public Double getRectY(Long chatId) {
        return userRectY.get(chatId);
    }

    @Override
    public void setRectWidth(Long chatId, Double rectWidth) {
        userRectWidth.put(chatId, rectWidth);
        log.info("Set rectWidth for chatId: {} to {}", chatId, rectWidth);
    }

    @Override
    public Double getRectWidth(Long chatId) {
        return userRectWidth.get(chatId);
    }

    @Override
    public void setRectHeight(Long chatId, Double rectHeight) {
        userRectHeight.put(chatId, rectHeight);
        log.info("Set rectHeight for chatId: {} to {}", chatId, rectHeight);
    }

    @Override
    public Double getRectHeight(Long chatId) {
        return userRectHeight.get(chatId);
    }

    @Override
    public void setTransformationTypes(Long chatId, List<String> transformationTypes) {
        userTransformationTypes.put(chatId, transformationTypes);
        log.info("Set TransformationTypes for chatId: {} to {}", chatId, transformationTypes);
    }

    @Override
    public List<String> getTransformationTypes(Long chatId) {
        return userTransformationTypes.getOrDefault(chatId, new ArrayList<>());
    }

    @Override
    public void clear(Long chatId) {
        userAffineCount.remove(chatId);
        userSymmetryCount.remove(chatId);
        userSamples.remove(chatId);
        userIterations.remove(chatId);
        userImageWidth.remove(chatId);
        userImageHeight.remove(chatId);
        userRectX.remove(chatId);
        userRectY.remove(chatId);
        userRectWidth.remove(chatId);
        userRectHeight.remove(chatId);
        userTransformationTypes.remove(chatId);
        log.info("Cleared data for chatId: {}", chatId);
    }
}