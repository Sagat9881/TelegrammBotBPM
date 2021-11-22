package com.apzakharov.telegrammBot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashMap;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(name = "chat_id", nullable = false)
    private  Long chatId;
    @Column(name = "user_id", nullable = false)
    private  Long userId;
    @Column(name = "chatBot_name", nullable = false)
    private  String chatBotName;

//    private  LinkedHashMap<Message,Message> chatMap;

}
