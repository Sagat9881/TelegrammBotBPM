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

//        Long chatID = camundaProcessService.getChatID(delegateExecution);

//        RuntimeService reciveResult = ProcessEngines.
        //TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)
        Long chatID = Spin.S(delegateExecution.getVariableLocalTyped("ChatID",true))
                .mapTo("java.lang.Long");
        String input = Spin.S(delegateExecution.getVariableLocalTyped("Input",true))
                .mapTo("java.lang.String");

        ProcessEngine engine = delegateExecution.getProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        LOGGER.info("Start MessageCorrelation for chatID: " + chatID + "\n Input text: \n" + input);
        MessageCorrelationResult reciveResult = runtimeService
                .createMessageCorrelation("new_incoming_message")
                .localVariableEquals("ChatID", chatID)
                .setVariableLocal("Input", input)
                .correlateWithResult();
        LOGGER.info("Result MessageCorrelation for chatID: " + chatID + "\n Result: пока хз, надо найти как логгировать");


        String outputText = chatID.equals(966663803L) ?
                "Люблю тебя :3" : "Тестовая заглушка работы сценария обработки ответа на сообщение (не на команду)";
        botService.sendMessage(chatID, outputText); //TODO: перенести отправку сообщений в ProcessMessageSend

    }


}
