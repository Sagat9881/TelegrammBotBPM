package com.apzakharov.telegrammBot.repo;

import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat findByChatId(long id);
    Chat findByUserId(long id);
}
