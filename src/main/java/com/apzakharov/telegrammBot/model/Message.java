package com.apzakharov.telegrammBot.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Data
@Entity
@Component
@AllArgsConstructor
@RequiredArgsConstructor
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    private final Long chatId;
    private final Long userId;
    private final String text;
    private Integer stateId;

    public Message(org.telegram.telegrambots.meta.api.objects.Message messageFromUpdate) {
        this.chatId = messageFromUpdate.getChatId();
        this.text = messageFromUpdate.getText();
        this.userId = Long.valueOf(messageFromUpdate.getFrom().getId());
    }

}


