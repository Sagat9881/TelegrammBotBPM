package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bpmn.service.CamundaProcessService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ProcessCommand implements JavaDelegate {
    CamundaProcessService camundaProcessService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long chatID = camundaProcessService.getChatID(delegateExecution);
        camundaProcessService.sendMessage("Тестовая заглушка работы сценария ответа на работу команды",chatID);
    }
}
