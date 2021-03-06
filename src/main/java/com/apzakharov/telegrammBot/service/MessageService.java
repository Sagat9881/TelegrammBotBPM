package com.apzakharov.telegrammBot.service;

import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.repo.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional(readOnly = true)
    public Message findByChat_id(Long id) {
        return messageRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public Message findByUser_id(Long id) {
        return messageRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    @Transactional
    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }


}

