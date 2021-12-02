package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.model.User;
import com.apzakharov.telegrammBot.repo.ChatRepository;
import com.apzakharov.telegrammBot.service.MessageService;
import com.apzakharov.telegrammBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserService userService;
    private final MessageService messageService;
    private final ChatRepository chatRepository;

    private static final Logger LOGGER = LogManager.getLogger(ChatService.class);


    @Transactional(readOnly = true)
    public Chat findById(Long id) {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatService.findById : id " + id);

        Optional<Chat> optionalChat = chatRepository.findById(id);

        Chat chat = optionalChat.orElseGet(() -> null);
        LOGGER.info("ChatService.findById:  chat" + chat);
        LOGGER.info("========================================================================================");

        return chat;
    }

    @Transactional(readOnly = true)
    public Chat findByUser_id(Long user_id) throws Exception {
        LOGGER.info("findByUserId START: user_id " + user_id);

        Optional<Chat> optionalChat = chatRepository.findByUserId(user_id);

        Chat chat = optionalChat.orElseGet(() -> null);
        LOGGER.info("findByUserId RESULT:  " + chat);

        return chat;
    }

    @Transactional(readOnly = true)
    public Chat findByChat_id(Long chat_id) throws Exception {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatService.findByChatId: chat_id " + chat_id);

        Optional<Chat> optionalChat = chatRepository.findByChatId(chat_id);

        Chat chat = optionalChat.orElseGet(() -> null);
        LOGGER.info("ChatService.findByChatId:  chat" + chat);
        LOGGER.info("========================================================================================");
        return chat;
    }

    @Transactional(readOnly = true)
    public User findUserByChatId(Long chatId) throws Exception {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatService.findUserByChatId START: chat_id " + chatId);

        AtomicReference<User> userAtomic = new AtomicReference<>();

        User user = userService.findByChatId(chatId).orElseGet(() -> null);
        LOGGER.info("ChatService.findUserByChatId :  user: " + user);
        LOGGER.info("========================================================================================");

        return user;
    }

    @Transactional(readOnly = true)
    public List<Chat> findAllChats() throws Exception {
        return chatRepository.findAll();
    }

    @Transactional
    public Chat addChat(Chat chat) throws Exception {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatService.addChat :  chat: " + chat);
        Chat addedChat = chatRepository.save(chat);
        LOGGER.info("ChatService.addChat :  addedChat: " + addedChat);
        LOGGER.info("========================================================================================");
        return addedChat;
    }

    @Transactional
    public Message addMessage(Message message) throws Exception {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatService.addMessage :  message: " + message);
        Message addedMessage = messageService.addMessage(message);
        LOGGER.info("ChatService.addMessage :  addedMessage: " + addedMessage);
        LOGGER.info("========================================================================================");
        return addedMessage;
    }


    public Chat createNewChat(Long chatId, Long userId) throws Exception {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatService.createNewChat :  chatId: " + chatId+" userId: "+userId);

        Chat chat = Chat.builder()
                .chatId(chatId)
                .userId(userId)
                .build();

        Chat addedChat = addChat(chat);

        LOGGER.info("ChatService.createNewChat :  addedChat: " + addedChat);
        LOGGER.info("========================================================================================");

        return chat;
    }

    public User createNewUser(Long chatId) throws Exception {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatService.createNewUser :  chatId: " + chatId);

        User user = User.builder()
                .chatId(chatId)
                .build();

        User addedUser = userService.addUser(user);

        LOGGER.info("ChatService.createNewUser :  addedUser: " + addedUser);
        LOGGER.info("========================================================================================");

        return addedUser;
    }

}
