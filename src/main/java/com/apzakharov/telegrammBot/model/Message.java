package com.apzakharov.telegrammBot.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



@Data
@Entity
@Builder
@Component
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    private  Long chatId;
    private  Long userId;
    private  String text;
    private  Integer stateId;

}

