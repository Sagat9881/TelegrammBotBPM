package com.apzakharov.telegrammBot.bpmn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStartRequestBody {

   private String businessKey;
   private String caseInstanceId;
   private Map<String, Object> variables;

}
