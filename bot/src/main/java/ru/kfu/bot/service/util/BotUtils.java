package ru.kfu.bot.service.util;

import ru.kfu.bot.dto.GenerateFractalRequest;
import ru.kfu.bot.dto.ListTransformationTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BotUtils {

    public static String getStringFromGenerateFractalRequest(GenerateFractalRequest generateFractalRequest) {
        return "Количество аффинных преобразований: " + generateFractalRequest.affineCount() +
                "\nКоличество симметрий: " + generateFractalRequest.symmetryCount() +
                "\nКоличество начальных точек: " + generateFractalRequest.samples() +
                "\nКоличество итераций: " + generateFractalRequest.iterations() +
                "\nШирина изображения: " + generateFractalRequest.imageWidth() +
                "\nВысота изображения: " + generateFractalRequest.imageHeight() +
                "\nX и Y координаты прямоугольника масштаба: " + generateFractalRequest.rectX() +
                " " + generateFractalRequest.rectY() +
                "\nШирина и высота прямоугольника масштаба: " + generateFractalRequest.rectWidth() +
                " " + generateFractalRequest.rectHeight() +
                "\nТипы преобразований: " + String.join(", ", generateFractalRequest.transformationTypes());
    }

    public static List<String> getInputTransformations(String message, ListTransformationTypes transformationTypes) {
        List<String> transformationList = new ArrayList<>();

        String[] parts = message.split(" ");
        for (String part : parts) {
            int value = Integer.parseInt(part);
            if (value >= 1 && value <= transformationTypes.transformationTypes().size()) {
                transformationList.add(transformationTypes.transformationTypes().get(value - 1));
            }
            else {
                throw new NumberFormatException("Incorrect transformation list");
            }
        }
        return transformationList;
    }

    public static String getStringFromListTransformationTypes(ListTransformationTypes listTransformationTypes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listTransformationTypes.transformationTypes().size(); i++) {
            sb.append(i + 1)
                    .append(". ")
                    .append(listTransformationTypes.transformationTypes().get(i))
                    .append("\n");
        }
        return sb.toString();
    }

    public static List<String> getRandomTransformations(ThreadLocalRandom random, ListTransformationTypes transformationTypes) {
        int transformationCount = random.nextInt(1, transformationTypes.transformationTypes().size() + 1);
        List<String> randomTransformations = new ArrayList<>();
        for (int i = 0; i < transformationCount; i++) {
            int randomNumber = ThreadLocalRandom.current().nextInt(1, transformationTypes.transformationTypes().size() + 1);
            randomTransformations.add(transformationTypes.transformationTypes().get(randomNumber - 1));
        }
        return randomTransformations;
    }
}
