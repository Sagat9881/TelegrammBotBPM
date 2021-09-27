package com.apzakharov.telegrammBot.bpmn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ProcessVariable {

   private String type;
   private String value;

}