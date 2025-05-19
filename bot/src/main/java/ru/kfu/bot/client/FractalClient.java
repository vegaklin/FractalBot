package ru.kfu.bot.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.kfu.bot.dto.*;
import ru.kfu.bot.exception.FractalClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FractalClient {

    private final WebClient fractalWebClient;

    public Mono<Void> registerChat(Long chatId) {
        return fractalWebClient
                .post()
                .uri("/tg-chat/{id}", chatId)
                .exchangeToMono(response -> handleResponse(response, Void.class));
    }

    public Mono<ListTransformationTypes> getTransformationTypes() {
        return fractalWebClient
                .get()
                .uri("/fractals/transformations")
                .exchangeToMono(response -> handleResponse(response, ListTransformationTypes.class));
    }

    public Mono<ListFractalResponse> getLastFractals(Long chatId) {
        return fractalWebClient
                .get()
                .uri("/fractals")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .exchangeToMono(response -> handleResponse(response, ListFractalResponse.class));
    }

    public Mono<FractalResponse> generateFractal(Long chatId, GenerateFractalRequest request) {
        return fractalWebClient
                .post()
                .uri("/fractals")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .bodyValue(request)
                .exchangeToMono(response -> handleResponse(response, FractalResponse.class));
    }

    private <T> Mono<T> handleResponse(ClientResponse response, Class<T> responseType) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(responseType).doOnSuccess(result -> log.info("Successfully: {}", result));
        } else {
            return response.bodyToMono(ApiErrorResponse.class).map(error -> {
                log.error("Client error: {} (code: {})", error.description(), error.code());
                throw new FractalClientException(error);
            });
        }
    }
}