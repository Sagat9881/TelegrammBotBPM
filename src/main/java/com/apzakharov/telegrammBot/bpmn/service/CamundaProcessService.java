package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import org.camunda.bpm.engine.delegate.DelegateExecution;

public interface CamundaProcessService {
    ProcessStartResult processStart(String processURL, ProcessStartRequestBody processBody);

    void sendMessage(String text, Long chatId);

    Long getChatID (DelegateExecution delegateExecution);
}
