package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CamundaClient {
   private static final Logger LOGGER = LogManager.getLogger(CamundaClient.class);

   private final RestTemplate template;
//   private final ChatBot botService;


    public ProcessStartResult processStart(String processURL, ProcessStartRequestBody processBody) {

        ProcessStartResult processStartResult = template.postForObject(processURL, processBody, ProcessStartResult.class);

        return (processStartResult != null) ? processStartResult :
                new ProcessStartResult();
    }


    public Long getChatID(DelegateExecution delegateExecution) {

        Map<String,Object> user = (Map<String,Object>)delegateExecution.getVariable("User");
        return (Long) user.get("chatID");

            }


}
