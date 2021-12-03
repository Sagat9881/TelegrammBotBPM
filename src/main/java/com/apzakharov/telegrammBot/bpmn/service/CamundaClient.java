package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bot.ChatService;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartMessageCorrelationRequest;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.spin.Spin;
import org.camunda.spin.json.SpinJsonNode;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.apzakharov.telegrammBot.bot.BotContext.getFromContextChatBotMap;



@Builder
@Data
@RequiredArgsConstructor
public class CamundaClient {
    private static final Logger LOGGER = LogManager.getLogger(CamundaClient.class);

    private final RestTemplate template;
    private final String botName;

    private static final String JSON_TYPE_STRING = "String";
    private static final String ProcessURL = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/process-definition/key/process-incoming-message/start";
    private static final String MessageCorrelateURL = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/message";
    private static final String ProcessURLTemplate = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/process-definition/key";

//   private final ChatBot botService;


    public ProcessStartResult processStart(String processURL, ProcessStartRequestBody processBody) {
        //get body
        LOGGER.info("========================================================================================");
        LOGGER.info("CamundaClient.ProcessStart START : ");
        LOGGER.info(" ");
        LOGGER.info("CamundaClient.ProcessStart: processURL: "+processURL);
        LOGGER.info("CamundaClient.ProcessStart: processBody:");
        LOGGER.info(processBody);
        LOGGER.info("========================================================================================");
        ProcessStartResult processStartResult = null;
        Map<String, Object> requestVariables = new HashMap<>();

        requestVariables.put("variables", processBody.getVariables());
        LOGGER.info("========================================================================================");
        LOGGER.info("CamundaClient.ProcessStart: requestVariables to string: ");
        LOGGER.info(requestVariables);
        LOGGER.info(" ");
        SpinJsonNode request = Spin.JSON(requestVariables);

        LOGGER.info("CamundaClient.ProcessStart: SpinJsonNode request: ");
        LOGGER.info(request);
        LOGGER.info(" ");
        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LOGGER.info("CamundaClient.ProcessStart: headers: ");
        LOGGER.info(headers);
        LOGGER.info(" ");
        // compling Request Entity
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        LOGGER.info("CamundaClient.ProcessStart: HttpEntity<String> entity to string: ");
        LOGGER.info(entity.toString());
        LOGGER.info(" ");
        LOGGER.info("CamundaClient.ProcessStart: HttpEntity<String> entity: ");
        LOGGER.info(entity);
        LOGGER.info("========================================================================================");

        // send request and TODO: parse result
        try {
           ResponseEntity<ProcessStartResult> processStartResultResponseEntity= template.postForEntity(processURL, entity, ProcessStartResult.class);
            LOGGER.info("CamundaClient.ProcessStart: getStatusCode(): " + processStartResultResponseEntity.getStatusCode());
            LOGGER.info("CamundaClient.ProcessStart: getBody(): ");
            LOGGER.info(processStartResultResponseEntity.getBody());
            LOGGER.info("========================================================================================");

            processStartResult = processStartResultResponseEntity.getBody();
        } catch (Exception e){
            LOGGER.info("CamundaClient.ProcessStart FAIL: "+e.getClass().getSimpleName());
            LOGGER.info("========================================================================================");
        }
        return processStartResult;
    }

    public ProcessStartResult processStart (Chat chat,User user, String input) throws Exception {
        LOGGER.info("========================================================================================");
        LOGGER.info("CamundaClient.ProcessStart START : ");
        LOGGER.info(" ");
        LOGGER.info("CamundaClient.ProcessStart: input: "+input);
        LOGGER.info("CamundaClient.ProcessStart: user:");
        LOGGER.info(user);
        LOGGER.info(" ");
        LOGGER.info("CamundaClient.ProcessStart: chat:");
        LOGGER.info(chat);
        LOGGER.info(" ");
        LOGGER.info("========================================================================================");
        ProcessStartResult result = null;
        Long chatID = chat.getChatId();

        try {
            Map<String, ProcessVariable> variables = new HashMap<>();

            variables.put("User",
                    new ProcessVariable(user.toString(), JSON_TYPE_STRING));
            variables.put("ChatID",
                    ProcessVariable.builder()
                            .value(chatID.toString())
                            .type( JSON_TYPE_STRING).build());

            variables.put("Input",
                    ProcessVariable.builder()
                            .value(input)
                            .type( JSON_TYPE_STRING).build());


            ProcessStartRequestBody processBody = new ProcessStartRequestBody();
            processBody.setVariables(variables);

            result = processStart(ProcessURL, processBody);

            LOGGER.info("CamundaClient.ProcessStart: processStart(ProcessURL, processBody): ");
            LOGGER.info("CamundaClient.ProcessStart: processStart(ProcessURL, processBody): ProcessURL: ");
            LOGGER.info(ProcessURL);
            LOGGER.info(" ");
            LOGGER.info("CamundaClient.ProcessStart: processStart(ProcessURL, processBody): processBody: ");
            LOGGER.info(processBody);
            LOGGER.info("========================================================================================");
        } catch (Exception ex) {
            LOGGER.info("CamundaClient.ProcessStart: PROCESS FAIL: "+ ex.getClass().getSimpleName());
        }

        return result;

//        requestVariables.accumulate("variables",processBody.getVariables());
//        Spin


//        request.put("Content-type",MediaType.APPLICATION_JSON_VALUE);
//        Map<String, Object> headers = new HashMap<>();
//        headers.put("сontent-Type","application/json");
//        request.put("headers",headers);


    }

