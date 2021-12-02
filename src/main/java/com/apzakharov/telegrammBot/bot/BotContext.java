package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartMessageCorrelationRequest;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;


@Data
@Component
@RequiredArgsConstructor
public class BotContext {

    private static final Map<String, ChatBot> chatBotContextMap = new HashMap<>();
    private static final Map<String, CamundaClient> camundaClientContextMap = new HashMap<>();
    private static final Map<String, ProcessStartMessageCorrelationRequest> awaitingChatMap = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger(BotContext.class);

    public static void putInContextChatBotMap(ChatBot chatBot) {
        LOGGER.info("========================================================================================");
        LOGGER.info("putInContextChatBotMap: ChatBot: " + chatBot);
        chatBotContextMap.put(chatBot.getBotUsername(), chatBot);
        LOGGER.info("========================================================================================");
    }

    public static ChatBot getFromContextChatBotMap(String botName) {
        LOGGER.info("========================================================================================");
        LOGGER.info("getFromContextChatBotMap: botName: " + botName);
        Optional<ChatBot> chatBotOptional = Optional.of(chatBotContextMap.get(botName));

        ChatBot chatBot = chatBotOptional.orElseGet(() -> null);
        LOGGER.info("getFromContextChatBotMap: result: " + chatBot);
        LOGGER.info("========================================================================================");
        return chatBot;


    }

    public static void putInCamundaClientContextMap(CamundaClient camundaClient) {
        LOGGER.info("========================================================================================");
        LOGGER.info("putInCamundaClientContextMap: CamundaClient: " + camundaClient + " botName: " + camundaClient.getBotName());
        camundaClientContextMap.put(camundaClient.getBotName(), camundaClient);
        LOGGER.info("========================================================================================");
    }

    public static CamundaClient getFromCamundaClientContextMap(String botName) {
        LOGGER.info("========================================================================================");
        LOGGER.info("getFromCamundaClientContextMap: botName: " + botName);
        Optional<CamundaClient> camundaClientOptional = Optional.of(camundaClientContextMap.get(botName));

        CamundaClient camundaClient = camundaClientOptional.orElseGet(() -> null);
        LOGGER.info("getFromContextChatBotMap: result: " + camundaClient);
        LOGGER.info("========================================================================================");
        return camundaClient;
    }

    public static void putInAwaitingChatMap(String chatID, ProcessStartMessageCorrelationRequest request) {
        LOGGER.info("========================================================================================");
        LOGGER.info("putInAwaitingChatMap: ChatID: " + chatID + " request: " + request);
        awaitingChatMap.put(chatID, request);
        LOGGER.info("========================================================================================");
    }

    public static ProcessStartMessageCorrelationRequest getFromAwaitingChatMap(String chatID) {
        LOGGER.info("========================================================================================");
        LOGGER.info("getFromAwaitingChatMap: ChatID: " + chatID);
        Optional<ProcessStartMessageCorrelationRequest> requestOptional = Optional.of(awaitingChatMap.get(chatID));

        ProcessStartMessageCorrelationRequest request = requestOptional.orElseGet(() -> null);
        LOGGER.info("getFromAwaitingChatMap result: request: " + request);
        awaitingChatMap.remove(chatID);
        LOGGER.info("awaitingChatMap.remove(chatID) result: awaitingChatMap.get(chatID) = " + awaitingChatMap.get(chatID));
        LOGGER.info("========================================================================================");
        return request;

    }


}
