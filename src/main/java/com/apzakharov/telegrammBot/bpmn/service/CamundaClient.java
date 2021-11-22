package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bot.ChatService;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.model.User;
import lombok.Builder;
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

import static com.apzakharov.telegrammBot.bot.BotContext.getFromContextChatBotMap;



@Builder
@RequiredArgsConstructor
public class CamundaClient {
    private static final Logger LOGGER = LogManager.getLogger(CamundaClient.class);

    private final RestTemplate template;
    private final BotContext botContext;
    private final String botName;

    private static final String JSON_TYPE_STRING = "String";
    private static final String ProcessURL = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/process-definition/key/process-incoming-message/start";

//   private final ChatBot botService;


    public void processStart(String processURL, ProcessStartRequestBody processBody) {
        //get body
        LOGGER.info("======================");
        LOGGER.info("CamundaClient.ProcessStart START : ");
        LOGGER.info(" ");
        LOGGER.info("\n"+"PROCESS BODY"+processBody+"\nPROCESS URL: "+processURL);
        LOGGER.info(" ");

        Map<String, Object> requestVariables = new HashMap<>();
        requestVariables.put("variables", processBody.getVariables());
        LOGGER.info("requestVariables to string: " + requestVariables);
        SpinJsonNode request = Spin.JSON(requestVariables);
        LOGGER.info("request: " + request);
        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LOGGER.info("headers: " + headers);
        // compling Request Entity
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        LOGGER.info("entity to string: " + entity);

        // send request and TODO: parse result
        try {
            ResponseEntity<ProcessStartResult> processStartResult = template.postForEntity(processURL, entity, ProcessStartResult.class);
            LOGGER.info("processStartResult status: " + processStartResult.getStatusCode() + "\n processStartResult body: " + processStartResult.getBody());
            LOGGER.info("======================");
        } catch (Exception e){
            LOGGER.info("CamundaClient.ProcessStart FAIL: ");
            e.getLocalizedMessage();
            LOGGER.info("======================");
        }
    }

    public void processStart (Chat chat,User user, String input) throws Exception {

        LOGGER.info("Start New UserProcessAnswer for \nChat: \n" + chat+"\nUser: \n"+user+"\n Input: \n"+input);

        Long chatID = chat.getChatId();

        try {
            Map<String, ProcessVariable> variables = new HashMap<>();

            variables.put("User",
                    new ProcessVariable(JSON_TYPE_STRING, user.toString()));
            variables.put("ChatID",
                    new ProcessVariable(JSON_TYPE_STRING, chatID.toString()));
            variables.put("Input",
                    new ProcessVariable(JSON_TYPE_STRING, input));

            LOGGER.info("Variabls for process: " + variables.toString());

            ProcessStartRequestBody processBody = new ProcessStartRequestBody();
            processBody.setVariables(variables);

            LOGGER.info("StartProcess for Url: \n" + ProcessURL + "\n" + " and body: \n" + processBody.toString());

            processStart(ProcessURL, processBody);

//            user.setStateId(BotStateBPMN.PROCESS.getBotStateBPMNID());
        } catch (Exception ex) {
            ex.getLocalizedMessage();
//            user.setStateId(BotStateBPMN.ERROR.getBotStateBPMNID());
        }

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
//    public void findByChat_id(Long chatID) throws Exception{
//        botService.findByChat_id(chatID);
//    }


}
