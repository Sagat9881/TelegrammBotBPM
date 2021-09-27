package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CamundaClient {
   private static final Logger LOGGER = LogManager.getLogger(CamundaClient.class);

   private final RestTemplate template;
//   private final ChatBot botService;


    public void processStart(String processURL, ProcessStartRequestBody processBody) {

        JSONObject requestVariables = new JSONObject(processBody.getVariables());
//        request.put("Content-type",MediaType.APPLICATION_JSON_VALUE);
//        Map<String, Object> headers = new HashMap<>();
//        headers.put("сontent-Type","application/json");
//        request.put("headers",headers);

        LOGGER.info("requestVariables to string: " + requestVariables.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestVariables.toString(),headers);
        LOGGER.info("entity to string: " + entity);

        // send request and TODO: parse result
        ResponseEntity<ProcessStartResult> processStartResult = template.postForEntity(processURL, entity, ProcessStartResult.class);
        LOGGER.info("processStartResult status: "+processStartResult.getStatusCode()+"\n processStartResult body: " + processStartResult.getBody());
        }


    public Long getChatID(DelegateExecution delegateExecution) {

        Map<String,Object> user = (Map<String,Object>)delegateExecution.getVariable("User");
        return (Long) user.get("chatID");

            }


}
