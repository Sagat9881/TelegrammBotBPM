package com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess;

import com.apzakharov.telegrammBot.bot.ChatBot;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartMessageCorrelationRequest;
import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = LogManager.getLogger(ProcessMessageSend.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("========================================================================================");
        LOGGER.info("ProcessMessageSend.execute :  CurrentActivityName: " + delegateExecution.getCurrentActivityName());

        //TODO: унинфицоравть получение переменных по DRY (запилить параметризированный метод)
        Long chatID = Long.valueOf((String) delegateExecution.getVariableTyped("chatID").getValue());

        String textToSend = (String) delegateExecution.getVariableTyped("textToSend").getValue();
        LOGGER.info("ProcessMessageSend.execute :  textToSend: " + textToSend);
        LOGGER.info("ProcessMessageSend.execute :  chatID: " + chatID);
        LOGGER.info("========================================================================================");
        FlowElement bpmnFlowModelElementInstance = delegateExecution.getBpmnModelElementInstance();
        BpmnModelInstance bpmnModelElementInstance = delegateExecution.getBpmnModelInstance();

        Collection<Relationship> relationshipList = bpmnModelElementInstance.getDefinitions().getRelationships();
        String bpmnFlowModelElementInstanceName = bpmnFlowModelElementInstance.getName();
        String bpmnModelElementInstanceName = bpmnModelElementInstance.toString();
        Collection<BoundaryEvent> bpmnBoundaryEventList = bpmnModelElementInstance.getModelElementsByType(BoundaryEvent.class);

        LOGGER.info("===================================================================");
        LOGGER.info("bpmnFlowModelElementInstanceName : " + bpmnFlowModelElementInstanceName);
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info("bpmnModelElementInstanceName : " + bpmnModelElementInstanceName);
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info("relationshipList: " + relationshipList);
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info("bpmnBoundaryEventList: " + bpmnBoundaryEventList);
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info("===================================================================");
        LOGGER.info("relationshipList: ");
        LOGGER.info("bpmnModelElementInstance.getDefinitions().getRelationships() ->");
        relationshipList.forEach(relationship -> {
            LOGGER.info("   relationship.getType() :" + relationship.getType());
            LOGGER.info("   relationship.getSources() :" + relationship.getSources());
            LOGGER.info("   relationship.getTargets() :" + relationship.getTargets());
            LOGGER.info("   relationship.getElementType().getTypeName() :" + relationship.getElementType().getTypeName());
        });
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info(" ");
        LOGGER.info("bpmnBoundaryEventList: ");
        LOGGER.info(bpmnBoundaryEventList);
        LOGGER.info(" ");
        LOGGER.info("bpmnBoundaryEventList.forEach(boundaryEvent -> " );
        bpmnBoundaryEventList.forEach(boundaryEvent -> {
            LOGGER.info("   boundaryEvent.getAttachedTo(): "+boundaryEvent.getAttachedTo());
            LOGGER.info("   boundaryEvent.getName(): "+boundaryEvent.getName());
            LOGGER.info("   boundaryEvent.getId() "+boundaryEvent.getId());
            LOGGER.info(" ");
        });
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info(" ");
        LOGGER.info("bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class) : " );
        LOGGER.info(bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class));
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info("bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class) : " );
        LOGGER.info(bpmnFlowModelElementInstance.getChildElementsByType(BoundaryEvent.class));
        LOGGER.info(" ");

        LOGGER.info("===================================================================");
        camundaClient.processSendMessage(chatID, textToSend);
        LOGGER.info("bpmnBoundaryEventList.stream().filter((boundaryEvent1 -> ");
        BoundaryEvent boundaryEvent =  bpmnBoundaryEventList.stream().filter((boundaryEventItem -> {
            LOGGER.info("   boundaryEventItem.cancelActivity() : "+boundaryEventItem.cancelActivity());
            boolean isValidMessageEvent = boundaryEventItem
                    .getAttachedTo()
                    .getName()
                    .equals(delegateExecution.getCurrentActivityName())||boundaryEventItem.cancelActivity();

            LOGGER.info("   boundaryEventItem.getName() : "+boundaryEventItem.getName());
            LOGGER.info("   boundaryEventItem.getAttachedTo().getName() : "+boundaryEventItem.getName());
            LOGGER.info("   isValidMessageEvent : "+isValidMessageEvent);

            if (isValidMessageEvent) return true;

            else return false;

        })).findFirst().orElseGet(()->null);

        LOGGER.info(" BoundaryEvent boundaryEvent.getName() : "+boundaryEvent.getName());
        LOGGER.info(" BoundaryEvent boundaryEvent.getId() : "+boundaryEvent.getId());

        if (Objects.nonNull(boundaryEvent)) {

            String businessKey =  delegateExecution.getCurrentActivityId();
            delegateExecution.setProcessBusinessKey(businessKey);

            ProcessStartMessageCorrelationRequest request = ProcessStartMessageCorrelationRequest
                    .builder()
//                    .messageName(boundaryEvent.getName())
                    .businessKey(businessKey)
                    .build();
            LOGGER.info("ProcessStartMessageCorrelationRequest request: ");
            LOGGER.info(request);
            putInAwaitingChatMap(String.valueOf(chatID), request);
        }

        LOGGER.info("===================================================================");
    }
}
