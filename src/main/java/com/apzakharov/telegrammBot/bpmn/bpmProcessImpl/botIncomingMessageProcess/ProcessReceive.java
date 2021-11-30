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
        variablesMap.put("ChatID", chatIdVar);
        List<ProcessStartMessageCorrelationRequest> correlationRequestList = new ArrayList<>();
        try {
            Map<String, String> buisnessCorrelation = getFromAwaitingChatMap(String.valueOf(chatID));
            buisnessCorrelation.forEach((k, v) -> {
                correlationRequestList.add(ProcessStartMessageCorrelationRequest
                        .builder()
                        .businessKey(k)
                        .messageName(v)
//                        .correlationKeys(correlationKeyMap)
                        .processVariables(variablesMap)
                        .build());
            });

            correlationRequestList.forEach(processBody -> {
                try {
                    camundaClient.createCorrelation(processBody);
                } catch (Exception e) {
                    LOGGER.info("CANT CORRELATE MESSAGE FOR CHATID: " + chatID + "\nMESSAGENAME: " + processBody.getMessageName() + "\nBUISNESSKEY: " + processBody.getBusinessKey());
                }
            });
            camundaClient.addMessage(message);


        } catch (Exception e) {
            LOGGER.info("NOT FOUND AWAITING MESSAGE CHAT FOR CHATID: " + chatID);
            camundaClient.processSendMessage(chatID, outputText);

            return;
        }

        LOGGER.info("Result MessageCorrelation for chatID: " + chatID + "\n Result: пока хз, надо найти как логгировать");


//        LOGGER.info("======================");
//        LOGGER.info("MEESAGE SEND PROCESS START: \n");
//        LOGGER.info("=========");
//        LOGGER.info("VARIABELS:  ");
//        LOGGER.info("CHATID: " + chatID);
//        LOGGER.info("INPUT: " + input);
//        LOGGER.info("OUTPUT_TEXT: " + outputText);
//        LOGGER.info("=========");
//
//        try {
//            botService.sendMessage(chatID, outputText);
//        } catch (Exception e) {
//            LOGGER.info("START MESSAGE SEND PROCESS FAIL: ");
//            e.getLocalizedMessage();
//        }
//
//        LOGGER.info("END MESSAGE SEND PROCESS \n");
//        LOGGER.info("======================\n");

    }


}
