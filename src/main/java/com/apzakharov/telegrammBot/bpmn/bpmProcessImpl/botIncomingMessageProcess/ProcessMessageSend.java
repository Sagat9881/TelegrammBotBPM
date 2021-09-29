package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.Spin;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProcessMessageSend implements JavaDelegate {

    private final ChatBot botService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)
        Long chatID = Spin.JSON(delegateExecution.getVariable("ChatID")).mapTo("java.lang.Long");
        String textToSend = Spin.JSON(delegateExecution.getVariable("TextToSend")).mapTo("java.lang.String");

        botService.sendMessage(chatID,textToSend);

//       Map<String,Object> variables = delegateExecution.getVariables();

    }
}