    public void processSendMessage(Long chatID, String textToSend) throws Exception{
        ChatBot chatBot = getFromContextChatBotMap(botName);
        chatBot.sendMessage(chatID,textToSend);
    }

    public Chat findChatByChatId(Long chatID) throws Exception{
        ChatBot chatBot = getFromContextChatBotMap(botName);
        return chatBot.findByChatId(chatID);
    }

    public void addMessage(Message message) throws Exception{
        ChatBot chatBot = getFromContextChatBotMap(botName);
        chatBot.addMessage(message);
    }

    public ProcessStartResult createCorrelation(ProcessStartMessageCorrelationRequest processBody) throws Exception{

        //get body
        LOGGER.info("========================================================================================");
        LOGGER.info("CamundaClient.createCorrelation : ");
        LOGGER.info(" ");
        LOGGER.info("CamundaClient.createCorrelation :  MessageCorrelateURL:");
        LOGGER.info(MessageCorrelateURL);
        LOGGER.info("CamundaClient.createCorrelation : processBody: ");
        LOGGER.info(processBody);
        LOGGER.info(" ");

        ProcessStartResult processStartResult = null;
        SpinJsonNode request = Spin.JSON(processBody);
        LOGGER.info("CamundaClient.createCorrelation: SpinJsonNode request: ");
        LOGGER.info(request);
        LOGGER.info(" ");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LOGGER.info("CamundaClient.createCorrelation: headers: ");
        LOGGER.info(headers);
        LOGGER.info(" ");


        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        LOGGER.info("CamundaClient.createCorrelation: HttpEntity<String> entity to string: ");
        LOGGER.info(entity.toString());
        LOGGER.info(" ");
        LOGGER.info("CamundaClient.createCorrelation: HttpEntity<String> entity: ");
        LOGGER.info(entity);
        LOGGER.info("========================================================================================");
        try {

            ResponseEntity<ProcessStartResult>  createCorrelationResultResponseEntity= template.postForEntity(MessageCorrelateURL, entity, ProcessStartResult.class);
            LOGGER.info("CamundaClient.createCorrelation: getStatusCode(): " + createCorrelationResultResponseEntity.getStatusCode());
            LOGGER.info("CamundaClient.createCorrelation: getBody(): ");
            LOGGER.info(createCorrelationResultResponseEntity.getBody());
            LOGGER.info("========================================================================================");

            processStartResult = createCorrelationResultResponseEntity.getBody();
        } catch (Exception e){
            LOGGER.info("CamundaClient.createCorrelation FAIL: "+e.getClass().getSimpleName());
            LOGGER.info("========================================================================================");
        }

        return processStartResult;
    }

    public ProcessStartResult startCommand(Long chatID, String input) throws Exception{
        ProcessStartResult processStartResult = null;
        LOGGER.info("========================================================================================");
        LOGGER.info("CamundaClient.startCommand :  chatID: " + chatID + " command: " + input);
        LOGGER.info("========================================================================================");

        ProcessStartRequestBody processBody = new ProcessStartRequestBody();
        Map<String, ProcessVariable> variablesForDelegate = new HashMap<>();

        variablesForDelegate.put("chatID",ProcessVariable
                .builder()
                .type(JSON_TYPE_STRING)
                .value(String.valueOf(chatID))
                .build());
        variablesForDelegate.put("Input",ProcessVariable
                .builder()
                .type(JSON_TYPE_STRING)
                .value(String.valueOf(input))
                .build());

        processBody.setVariables(variablesForDelegate);
        LOGGER.info("========================================================================================");
        LOGGER.info("CamundaClient.startCommand : variablesForDelegate: ");
        LOGGER.info(variablesForDelegate);
        LOGGER.info(" ");
        LOGGER.info("CamundaClient.startCommand : processBody: ");
        LOGGER.info(processBody);
        LOGGER.info("========================================================================================");

        String processURL = ProcessURLTemplate + input + "/start";

        try {
            LOGGER.info("========================================================================================");
            LOGGER.info("CamundaClient.startCommand : processURL: +" + processURL);
            processStartResult= processStart(processURL, processBody);
        } catch (Exception e) {
            LOGGER.info("CamundaClient.startCommand : PROCESS FAIL: " + e.getClass().getSimpleName());
        }
        LOGGER.info("========================================================================================");

        return processStartResult;
    }

//    public void findByChat_id(Long chatID) throws Exception{
//        botService.findByChat_id(chatID);
//    }


}
