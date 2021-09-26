package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.service.UserProcessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ProcessAnswer implements JavaDelegate {

    private final ChatBot botService;
    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

//        Long chatID = camundaProcessService.getChatID(delegateExecution);

        Long chatID = (Long) delegateExecution.getVariable("ChatID");


        String outputText = "Тестовая заглушка работы сценария обработки ответа на сообщение (не на команду)";
        botService.sendMessage(chatID, outputText);

    }


}
