package com.apzakharov.telegrammBot.repo;

import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findByChatId(long id);
    Message findByUserId(long id);
}
