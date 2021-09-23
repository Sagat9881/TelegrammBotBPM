package com.apzakharov.telegrammBot.bpmn.dto;

import lombok.Data;



import java.util.Map;

@Data
public class ProcessStartRequestBody {

    String businessKey;
    String caseInstanceId;
    Map<String, ProcessVariable> variables;

}
