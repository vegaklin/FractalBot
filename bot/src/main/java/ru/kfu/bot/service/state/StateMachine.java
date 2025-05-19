package ru.kfu.bot.service.state;

import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kfu.bot.client.FractalClient;
import ru.kfu.bot.dto.FractalResponse;
import ru.kfu.bot.dto.GenerateFractalRequest;
import ru.kfu.bot.dto.ListFractalResponse;
import ru.kfu.bot.dto.ListTransformationTypes;
import ru.kfu.bot.exception.FractalClientException;
import ru.kfu.bot.service.TelegramMessenger;
import ru.kfu.bot.service.cache.fractal.UserFractalRepository;
import ru.kfu.bot.service.cache.state.UserStateRepository;
import ru.kfu.bot.service.model.BotState;
import ru.kfu.bot.service.util.BotUtils;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackStateMachine {

    private final TelegramMessenger telegramMessenger;

    private final FractalClient fractalClient;

    private final UserFractalRepository inMemoryUserFractalRepository;
    private final UserStateRepository inMemoryUserStateRepository;

    public BotState getBotState(Long chatId) {
        return inMemoryUserStateRepository.getState(chatId);
    }

    public void trackProcess(Long chatId, String message) {
        log.info("Processing message '{}' for chatId {}", message, chatId);
        switch (getBotState(chatId)) {
            case AWAITING_AFFINE_COUNT -> handleAffineCount(chatId, message);
            case AWAITING_SYMMETRY_COUNT -> handSymmetryCount(chatId, message);
            case AWAITING_SAMPLES -> handleSamples(chatId, message);
            case AWAITING_ITERATIONS -> handleIterations(chatId, message);
            case AWAITING_IMAGE_WIDTH -> handleImageWidth(chatId, message);
            case AWAITING_IMAGE_HEIGHT -> handleImageHeight(chatId, message);
            case AWAITING_RECT_X -> handleRectX(chatId, message);
            case AWAITING_RECT_Y -> handleRectY(chatId, message);
            case AWAITING_RECT_WIDTH -> handleRectWidth(chatId, message);
            case AWAITING_RECT_HEIGHT -> handleRectHeight(chatId, message);
            case AWAITING_TRANSFORMATION_TYPES -> handleTransformationTypes(chatId, message);
            case AWAITING_HISTORY_CONFIG -> handleHistoryConfig(chatId, message);
            default -> {
                log.error("Unexpected bot state for chatId {}: {}", chatId, getBotState(chatId));
                telegramMessenger.sendMessage(chatId, "Произошла ошибка с состоянием бота, повторите команду!");
            }
        }
    }

    private void handleAffineCount(Long chatId, String message) {
        log.info("Handling AffineCount for chatId {}: {}", chatId, message);

        try {
            Long value = Long.parseLong(message);

            if (value <= 0 || value > 50) {
                sendIncorrectSizeMessage(chatId);
                return;
            }

            inMemoryUserFractalRepository.setAffineCount(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_SYMMETRY_COUNT);

            telegramMessenger.sendMessage(chatId, "Введите количетво симметрии для фрактала:");

            log.info("AffineCount set for chatId {}, moving to AWAITING_SYMMETRY_COUNT state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handSymmetryCount(Long chatId, String message) {
        log.info("Handling SymmetryCount for chatId {}: {}", chatId, message);

        try {
            long value = Long.parseLong(message);

            if (value <= 0 || value > 50) {
                sendIncorrectSizeMessage(chatId);
                return;
            }

            inMemoryUserFractalRepository.setSymmetryCount(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_SAMPLES);

            telegramMessenger.sendMessage(chatId, "Введите количество начальных точек:");

            log.info("SymmetryCount set for chatId {}, moving to AWAITING_SAMPLES state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handleSamples(Long chatId, String message) {
        log.info("Handling Samples for chatId {}: {}", chatId, message);

        try {
            long value = Long.parseLong(message);

            if (value <= 0 || value > 100000) {
                sendIncorrectSizeMessage(chatId);
                return;
            }

            inMemoryUserFractalRepository.setSamples(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_ITERATIONS);

            telegramMessenger.sendMessage(chatId, "Введите количество итерации на одну начальную точку:");

            log.info("Samples set for chatId {}, moving to AWAITING_ITERATIONS state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handleIterations(Long chatId, String message) {
        log.info("Handling Iterations for chatId {}: {}", chatId, message);

        try {
            long value = Long.parseLong(message);

            if (value <= 0 || value > 1000000000) {
                sendIncorrectSizeMessage(chatId);
                return;
            }

            inMemoryUserFractalRepository.setIterations(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_IMAGE_WIDTH);

            telegramMessenger.sendMessage(chatId, "Введите ширину (количество пикселей) разрешения изображения:");

            log.info("Iterations set for chatId {}, moving to AWAITING_IMAGE_WIDTH state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handleImageWidth(Long chatId, String message) {
        log.info("Handling ImageWidth for chatId {}: {}", chatId, message);

        try {
            long value = Long.parseLong(message);

            if (value <= 30 || value > 5000) {
                sendIncorrectSizeMessage(chatId);
                return;
            }

            inMemoryUserFractalRepository.setImageWidth(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_IMAGE_HEIGHT);

            telegramMessenger.sendMessage(chatId, "Введите выосоту (количество пикселей) разрешения изображения:");

            log.info("ImageWidth set for chatId {}, moving to AWAITING_IMAGE_HEIGHT state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handleImageHeight(Long chatId, String message) {
        log.info("Handling ImageHeight for chatId {}: {}", chatId, message);

        try {
            long value = Long.parseLong(message);

            if (value <= 30 || value > 5000) {
                sendIncorrectSizeMessage(chatId);
                return;
            }

            inMemoryUserFractalRepository.setImageHeight(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_RECT_X);

            telegramMessenger.sendMessage(chatId, "Введите координату X нижнего левого угла видимой области:");

            log.info("ImageHeight set for chatId {}, moving to AWAITING_RECT_X state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handleRectX(Long chatId, String message) {
        log.info("Handling RectX for chatId {}: {}", chatId, message);

        try {
            Long value = Long.parseLong(message);

            inMemoryUserFractalRepository.setRectX(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_RECT_Y);

            telegramMessenger.sendMessage(chatId, "Введите координату Y нижнего левого угла видимой области:");

            log.info("RectX set for chatId {}, moving to AWAITING_RECT_Y state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handleRectY(Long chatId, String message) {
        log.info("Handling RectY for chatId {}: {}", chatId, message);

        try {
            Long value = Long.parseLong(message);

            inMemoryUserFractalRepository.setRectY(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_RECT_WIDTH);

            telegramMessenger.sendMessage(chatId, "Введите ширину видимой области:");

            log.info("RectY set for chatId {}, moving to AWAITING_RECT_WIDTH state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handleRectWidth(Long chatId, String message) {
        log.info("Handling RectWidth for chatId {}: {}", chatId, message);

        try {
            Long value = Long.parseLong(message);

            inMemoryUserFractalRepository.setRectWidth(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_RECT_HEIGHT);

            telegramMessenger.sendMessage(chatId, "Введите высоту видимой области:");

            log.info("RectWidth set for chatId {}, moving to AWAITING_RECT_HEIGHT state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        }
    }

    private void handleRectHeight(Long chatId, String message) {
        log.info("Handling RectHeight for chatId {}: {}", chatId, message);

        try {
            Long value = Long.parseLong(message);

            inMemoryUserFractalRepository.setRectHeight(chatId, value);
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_TRANSFORMATION_TYPES);

            ListTransformationTypes transformationTypes = fractalClient.getTransformationTypes().block();
            if (transformationTypes == null || transformationTypes.transformationTypes().isEmpty()) {
                telegramMessenger.sendMessage(chatId, "Доступных транформаций нет. Попробуйте еще раз!");
                inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
                return;
            }

            telegramMessenger.sendMessage(chatId, BotUtils.getStringFromListTransformationTypes(transformationTypes));
            telegramMessenger.sendMessage(chatId, "Введите номера нужных трансформации через пробел:");

            log.info("RectHeight set for chatId {}, moving to AWAITING_TRANSFORMATION_TYPES state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        } catch (FractalClientException e) {
            log.error("Error getting transformation types {}", chatId, e);
            telegramMessenger.sendMessage(chatId, "Произошла ошибка загрузки транформации. Попробуйте еще раз!");
            inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
        }
    }

    private void handleTransformationTypes(Long chatId, String message) {
        log.info("Handling TransformationTypes for chatId {}: {}", chatId, message);

        try {
            ListTransformationTypes transformationTypes = fractalClient.getTransformationTypes().block();
            if (transformationTypes == null || transformationTypes.transformationTypes().isEmpty()) {
                telegramMessenger.sendMessage(chatId, "Доступных транформаций нет. Попробуйте еще раз!");
                inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
                return;
            }

            List<String> transformations = BotUtils.getInputTransformations(message, transformationTypes);

            inMemoryUserFractalRepository.setTransformationTypes(chatId, transformations);
            inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);

            log.info("TransformationTypes set for chatId {}, moving to DEFAULT state", chatId);
        } catch (NumberFormatException e) {
            log.error("For chatId {} incorrect numbers parsing", chatId, e);
            telegramMessenger.sendMessage(chatId, "Некорректные значения. Введите заново числа чрез пробел:");
            return;
        } catch (FractalClientException e) {
            log.error("Error getting transformation types {}", chatId, e);
            telegramMessenger.sendMessage(chatId, "Произошла ошибка загрузки транформации. Попробуйте еще раз!");
            inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
            return;
        }

        try {
            GenerateFractalRequest generateFractalRequest = new GenerateFractalRequest(
                    inMemoryUserFractalRepository.getAffineCount(chatId),
                    inMemoryUserFractalRepository.getSymmetryCount(chatId),
                    inMemoryUserFractalRepository.getSamples(chatId),
                    inMemoryUserFractalRepository.getIterations(chatId),
                    inMemoryUserFractalRepository.getImageWidth(chatId),
                    inMemoryUserFractalRepository.getImageHeight(chatId),
                    inMemoryUserFractalRepository.getRectX(chatId),
                    inMemoryUserFractalRepository.getRectY(chatId),
                    inMemoryUserFractalRepository.getRectWidth(chatId),
                    inMemoryUserFractalRepository.getRectHeight(chatId),
                    inMemoryUserFractalRepository.getTransformationTypes(chatId)
            );

            FractalResponse fractalResponse = fractalClient.generateFractal(chatId, generateFractalRequest).block();
            if (fractalResponse == null) {
                telegramMessenger.sendMessage(chatId, "Фрактал не смог сгенерироваться. Попробуйте еще раз!");
                inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
                return;
            }

            SendPhoto sendPhoto = new SendPhoto(chatId, fractalResponse.image()).fileName("fractal.png").caption(BotUtils.getStringFromGenerateFractalRequest(generateFractalRequest));
            telegramMessenger.sendImage(chatId, sendPhoto);
        } catch (FractalClientException e) {
            log.error("Error getting transformation types {}", chatId, e);
            telegramMessenger.sendMessage(chatId, "Произошла ошибка при генерации фрактала (возможно слишком большой размер). Попробуйте еще раз!");
        }

        inMemoryUserFractalRepository.clear(chatId);
        inMemoryUserStateRepository.clear(chatId);
    }

    private void handleHistoryConfig(Long chatId, String message) {
        log.info("Handling HistoryConfig for chatId {}: {}", chatId, message);

        try {
            if (message.equals("Выход")) {
                telegramMessenger.sendMessage(chatId, "Вы вышли из истории, можете ввести /generate для генерации нового фрактала");
                inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
                return;
            }

            int value = Integer.parseInt(message);

            ListFractalResponse listFractalResponse = fractalClient.getLastFractals(chatId).block();
            if(listFractalResponse == null || listFractalResponse.fractalResponseList().isEmpty()) {
                telegramMessenger.sendMessage(chatId, "Исторя пуста. Сгенерируйте первый фрактал!");
                inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
                return;
            }

            if (value <= 0 || value > listFractalResponse.fractalResponseList().size()) {
                telegramMessenger.sendMessage(chatId, "Число не совпадает с номером изображения. Попробуйте еще раз!");
                return;
            }

            FractalResponse fractalResponse = fractalClient.generateFractal(
                    chatId,
                    listFractalResponse.fractalResponseList().get(value - 1).generateFractalRequest()
            ).block();
            if (fractalResponse == null) {
                telegramMessenger.sendMessage(chatId, "Фрактал не смог сгенерироваться. Попробуйте еще раз!");
                inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
                return;
            }

            SendPhoto sendPhoto = new SendPhoto(chatId, fractalResponse.image()).fileName("fractal.png").caption(BotUtils.getStringFromGenerateFractalRequest(
                    listFractalResponse.fractalResponseList().get(value - 1).generateFractalRequest()
            ));
            telegramMessenger.sendImage(chatId, sendPhoto);

            log.info("HistoryConfig set for chatId {}, moving to DEFAULT state", chatId);
        } catch (NumberFormatException e) {
            sendIncorrectNumberMessage(chatId, e);
        } catch (FractalClientException e) {
            log.error("Error getting transformation types {}", chatId, e);
            telegramMessenger.sendMessage(chatId, "Произошла ошибка загрузки истории. Попробуйте еще раз!");
            inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
        }
    }

    private void sendIncorrectSizeMessage(Long chatId) {
        telegramMessenger.sendMessage(chatId, "Слишком маленькие или большие данные. Попробуйте еще раз:");
    }

    private void sendIncorrectNumberMessage(Long chatId, NumberFormatException e) {
        log.error("For chatId {} incorrect number parsing", chatId, e);
        telegramMessenger.sendMessage(chatId, "Некорректное значение. Введите заново:");
    }
}