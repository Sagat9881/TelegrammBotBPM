package com.apzakharov.telegrammBot.bpmn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@ToString
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ProcessStartResult {

    private String id;
    private String definitionId;
    private String businessKey;
    private String caseInstanceId;
    private String tenantId;
    private Boolean ended;
    private Boolean suspended;

}