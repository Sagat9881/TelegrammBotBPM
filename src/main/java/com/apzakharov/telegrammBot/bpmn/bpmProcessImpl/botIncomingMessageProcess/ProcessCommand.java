package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.service.CamundaProcessService;
import com.apzakharov.telegrammBot.model.User;
import com.apzakharov.telegrammBot.service.UserService;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class ProcessCommand implements JavaDelegate {
    private static final Logger LOGGER = LogManager.getLogger(ProcessCommand.class);
    CamundaProcessService camundaProcessService;
    UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
//        Long chatID = camundaProcessService.getChatID(delegateExecution);
        Long chatID = (Long) delegateExecution.getVariable("ChatID");
        LOGGER.info("ProcessCommand for chatID: "+ chatID);

        String outputText = "Тестовая заглушка работы сценария ответа на работу команды";
        camundaProcessService.sendMessage(outputText,chatID);
    }
}
