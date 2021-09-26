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


import java.util.Map;

@Service
@RequiredArgsConstructor
public class CamundaClient {
   private static final Logger LOGGER = LogManager.getLogger(CamundaClient.class);

   private final RestTemplate template;
//   private final ChatBot botService;


    public String processStart(String processURL, ProcessStartRequestBody processBody) {

       JSONObject request = new JSONObject(processBody);

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        request.put("headers",headers);

        LOGGER.info("request: " + request.toString());


        // send request and TODO: parse result
       String processStartResult = template.postForObject(processURL, request, String.class);
//        if (loginResponse.getStatusCode() == HttpStatus.OK) {
//            return loginResponse.getBody();
//        } else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//            // nono... bad credentials
//        }
//
//        return ;

        return (processStartResult != null) ? processStartResult :
                "fail";
    }


    public Long getChatID(DelegateExecution delegateExecution) {

        Map<String,Object> user = (Map<String,Object>)delegateExecution.getVariable("User");
        return (Long) user.get("chatID");

            }


}
