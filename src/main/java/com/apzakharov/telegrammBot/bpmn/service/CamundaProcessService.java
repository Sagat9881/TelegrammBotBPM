package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bot.BotService;
import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess.ProcessCommand;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import com.apzakharov.telegrammBot.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CamundaProcessService{
    private static final Logger LOGGER = LogManager.getLogger(CamundaProcessService.class);

   private final RestTemplate template;
   private final ChatBot botService;


    public ProcessStartResult processStart(String processURL, ProcessStartRequestBody processBody) {

        ProcessStartResult processStartResult = template.postForObject(processURL, processBody, ProcessStartResult.class);


        return (processStartResult != null) ? processStartResult :
                new ProcessStartResult();
    }

    public void sendMessage (String text, Long chatId){
        LOGGER.info("Start send message with text: "+text+", and chatID: "+chatId);
        botService.sendMessage(chatId,text);

    }


    public Long getChatID(DelegateExecution delegateExecution) {

        Map<String,Object> user = (Map<String,Object>)delegateExecution.getVariable("User");
        return (Long) user.get("chatID");

            }


}
