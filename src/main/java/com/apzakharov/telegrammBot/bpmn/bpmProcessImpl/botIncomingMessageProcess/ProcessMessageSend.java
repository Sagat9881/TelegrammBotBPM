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
import org.camunda.bpm.model.bpmn.impl.instance.BoundaryEventImpl;
import org.camunda.bpm.model.bpmn.impl.instance.IntermediateCatchEventImpl;
import org.camunda.bpm.model.bpmn.instance.*;
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
    private static final String MESSAGE_INTERMEDIATE_CATCH_EVENT_TYPE_NAME = "false";

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
        Collection<IntermediateCatchEvent> bpmnIntermediateCatchEventList = bpmnModelElementInstance.getModelElementsByType(IntermediateCatchEvent.class);

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
        LOGGER.info("===================================================================");
        LOGGER.info("bpmnIntermediateCatchEventList: ");
        LOGGER.info("bpmnModelElementInstance.getModelElementsByType(IntermediateCatchEvent.class); ->");
        LOGGER.info("items: ");
        bpmnIntermediateCatchEventList.forEach(catchEvent -> {
            LOGGER.info(" item: ");
            LOGGER.info("   catchEvent.getName() :" + catchEvent.getName());
            LOGGER.info("   catchEvent.getPreviousNodes().singleResult().getName()" + catchEvent.getPreviousNodes().singleResult().getName());
            LOGGER.info("   catchEvent.getIncoming().forEach(sequenceFlow -> : ");
            LOGGER.info("   items: ");
            catchEvent.getIncoming().forEach(sequenceFlow -> {
                LOGGER.info("     item: ");
                LOGGER.info("       sequenceFlow.getName() :" + sequenceFlow.getName());
                LOGGER.info("       sequenceFlow.getSource() :" + sequenceFlow.getSource());
                LOGGER.info("       sequenceFlow.getTarget() :" + sequenceFlow.getTarget());
                LOGGER.info("       sequenceFlow.getElementType().getTypeName() :" + sequenceFlow.getElementType().getTypeName());
                LOGGER.info("---------------------------------------------------- ");

            });
            LOGGER.info("   catchEvent.getOutgoing().forEach(sequenceFlow -> : ");
            LOGGER.info("   items: ");
            catchEvent.getOutgoing().forEach(sequenceFlow -> {
                LOGGER.info("     item: ");
                LOGGER.info("       sequenceFlow.getName() :" + sequenceFlow.getName());
                LOGGER.info("       sequenceFlow.getSource() :" + sequenceFlow.getSource());
                LOGGER.info("       sequenceFlow.getTarget() :" + sequenceFlow.getTarget());
                LOGGER.info("       sequenceFlow.getElementType().getTypeName() :" + sequenceFlow.getElementType().getTypeName());
                LOGGER.info("---------------------------------------------------- ");

            });
            LOGGER.info("---------------------------------------------------- ");
        });
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info(" ");
        LOGGER.info("bpmnBoundaryEventList: ");
        LOGGER.info(bpmnBoundaryEventList);
        LOGGER.info(" ");
        LOGGER.info("bpmnBoundaryEventList.forEach(boundaryEvent -> ");
        LOGGER.info("items: ");
        bpmnBoundaryEventList.forEach(boundaryEvent -> {
            LOGGER.info(" item: ");
            LOGGER.info("   boundaryEvent.getAttachedTo(): " + boundaryEvent.getAttachedTo());
            LOGGER.info("   boundaryEvent.getName(): " + boundaryEvent.getName());
            LOGGER.info("   boundaryEvent.getId() " + boundaryEvent.getId());
            LOGGER.info("---------------------------------------------------- ");
        });
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info(" ");
        LOGGER.info("bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class) : ");
        LOGGER.info(bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class));
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        LOGGER.info("bpmnFlowModelElementInstance.getUniqueChildElementByType(BoundaryEvent.class) : ");
        LOGGER.info(bpmnFlowModelElementInstance.getChildElementsByType(BoundaryEvent.class));
        LOGGER.info(" ");
        LOGGER.info("===================================================================");
        camundaClient.processSendMessage(chatID, textToSend);

        LOGGER.info("===================================================================");
        LOGGER.info("bpmnBoundaryEventList.stream().filter((boundaryEvent1 -> ");
        LOGGER.info("items: ");

        IntermediateCatchEvent intermediateCatchEvent = bpmnIntermediateCatchEventList.stream().filter((intermediateCatchEventItem) -> {
            LOGGER.info("   intermediateCatchEventItem.getName() : " + intermediateCatchEventItem.getName());
            boolean isValidMessageEvent = intermediateCatchEventItem
                    .getParentElement()
                    .getDomElement()
                    .getLocalName()
                    .equals(delegateExecution.getCurrentActivityName());

            LOGGER.info("   boundaryEventItem.getName() : " + intermediateCatchEventItem.getName());
            LOGGER.info("   boundaryEventItem.getAttachedTo().getName() : " + intermediateCatchEventItem.getName());
            LOGGER.info("   isValidMessageEvent : " + isValidMessageEvent);

            if (isValidMessageEvent) return true;

            else return false;

        }).findFirst().orElseGet(() -> null);

        BoundaryEvent boundaryEvent = bpmnBoundaryEventList.stream().filter((boundaryEventItem) -> {
            LOGGER.info("  item: ");
            LOGGER.info("   boundaryEventItem.cancelActivity() : " + boundaryEventItem.cancelActivity());
            boolean isValidMessageEvent = boundaryEventItem
                    .getAttachedTo()
                    .getName()
                    .equals(delegateExecution.getCurrentActivityName()) || boundaryEventItem.cancelActivity();

            LOGGER.info("   boundaryEventItem.getName() : " + boundaryEventItem.getName());
            LOGGER.info("   boundaryEventItem.getAttachedTo().getName() : " + boundaryEventItem.getName());
            LOGGER.info("   isValidMessageEvent : " + isValidMessageEvent);
            LOGGER.info(" ");

            if (isValidMessageEvent) return true;

            else return false;

        }).findFirst().orElseGet(() ->null);

        CatchEvent catchEvent = intermediateCatchEvent;

        if (Objects.nonNull(catchEvent)) {

            String businessKey = delegateExecution.getCurrentActivityId();
            delegateExecution.setProcessBusinessKey(businessKey);

            ProcessStartMessageCorrelationRequest request = ProcessStartMessageCorrelationRequest
                    .builder()
                    .messageName(catchEvent.getName())
                    .businessKey(businessKey)
                    .build();
            LOGGER.info("ProcessStartMessageCorrelationRequest request: ");
            LOGGER.info(request);
            putInAwaitingChatMap(String.valueOf(chatID), request);
        }

        LOGGER.info("===================================================================");
    }
}
