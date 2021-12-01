package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartMessageCorrelationRequest;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.Relationship;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.apzakharov.telegrammBot.bot.BotContext.putInAwaitingChatMap;

@Component
@RequiredArgsConstructor
public class ProcessMessageSend implements JavaDelegate {

    private final CamundaClient camundaClient;
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String sep = "/";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)
        Long chatID = Long.valueOf((String) delegateExecution.getVariableTyped("ChatID").getValue());

        String textToSend = (String) delegateExecution.getVariableTyped("TextToSend").getValue();
        String isNeedAnswer = (String) delegateExecution.getVariableTyped("isNeedAnswer").getValue();

        String processId = delegateExecution.getProcessInstanceId();
        String executionId = delegateExecution.getId();

        ProcessEngineServices runtimeService = delegateExecution.getProcessEngineServices();
        FlowElement bpmnFlowModelElementInstance = delegateExecution.getBpmnModelElementInstance();
        BpmnModelInstance bpmnModelElementInstance = delegateExecution.getBpmnModelInstance();

        Collection<Relationship> relationshipList = bpmnModelElementInstance.getDefinitions().getRelationships();
        String bpmnFlowModelElementInstanceName = bpmnFlowModelElementInstance.getName();
        String bpmnModelElementInstanceName = bpmnModelElementInstance.toString();
        Collection<BoundaryEvent> bpmnBoundaryEventList = bpmnModelElementInstance.getModelElementsByType(BoundaryEvent.class);



        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("bpmnFlowModelElementInstanceName : " + bpmnFlowModelElementInstanceName);
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("bpmnModelElementInstanceName : " + bpmnModelElementInstanceName);
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("relationshipList: " + relationshipList);
        System.out.println("bpmnBoundaryEventList: " + bpmnBoundaryEventList);
        System.out.println("===================================================================");
        bpmnFlowModelElementInstance.getCategoryValueRefs().forEach(categoryValue -> {
            System.out.println("bpmnFlowModelElementInstance.getCategoryValueRefs().forEach(categoryValue -> :" + categoryValue.toString());
        });
        System.out.println("===================================================================");

        System.out.println("===================================================================");
        relationshipList.forEach(relationship -> {
            System.out.println("bpmnModelElementInstance.getDefinitions().getRelationships() -> :" + relationship.toString());
        });
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        bpmnBoundaryEventList.forEach(boundaryEvent -> {
            System.out.println("bpmnBoundaryEventList.forEach(boundaryEvent -> " + boundaryEvent.toString());
        });
        System.out.println("===================================================================");

        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class) : " + bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class));
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class) : " + bpmnFlowModelElementInstance.getChildElementsByType(BoundaryEvent.class));
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        camundaClient.processSendMessage(chatID, textToSend);

        BoundaryEvent boundaryEvent =  bpmnBoundaryEventList.stream().filter((boundaryEvent1 -> {
            if (!boundaryEvent1.cancelActivity()) return true;

            return false;

        })).findFirst().orElseGet(()->null);

        if (Objects.nonNull(boundaryEvent)) {

            String businessKey =  delegateExecution.getCurrentActivityId();
            delegateExecution.setProcessBusinessKey(businessKey);

            ProcessStartMessageCorrelationRequest request = ProcessStartMessageCorrelationRequest
                    .builder()
                    .businessKey(businessKey)
                    .build();

            putInAwaitingChatMap(String.valueOf(chatID), request);
        }

        System.out.println("boundaryEvent name: " + Objects.requireNonNull(boundaryEvent).getName());
        System.out.println("boundaryEventAttachTo name: " + Objects.requireNonNull(boundaryEvent).getAttachedTo().getName());
        System.out.println("boundaryEventAttachTo name: " + Objects.requireNonNull(boundaryEvent).getAttachedTo().getName());
        System.out.println("delegateExecution.getCurrentActivityName(): " + delegateExecution.getCurrentActivityName());
        System.out.println("===================================================================");


//        if(needAnswer.equals("true")){
//            botService.putInAwaitingChatMap(String.valueOf(chatID),delegateExecution.getProcessInstance().getProcessInstanceId())
//        }
//       Map<String,Object> variables = delegateExecution.getVariables();

    }
}
