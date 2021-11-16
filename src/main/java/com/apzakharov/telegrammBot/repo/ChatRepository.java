package com.apzakharov.telegrammBot.repo;

import com.apzakharov.telegrammBot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findById(Long id);
    Chat findByUserId(Long id);
}
