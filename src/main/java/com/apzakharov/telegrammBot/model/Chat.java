package com.apzakharov.telegrammBot.model;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashMap;

@Data
@Entity
@Table(name = "chats")
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(name = "chatId", nullable = false)
    private  Long chatId;
    @Column(name = "userId", nullable = false)
    private  Long userId;

//    private  LinkedHashMap<Message,Message> chatMap;

}
