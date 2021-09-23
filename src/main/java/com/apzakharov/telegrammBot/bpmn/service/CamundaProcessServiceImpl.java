package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bot.BotService;
import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import com.apzakharov.telegrammBot.model.User;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CamundaProcessServiceImpl implements CamundaProcessService {
    RestTemplate template;
    ChatBot botService;

    @Override
    public ProcessStartResult processStart(String processURL, ProcessStartRequestBody processBody) {

        ProcessStartResult processStartResult = template.postForObject(processURL, processBody, ProcessStartResult.class);


        return (processStartResult != null) ? processStartResult :
                new ProcessStartResult();
    }

    public void sendMessage (String text, Long chatId){
        botService.sendMessage(chatId,text);

    }

    @Override
    public Long getChatID(DelegateExecution delegateExecution) {

        Map<String,Object> user = (Map<String,Object>)delegateExecution.getVariable("User");
        return (Long) user.get("chatID");


            }


}
