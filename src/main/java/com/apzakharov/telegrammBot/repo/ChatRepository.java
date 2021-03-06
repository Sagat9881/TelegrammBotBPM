package com.apzakharov.telegrammBot.repo;

import com.apzakharov.telegrammBot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {


    Optional<Chat> findById(Long id);
    Optional <Chat> findByUserId(Long user_id);
    Optional <Chat> findByChatId(Long chat_Id);
}
