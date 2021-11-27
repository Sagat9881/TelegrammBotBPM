package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.springframework.stereotype.Component;

import static com.apzakharov.telegrammBot.bot.BotContext.putInAwaitingChatMap;

@Component
@RequiredArgsConstructor
public class ProcessMessageSend implements JavaDelegate {

    private final CamundaClient camundaClient;
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)
        Long chatID = Long.valueOf((String) delegateExecution.getVariableTyped("ChatID").getValue());

        String textToSend = (String) delegateExecution.getVariableTyped("TextToSend").getValue();
        String isNeedAnswer = (String) delegateExecution.getVariableTyped("isNeedAnswer").getValue();

        String processId = delegateExecution.getProcessInstanceId();
        String executionId = delegateExecution.getId();

        ProcessEngineServices runtimeService = delegateExecution.getProcessEngineServices();
        FlowElement bpmnModelElementInstance = delegateExecution.getBpmnModelElementInstance();

        String bpmnModelElementInstanceName = bpmnModelElementInstance.getName();
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("getBpmnModelElementInstance().getName() : "+bpmnModelElementInstanceName);
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        bpmnModelElementInstance.getCategoryValueRefs().forEach(categoryValue -> {
            System.out.println("bpmnModelElementInstance.getCategoryValueRefs().forEach(categoryValue -> "+categoryValue.toString());
        });

        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("bpmnModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class) : "+bpmnModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class));
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("bpmnModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class) : "+bpmnModelElementInstance.getChildElementsByType(BoundaryEvent.class));
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        camundaClient.processSendMessage(chatID, textToSend);

        if(isNeedAnswer.equalsIgnoreCase(TRUE)){
            putInAwaitingChatMap(processId,executionId);
        }
//        if(needAnswer.equals("true")){
//            botService.putInAwaitingChatMap(String.valueOf(chatID),delegateExecution.getProcessInstance().getProcessInstanceId())
//        }
//       Map<String,Object> variables = delegateExecution.getVariables();

    }
}
