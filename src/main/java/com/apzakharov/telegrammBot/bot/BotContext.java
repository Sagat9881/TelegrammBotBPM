package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess.ProcessAnswer;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

@Data
@RequiredArgsConstructor
@Component
public class BotContext {

    private ChatBot bot;
    private CamundaClient camundaClient;

    private static Map<String,ChatBot> chatBotContextMap = new HashMap<>();
    private static Map<String,CamundaClient> camundaClientContextMap = new HashMap<>();
    private static Map<String,String> awaitingChatMap = new HashMap<>();

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

    public static void putInAwaitingChatMap(String processId,String executionId){
        awaitingChatMap.put(processId,executionId);
    }

    public static String getFromAwaitingChatMap(String processId){
        String executionId = awaitingChatMap.get(processId);
        awaitingChatMap.remove(processId);

        return executionId;

    }






}
