package com.apzakharov.telegrammBot.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id", nullable = false)
    private  Long chatId;
    @Column(name = "user_id", nullable = false)
    private  Long userId;
    @Column(name = "text", nullable = false)
    private  String text;
    @Column(name = "stateId")
    private  Integer stateId;

}

