package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import com.apzakharov.telegrammBot.bpmn.service.UserProcessService;
import com.apzakharov.telegrammBot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.DataFormats;
import org.camunda.spin.Spin;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class ProcessCommand implements JavaDelegate {
    private static final Logger LOGGER = LogManager.getLogger(ProcessCommand.class);
    private static final String JSON_TYPE_STRING = "String";
    private static final String ProcessURLTemplate = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/process-definition/key";

    private final ChatBot botService;
    private final CamundaClient camundaClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
//        Long chatID = camundaProcessService.getChatID(delegateExecution);
        //TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)
        Long chatID = Spin.S(delegateExecution.getVariableTyped("ChatID").getValue())
                .mapTo("java.lang.Long");
        String input = Spin.S(delegateExecution.getVariableTyped("Input").getValue())
                .mapTo("java.lang.String");
        LOGGER.info("ProcessCommand for chatID: "+ chatID);

        ProcessStartRequestBody processBody = new ProcessStartRequestBody();
        Map<String, ProcessVariable> variablesForDelegate =  new HashMap<>();
        Set<String> variablesNameFromProcess =  delegateExecution.getVariableNames();

        variablesNameFromProcess.forEach((String variableName) ->{

            String value = Spin.S(delegateExecution
                    .getVariable(variableName))
                    .toString();

            variablesForDelegate.put(variableName,
                    new ProcessVariable(JSON_TYPE_STRING,value));

        });

        processBody.setVariables(variablesForDelegate);
        LOGGER.info("Variabls for process: "+ variablesForDelegate.toString()+"\n ProcessName: "+ input);

        String processURL = ProcessURLTemplate+input+"/start";
        LOGGER.info("ProcessURL for process: "+ processURL+"\n ProcessName: "+ input);

        camundaClient.processStart(processURL,processBody);
        String outputText = "Тестовая заглушка работы сценария ответа на работу команды";
        botService.sendMessage(chatID, outputText);//TODO: перенести отправку сообщений в ProcessMessageSend
    }

    
}
