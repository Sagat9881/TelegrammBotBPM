package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
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

        JSONObject request = new JSONObject(processBody);
        Map<String, Object> headers = new HashMap<>();
        headers.put("сontent-Type","application/json");
        request.put("headers",headers);
        LOGGER.info("request: " + request.toString());

        HttpEntity<String> entity = new HttpEntity<String>(request.toString());
        LOGGER.info("entity: " + entity.toString());

        // send request and TODO: parse result
        ResponseEntity<String> processStartResult = template.postForEntity(processURL, request.toMap(), String.class);

        }


    public Long getChatID(DelegateExecution delegateExecution) {

        Map<String,Object> user = (Map<String,Object>)delegateExecution.getVariable("User");
        return (Long) user.get("chatID");

            }


}
