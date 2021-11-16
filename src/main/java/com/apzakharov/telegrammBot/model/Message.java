package com.apzakharov.telegrammBot.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;


@Data
@Entity
@Table(name = "messages")
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id", nullable = false)
    private  Long chat_id;
    @Column(name = "user_id", nullable = false)
    private  Long user_id;
    @Column(name = "text", nullable = false)
    private  String text;
    @Column(name = "state_id")
    private  Integer state_id;

}

