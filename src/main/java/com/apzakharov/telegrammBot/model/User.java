package com.apzakharov.telegrammBot.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Data
@Entity
@Component
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private final Long chatId;

    private Integer stateId;
    private String phone;
    private String email;
    private Boolean admin;
    private Boolean notified = false;


}
