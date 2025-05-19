package ru.kfu.bot.service.command;


import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kfu.bot.client.FractalClient;
import ru.kfu.bot.dto.FractalResponse;
import ru.kfu.bot.dto.GenerateFractalRequest;
import ru.kfu.bot.dto.ListTransformationTypes;
import ru.kfu.bot.exception.FractalClientException;
import ru.kfu.bot.service.TelegramMessenger;
import ru.kfu.bot.service.util.BotUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class RandomCommand implements CommandHandler {

    private final TelegramMessenger telegramMessenger;

    private final FractalClient fractalClient;

    public static final long AFFINE_COUNT_MIN = 1;
    public static final long AFFINE_COUNT_MAX = 30;

    public static final long SYMMETRY_COUNT_MIN = 1;
    public static final long SYMMETRY_COUNT_MAX = 10;

    public static final long SAMPLES_MIN = 10;
    public static final long SAMPLES_MAX = 100;

    public static final long ITERATIONS_MIN = 500_000;
    public static final long ITERATIONS_MAX = 2_000_000;

    public static final long IMAGE_WIDTH = 1000;
    public static final long IMAGE_HEIGHT = 1000;

    public static final double RECT_X = -1.5;
    public static final double RECT_Y = -1.5;
    public static final double RECT_WIDTH = 3.0;
    public static final double RECT_HEIGHT = 3.0;

    @Override
    public String commandName() {
        return "/random";
    }

    @Override
    public void handle(Long chatId, String message) {
        log.info("Processing '/random' command for chatId {}", chatId);

        try {
            ListTransformationTypes transformationTypes = fractalClient.getTransformationTypes().block();
            if (transformationTypes == null || transformationTypes.transformationTypes().isEmpty()) {
                telegramMessenger.sendMessage(chatId, "Доступных транформаций нет. Попробуйте еще раз!");
                return;
            }

            ThreadLocalRandom random = ThreadLocalRandom.current();
            List<String> randomTransformations = BotUtils.getRandomTransformations(random, transformationTypes);
            GenerateFractalRequest generateFractalRequest = new GenerateFractalRequest(
                    random.nextLong(AFFINE_COUNT_MIN, AFFINE_COUNT_MAX),
                    random.nextLong(SYMMETRY_COUNT_MIN, SYMMETRY_COUNT_MAX),
                    random.nextLong(SAMPLES_MIN, SAMPLES_MAX),
                    random.nextLong(ITERATIONS_MIN, ITERATIONS_MAX),
                    IMAGE_WIDTH,
                    IMAGE_HEIGHT,
                    RECT_X,
                    RECT_Y,
                    RECT_WIDTH,
                    RECT_HEIGHT,
                    randomTransformations
            );

            FractalResponse fractalResponse = fractalClient.generateFractal(chatId, generateFractalRequest).block();
            if (fractalResponse == null) {
                telegramMessenger.sendMessage(chatId, "Фрактал не смог сгенерироваться. Попробуйте еще раз!");
                return;
            }

            SendPhoto sendPhoto = new SendPhoto(chatId, fractalResponse.image()).fileName("fractal.png").caption(BotUtils.getStringFromGenerateFractalRequest(
                    fractalResponse.generateFractalRequest())
            );
            telegramMessenger.sendImage(chatId, sendPhoto);
        } catch (FractalClientException e) {
            log.error("Error generation random fractal for {}", chatId, e);
            telegramMessenger.sendMessage(chatId, "Произошла ошибка при генерации слечайного. Попробуйте еще раз!");
        }
    }
}