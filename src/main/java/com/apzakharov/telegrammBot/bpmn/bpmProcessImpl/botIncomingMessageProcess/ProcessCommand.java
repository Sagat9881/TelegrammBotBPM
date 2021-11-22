package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class ProcessCommand implements JavaDelegate {
    private static final Logger LOGGER = LogManager.getLogger(ProcessCommand.class);
    private static final String JSON_TYPE_STRING = "String";
    private static final String TYPE_STRING = "java.lang.String";
    private static final String TYPE_LONG = "java.lang.Long";
    private static final String ProcessURLTemplate = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/process-definition/key";

    private final CamundaClient camundaClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("ProcessCommand.execute START:  " + delegateExecution);
//        Long chatID = camundaProcessService.getChatID(delegateExecution);
        //TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)

        Long chatID = Long.valueOf((String) delegateExecution.getVariableTyped("ChatID").getValue());
        String input = (String) delegateExecution.getVariableTyped("Input").getValue();


        LOGGER.info("ProcessCommand for chatID: " + chatID + "\nCommand: " + input);

        ProcessStartRequestBody processBody = new ProcessStartRequestBody();
        Map<String, ProcessVariable> variablesForDelegate = new HashMap<>();
        Set<String> variablesNameFromProcess = delegateExecution.getVariableNames();

        variablesNameFromProcess.forEach((String variableName) -> {

            String value = (String) delegateExecution.getVariableTyped(variableName).getValue();

            variablesForDelegate.put(variableName,
                    new ProcessVariable(JSON_TYPE_STRING, value));

        });

        processBody.setVariables(variablesForDelegate);
        LOGGER.info("======================");
        LOGGER.info("PROCESS BODY: ");
        LOGGER.info("Variabls for CommandProcess: " + variablesForDelegate + "\n ProcessCommandName: " + input);

        String processURL = ProcessURLTemplate + input + "/start";
        LOGGER.info("ProcessURL for CommandProcess: " + processURL + "\n ProcessCommandName: " + input);
        try {
            LOGGER.info("START COMMAND PROCESS: \n");
            camundaClient.processStart(processURL, processBody);
        } catch (Exception e) {
            LOGGER.info("START COMMAND PROCESS FAIL: ");
            e.getLocalizedMessage();
        }
        LOGGER.info("END COMMAND PROCESS \n");
        LOGGER.info("======================\n");

//        String outputText = "Тестовая заглушка работы сценария ответа на работу команды";
//
//        LOGGER.info("======================");
//
//        LOGGER.info("MEESAGE SEND PROCESS START: \n");
//        LOGGER.info("VARIABELS:  \n" + "CHATID: " + chatID + "\nINPUT: " + input + "\nOUTPUT_TEXT: " + outputText);
//        try {
//            botService.sendMessage(chatID, outputText);
//        } catch (Exception e) {
//            LOGGER.info("START MESSAGE SEND PROCESS FAIL: ");
//            e.getLocalizedMessage();
//        }
//        LOGGER.info("END MESSAGE SEND PROCESS \n");
//        LOGGER.info("======================\n");
    }


}
