package com.apzakharov.telegrammBot.config;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import static com.apzakharov.telegrammBot.bot.BotContext.putInCamundaClientContextMap;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:telegram.properties")
public class CamundaClientConfig {

    @Value("${bot.name}")
    private String botName;

    private final RestTemplate template;

    @Bean
    @Qualifier
    public CamundaClient camundaClient() {
        CamundaClient camundaClient = CamundaClient.builder()
                .botName(botName)
                .template(template)
                .build();

        putInCamundaClientContextMap(camundaClient);

        return camundaClient;
    }
}
