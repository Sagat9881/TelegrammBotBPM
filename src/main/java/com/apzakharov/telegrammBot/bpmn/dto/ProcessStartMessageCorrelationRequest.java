package com.apzakharov.telegrammBot.bpmn.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Builder
@Component
public class ProcessStartMessageCorrelationRequest {

    private String messageName;
    private String businessKey;
    private Map<String, ProcessVariable> correlationKeys;
    private Map<String, ProcessVariable> processVariables;
}
