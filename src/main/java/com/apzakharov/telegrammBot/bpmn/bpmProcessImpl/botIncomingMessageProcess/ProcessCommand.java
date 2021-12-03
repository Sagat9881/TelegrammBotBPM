//package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;
//
//import com.apzakharov.telegrammBot.bot.ChatBot;
//import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
//import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
//import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
//import lombok.RequiredArgsConstructor;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.camunda.bpm.engine.delegate.DelegateExecution;
//import org.camunda.bpm.engine.delegate.JavaDelegate;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//
//@Component
//@RequiredArgsConstructor
//public class ProcessCommand implements JavaDelegate {
//    private static final Logger LOGGER = LogManager.getLogger(ProcessCommand.class);
//    private static final String JSON_TYPE_STRING = "String";
//    private static final String TYPE_STRING = "java.lang.String";
//    private static final String TYPE_LONG = "java.lang.Long";
//
//
//    private final CamundaClient camundaClient;
//
//    @Override
//    public void execute(DelegateExecution delegateExecution) throws Exception {
//        LOGGER.info("========================================================================================");
//        LOGGER.info("ProcessCommand.execute :  CurrentActivityName: " + delegateExecution.getCurrentActivityName());
//
//
//        Long chatID = Long.valueOf((String) delegateExecution.getVariableTyped("ChatID").getValue());
//        String input = (String) delegateExecution.getVariableTyped("Input").getValue();
//
//        LOGGER.info("========================================================================================");
//        LOGGER.info("ProcessCommand.execute :  chatID: " + chatID + " command: " + input);
//        LOGGER.info("========================================================================================");
//
//        ProcessStartRequestBody processBody = new ProcessStartRequestBody();
//        Map<String, ProcessVariable> variablesForDelegate = new HashMap<>();
//        Set<String> variablesNameFromProcess = delegateExecution.getVariableNames();
//
//        variablesNameFromProcess.forEach((String variableName) -> {
//
//            String value = (String) delegateExecution.getVariableTyped(variableName).getValue();
//
//            variablesForDelegate.put(variableName,ProcessVariable
//                    .builder()
//                    .type(JSON_TYPE_STRING)
//                    .value(value)
//                    .build());
//        });
//
//        processBody.setVariables(variablesForDelegate);
//        LOGGER.info("========================================================================================");
//        LOGGER.info("ProcessCommand.execute : variablesForDelegate: ");
//        LOGGER.info(variablesForDelegate);
//        LOGGER.info(" ");
//        LOGGER.info("ProcessCommand.execute : processBody: ");
//        LOGGER.info(processBody);
//        LOGGER.info("========================================================================================");
//
//        String processURL = null;
//
//        try {
//            LOGGER.info("========================================================================================");
//            LOGGER.info("ProcessCommand.execute : processURL: +" + processURL);
//
//            camundaClient.processStart(processURL, processBody);
//        } catch (Exception e) {
//            LOGGER.info("ProcessCommand.execute : PROCESS FAIL: " + e.getClass().getSimpleName());
//            LOGGER.info("========================================================================================");
//        }
//
//    }
//
//
//}
