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
        Long chatID = Spin.S(delegateExecution.getVariableTyped("ChatID").getValue())
                .mapTo("java.lang.Long");

        String textToSend = Spin.S(delegateExecution.getVariableTyped("TextToSend").getValue())
                .mapTo("java.lang.String");
        botService.sendMessage(chatID,textToSend);

//       Map<String,Object> variables = delegateExecution.getVariables();

    }
}
