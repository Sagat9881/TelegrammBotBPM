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
    @Column(name = "chat_id", nullable = false)
    private  Long chat_id;
    @Column(name = "user_id", nullable = false)
    private  Long user_id;

//    private  LinkedHashMap<Message,Message> chatMap;

}
