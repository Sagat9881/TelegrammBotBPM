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
public  class BotContext {

    private static final Map<String,ChatBot> chatBotContextMap = new HashMap<>();
    private static final Map<String,CamundaClient> camundaClientContextMap = new HashMap<>();
    private static final Map<String,ProcessStartMessageCorrelationRequest> awaitingChatMap = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger(BotContext.class);

    public static void putInContextChatBotMap(ChatBot chatBot){
        chatBotContextMap.put(chatBot.getBotUsername(),chatBot);
    }

    public static ChatBot getFromContextChatBotMap(String botName){
        return chatBotContextMap.get(botName);
    }

    public static void putInCamundaClientContextMap(CamundaClient camundaClient){
        camundaClientContextMap.put(camundaClient.getBotName(),camundaClient);
    }

    public static CamundaClient getFromCamundaClientContextMap(String botName){
        return camundaClientContextMap.get(botName);
    }

    public static void putInAwaitingChatMap(String chatID, ProcessStartMessageCorrelationRequest request){
        awaitingChatMap.put(chatID,request);
    }

    public static ProcessStartMessageCorrelationRequest getFromAwaitingChatMap(String chatID){
        ProcessStartMessageCorrelationRequest request = awaitingChatMap.get(chatID);
        awaitingChatMap.remove(chatID);

        return request;

    }



}
