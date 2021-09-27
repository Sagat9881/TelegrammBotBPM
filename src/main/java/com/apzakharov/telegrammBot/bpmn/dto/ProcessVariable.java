package com.apzakharov.telegrammBot.bpmn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProcessVariable {

   private String type;
   private String value;

}