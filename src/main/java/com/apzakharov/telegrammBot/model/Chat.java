package com.apzakharov.telegrammBot.model;


import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Builder
@Component
public class Chat {

    @Id
    @GeneratedValue
    private Long id;

    private  Long chatId;
    private  Long userId;
    private  LinkedHashMap<Message,Message> chatMap;

}
