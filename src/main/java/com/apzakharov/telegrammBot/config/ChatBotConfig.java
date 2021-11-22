package com.apzakharov.telegrammBot.config;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bot.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static com.apzakharov.telegrammBot.bot.BotContext.putInContextChatBotMap;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:telegram.properties")
public class ChatBotConfig {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final ChatService chatService;

    @Bean
    public ChatBot chatBot() {
        ChatBot chatBot =ChatBot.builder()
                                .botName(botName)
                                .botToken(botToken)
                                .chatService(chatService)
                                .build();

        putInContextChatBotMap(chatBot);

        return chatBot;
    }


}
