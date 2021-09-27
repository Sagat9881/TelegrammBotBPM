package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
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


import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CamundaClient {
   private static final Logger LOGGER = LogManager.getLogger(CamundaClient.class);

   private final RestTemplate template;
//   private final ChatBot botService;


    public void processStart(String processURL, ProcessStartRequestBody processBody) {
        //get body
        Map<String,Object> requestVariables = new HashMap<>();
        requestVariables.put("variables",processBody.getVariables());
        LOGGER.info("requestVariables to string: " + requestVariables);
        SpinJsonNode request = Spin.JSON(requestVariables);
        LOGGER.info("request: " + request);
        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LOGGER.info("headers: " + headers);
        // compling Request Entity
        HttpEntity<SpinJsonNode> entity = new HttpEntity<>(request,headers);
        LOGGER.info("entity to string: " + entity);

        // send request and TODO: parse result
        ResponseEntity<ProcessStartResult> processStartResult = template.postForEntity(processURL, entity, ProcessStartResult.class);
        LOGGER.info("processStartResult status: "+processStartResult.getStatusCode()+"\n processStartResult body: " + processStartResult.getBody());
    }

//        requestVariables.accumulate("variables",processBody.getVariables());
//        Spin


//        request.put("Content-type",MediaType.APPLICATION_JSON_VALUE);
//        Map<String, Object> headers = new HashMap<>();
//        headers.put("сontent-Type","application/json");
//        request.put("headers",headers);




    public Long getChatID(DelegateExecution delegateExecution) {

        Map<String,Object> user = (Map<String,Object>)delegateExecution.getVariable("User");
        return (Long) user.get("chatID");

            }


}
