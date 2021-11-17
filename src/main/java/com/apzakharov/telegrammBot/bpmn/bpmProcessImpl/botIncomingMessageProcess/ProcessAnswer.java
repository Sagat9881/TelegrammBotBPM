package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.service.UserProcessService;
import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.service.MessageService;
import com.apzakharov.telegrammBot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import spinjar.com.fasterxml.jackson.core.JsonParseException;
import spinjar.com.fasterxml.jackson.databind.JsonMappingException;


import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.spin.Spin;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class ProcessAnswer implements JavaDelegate {

    private static final Logger LOGGER = LogManager.getLogger(ProcessAnswer.class);
    private final ChatBot botService;
    private final UserService userService;
    private final MessageService messageService;

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
            userId = userService.findByChat_id(chatID).getId();
        } catch (Exception e) {
            LOGGER.info("NOT FOUND USER FOR CHATID: " + chatID);
            e.getLocalizedMessage();
        }

        Message message = Message.builder()
                .chatId(chatID)
                .userId(userId)
                .text(outputText)
                .build();

        messageService.addMessage(message);
        try {
            ProcessEngine engine = delegateExecution.getProcessEngine();
            RuntimeService runtimeService = engine.getRuntimeService();
            LOGGER.info("Start MessageCorrelation for chatID: " + chatID + "\n Input text: \n" + input);
//            MessageCorrelationResult reciveResult = runtimeService
//                    .createMessageCorrelation("NewIncomingMessage")
//                    .localVariableEquals("ChatID", String.valueOf(chatID))
//                    .setVariableLocal("Input", input)
//                    .correlateWithResult();

            delegateExecution.getProcessEngineServices()
                    .getRuntimeService()
                    .createMessageCorrelation("NewIncomingMessage")
                    .processInstanceVariableEquals("ChatID",String.valueOf(chatID))
                    .setVariableLocal("Input", input)
                    .correlate();

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
