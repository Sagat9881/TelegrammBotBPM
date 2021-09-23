package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bpmn.service.CamundaProcessService;
import com.apzakharov.telegrammBot.model.User;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ProcessAnswer implements JavaDelegate {

    CamundaProcessService camundaProcessService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        User user = (User) delegateExecution.getVariable("User");
        Long chatID = user.getChatId();

        String inputText = "Тестовая заглушка работы сценария обработки ответа на сообщение (не на команду)";
        camundaProcessService.sendMessage(inputText, chatID);

    }


}
