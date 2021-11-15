package com.apzakharov.telegrammBot.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Component
@NoArgsConstructor
@RequiredArgsConstructor
public class Chat {

    @Id
    @GeneratedValue
    private Long id;

    private final Long chatId;
    private final Long userId;
    private final LinkedHashMap<Message,Message> chatMap;

    public Chat(Long chatId, Long userId, LinkedHashMap<Message, Message> chatMap) {
        this.chatId = chatId;
        this.userId = userId;
        this.chatMap = chatMap;
    }
}
