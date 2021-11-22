package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import com.apzakharov.telegrammBot.model.Message;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.ProcessEngineService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.apzakharov.telegrammBot.bot.BotContext.getFromAwaitingChatMap;


@Component
@RequiredArgsConstructor
public class ProcessAnswer implements JavaDelegate {

    private static final Logger LOGGER = LogManager.getLogger(ProcessAnswer.class);
    private final CamundaClient camundaClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("ProcessAnswer.execute START:  " + delegateExecution);

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

        camundaClient.addMessage(message);
        try {
            ProcessEngineServices engineService = delegateExecution.getProcessEngineServices();
            RuntimeService runtimeService = engineService.getRuntimeService();
            String executionId = getFromAwaitingChatMap(delegateExecution.getProcessInstanceId());

            Map<String,Object> correlationKeys =new HashMap<>();
            correlationKeys.put("ChatID",chatID);
            correlationKeys.put("Input",input);

            LOGGER.info("Start MessageCorrelation for chatID: " + chatID + "\n Input text: \n" + input);

            runtimeService
                    .messageEventReceived("NewIncomingMessage",executionId,correlationKeys);


        } catch (Exception e) {
            e.getLocalizedMessage();
            LOGGER.info("NOT FOUND AWAITING MESSAGE CHAT FOR CHATID: " + chatID);
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
