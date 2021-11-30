package com.apzakharov.telegrammBot.bot;

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

    private ChatBot bot;
    private CamundaClient camundaClient;

    private static Map<String,ChatBot> chatBotContextMap = new HashMap<>();
    private static Map<String,CamundaClient> camundaClientContextMap = new HashMap<>();
    private static Map<String,Map<String,String>> awaitingChatMap = new HashMap<>();

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

    public static void putInAwaitingChatMap(String chatID,Map<String,String> messageNameAndBusinessKey){
        awaitingChatMap.put(chatID,messageNameAndBusinessKey);
    }

    public static Map<String,String> getFromAwaitingChatMap(String chatID){
        Map<String,String> messageNameAndBusinessKey = awaitingChatMap.get(chatID);
        awaitingChatMap.remove(chatID);

        return messageNameAndBusinessKey;

    }






}
