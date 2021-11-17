package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.service.UserProcessService;
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

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("ProcessAnswer.execute START:  " + delegateExecution);
//        Long chatID = camundaProcessService.getChatID(delegateExecution);

//        RuntimeService reciveResult = ProcessEngines.
//      TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)

        Object chatIdFromProcess  = delegateExecution.getVariableTyped("ChatID");
        Object inputFromProcess   = delegateExecution.getVariableTyped("Input");

        String chatIdFromProcessType = delegateExecution.getVariableTyped("ChatID").getType().getName();
        String inputFromProcessType = delegateExecution.getVariableTyped("ChatID").getType().getName();

        Long chatID = Spin.S(chatIdFromProcess,chatIdFromProcessType)
                .mapTo("java.lang.Long");

        String input = Spin.S(inputFromProcess,inputFromProcessType)
                .mapTo("java.lang.String");

        String outputText = chatID.equals(966663803L) ?
                "Люблю тебя :3" : "Тестовая заглушка работы сценария обработки ответа на сообщение (не на команду)";

        LOGGER.info("======================");

        LOGGER.info("MEESAGE SEND PROCESS START: \n");
        LOGGER.info("VARIABELS:  \n" + "CHATID: " + chatID + "\nINPUT: " + input + "\nOUTPUT_TEXT: " + outputText);
        try {
            botService.sendMessage(chatID, outputText);
        } catch (Exception e) {
            LOGGER.info("START MESSAGE SEND PROCESS FAIL: ");
            e.getLocalizedMessage();
        }

        LOGGER.info("END MESSAGE SEND PROCESS \n");
        LOGGER.info("======================\n");

    }


}
