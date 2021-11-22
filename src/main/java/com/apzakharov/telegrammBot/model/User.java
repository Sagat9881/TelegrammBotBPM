package com.apzakharov.telegrammBot.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private  Long chatId;
    @Column(name = "state_id")
    private Long stateId;
    @Column(name = "phone")
    private String phone;
    @Column(name = "default_answer")
    private String defaultAnswer;
    @Column(name = "email")
    private String email;
    @Column(name = "admin")
    private Boolean admin;
    @Column(name = "notified")
    private Boolean notified = false;


}
