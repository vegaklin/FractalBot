package ru.kfu.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.kfu.bot.configuration.BotConfig;
import ru.kfu.bot.configuration.FractalConfig;

@SpringBootApplication
@EnableConfigurationProperties({BotConfig.class, FractalConfig.class})
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}