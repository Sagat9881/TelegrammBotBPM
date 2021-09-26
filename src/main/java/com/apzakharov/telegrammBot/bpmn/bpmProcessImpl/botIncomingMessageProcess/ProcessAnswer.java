package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bpmn.service.CamundaProcessService;
import com.apzakharov.telegrammBot.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

@Component
public class ProcessAnswer implements JavaDelegate {

    CamundaProcessService camundaProcessService;
    ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

//        Long chatID = camundaProcessService.getChatID(delegateExecution);

        Long chatID = (Long) delegateExecution.getVariable("ChatID");


        String outputText = "Тестовая заглушка работы сценария обработки ответа на сообщение (не на команду)";
        camundaProcessService.sendMessage(outputText, chatID);

    }


}
