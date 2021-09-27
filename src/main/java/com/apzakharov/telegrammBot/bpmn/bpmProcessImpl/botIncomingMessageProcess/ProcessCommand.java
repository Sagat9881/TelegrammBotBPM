package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.service.UserProcessService;
import com.apzakharov.telegrammBot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.Spin;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ProcessCommand implements JavaDelegate {
    private static final Logger LOGGER = LogManager.getLogger(ProcessCommand.class);

    private final ChatBot botService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
//        Long chatID = camundaProcessService.getChatID(delegateExecution);
        Long chatID = Spin.JSON(delegateExecution.getVariable("ChatID")).mapTo("java.lang.Long");
        LOGGER.info("ProcessCommand for chatID: "+ chatID);

        String outputText = "Тестовая заглушка работы сценария ответа на работу команды";
        botService.sendMessage(chatID, outputText);
    }
}
