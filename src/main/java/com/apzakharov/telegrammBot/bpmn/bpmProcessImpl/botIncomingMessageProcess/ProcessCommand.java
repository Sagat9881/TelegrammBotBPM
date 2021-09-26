package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bpmn.service.CamundaProcessService;
import com.apzakharov.telegrammBot.model.User;
import com.apzakharov.telegrammBot.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class ProcessCommand implements JavaDelegate {
    CamundaProcessService camundaProcessService;
    UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
//        Long chatID = camundaProcessService.getChatID(delegateExecution);
        Long chatID = (Long) delegateExecution.getVariable("ChatID");

        String outputText = "Тестовая заглушка работы сценария ответа на работу команды";
        camundaProcessService.sendMessage(outputText,chatID);
    }
}
