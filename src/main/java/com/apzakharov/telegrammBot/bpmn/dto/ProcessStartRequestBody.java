package com.apzakharov.telegrammBot.bpmn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Map;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStartRequestBody {
   @Id
   @GeneratedValue
   private Long id;

   private String businessKey;
   private String caseInstanceId;
   private Map<String, Object> variables;

}
