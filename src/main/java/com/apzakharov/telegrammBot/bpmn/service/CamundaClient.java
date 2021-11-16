package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bot.BotStateBPMN;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
import com.apzakharov.telegrammBot.model.User;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.spin.Spin;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.impl.SpinBpmPlatformPlugin;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CamundaClient {
    private static final Logger LOGGER = LogManager.getLogger(CamundaClient.class);

    private final RestTemplate template;

    private static final String JSON_TYPE_STRING = "String";
    private static final String ProcessURL = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/process-definition/key/process-incoming-message/start";

//   private final ChatBot botService;


    public void processStart(String processURL, ProcessStartRequestBody processBody) {
        //get body
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
        ResponseEntity<ProcessStartResult> processStartResult = template.postForEntity(processURL, entity, ProcessStartResult.class);
        LOGGER.info("processStartResult status: " + processStartResult.getStatusCode() + "\n processStartResult body: " + processStartResult.getBody());
    }

    public void processStart(@Nonnull BotContext contex) throws Exception {

        LOGGER.info("Start New UserProcessAnswer for contex: \n" + contex.toString());

        User user = contex.getUser();
        Long chatID = user.getChat_id();
        String input = contex.getInput();

        LOGGER.info("Context for process: \n" + contex.toString());

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
            ex.printStackTrace();
//            user.setStateId(BotStateBPMN.ERROR.getBotStateBPMNID());
        }

//        requestVariables.accumulate("variables",processBody.getVariables());
//        Spin


//        request.put("Content-type",MediaType.APPLICATION_JSON_VALUE);
//        Map<String, Object> headers = new HashMap<>();
//        headers.put("сontent-Type","application/json");
//        request.put("headers",headers);


    }
}
