package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartMessageCorrelationRequest;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import com.apzakharov.telegrammBot.model.Message;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apzakharov.telegrammBot.bot.BotContext.getFromAwaitingChatMap;


@Component
@RequiredArgsConstructor
public class ProcessReceive implements JavaDelegate {

    private static final Logger LOGGER = LogManager.getLogger(ProcessReceive.class);
    private static final String NEW_MESSAGE_NAME = "NewIncomingMessage";
    private static final String JSON_TYPE_STRING = "String";
    private final CamundaClient camundaClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("ProcessReceive.execute START:  " + delegateExecution);

//      TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)
        String input = (String) delegateExecution.getVariableTyped("Input").getValue();
        Long chatID = Long.valueOf((String) delegateExecution.getVariableTyped("ChatID").getValue());
        String outputText = chatID.equals(966663803L) ?
                "Люблю тебя :3" : "Тестовая заглушка работы сценария обработки ответа на сообщение (не на команду)";

        Long userId = null;

        try {
            userId = camundaClient.findChatByChatId(chatID).getUserId();
        } catch (Exception e) {
            LOGGER.info("NOT FOUND USER FOR CHATID: " + chatID);
            e.getLocalizedMessage();
        }

        Message message = Message.builder()
                .chatId(chatID)
                .userId(userId)
                .text(input)
                .build();

        ProcessVariable chatIdVar = ProcessVariable.builder()
                .value(chatID.toString())
                .type(JSON_TYPE_STRING)
                .build();

        ProcessVariable inputVar = ProcessVariable.builder()
                .value(input)
                .type(JSON_TYPE_STRING)
                .build();

        Map<String, ProcessVariable> correlationKeyMap = new HashMap<>();
        correlationKeyMap.put("ChatID", chatIdVar);

        Map<String, ProcessVariable> variablesMap = new HashMap<>();
        variablesMap.put("Input", inputVar);

        try {
            ProcessStartMessageCorrelationRequest buisnessCorrelation = getFromAwaitingChatMap(String.valueOf(chatID));
            buisnessCorrelation.setCorrelationKeys(correlationKeyMap);
            buisnessCorrelation.setProcessVariables(variablesMap);

            camundaClient.addMessage(message);
            camundaClient.createCorrelation(buisnessCorrelation);
        } catch (Exception e) {
            LOGGER.info("CANT CORRELATE MESSAGE FOR CHATID: " + chatID );
        }

        LOGGER.info("Result MessageCorrelation for chatID: " + chatID + "\n Result: пока хз, надо найти как логгировать");

    }


}
