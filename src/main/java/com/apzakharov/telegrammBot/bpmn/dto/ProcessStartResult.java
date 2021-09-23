package com.apzakharov.telegrammBot.bpmn.dto;

import lombok.Data;

@Data
public class ProcessStartResult {

    String id;
    String definitionId;
    String businessKey;
    String caseInstanceId;
    String tenantId;
    Boolean ended;
    Boolean suspended;

}