package ru.kfu.bot.service.command;

import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kfu.bot.client.FractalClient;
import ru.kfu.bot.dto.ListFractalResponse;
import ru.kfu.bot.exception.FractalClientException;
import ru.kfu.bot.service.TelegramMessenger;
import ru.kfu.bot.service.cache.state.UserStateRepository;
import ru.kfu.bot.service.model.BotState;
import ru.kfu.bot.service.util.BotUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryCommand  implements CommandHandler {

    private final TelegramMessenger telegramMessenger;

    private final FractalClient fractalClient;

    private final UserStateRepository inMemoryUserStateRepository;

    @Override
    public String commandName() {
        return "/history";
    }

    @Override
    public void handle(Long chatId, String message) {
        log.info("Processing '/history' command for chatId {}", chatId);

        try {
            ListFractalResponse listFractalResponse = fractalClient.getLastFractals(chatId).block();
            if(listFractalResponse == null || listFractalResponse.fractalResponseList().isEmpty()) {
                telegramMessenger.sendMessage(chatId, "Исторя пуста. Сгенерируйте первый фрактал!");
                inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
                return;
            }

            for (int i = 0; i < listFractalResponse.fractalResponseList().size(); i++) {
                SendPhoto sendPhoto = new SendPhoto(
                        chatId,
                        listFractalResponse.fractalResponseList().get(i).image()
                ).fileName("fractal.png").caption(
                        i + 1 + ". " + BotUtils.getStringFromGenerateFractalRequest(
                                listFractalResponse.fractalResponseList().get(i).generateFractalRequest()
                        )
                );
                telegramMessenger.sendImage(chatId, sendPhoto);
            }

            telegramMessenger.sendMessage(chatId, "Введите номер изображения, чтобы снова сгенерировать с такой же конфигурацией, либо введите \"Выход\"");
            inMemoryUserStateRepository.setState(chatId, BotState.AWAITING_HISTORY_CONFIG);
        } catch (FractalClientException e) {
            log.error("Error getting history types {}", chatId, e);
            telegramMessenger.sendMessage(chatId, "Произошла ошибка загрузки истории. Попробуйте еще раз!");
            inMemoryUserStateRepository.setState(chatId, BotState.DEFAULT);
        }
    }
}
