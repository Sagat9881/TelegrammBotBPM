package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.bpmn.service.CamundaClient;
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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserService userService;
    private final MessageService messageService;
    private final CamundaClient camundaClient;
    private final ChatRepository chatRepository;

    private static final Logger LOGGER = LogManager.getLogger(ChatService.class);

    private static final String REGISTRATION_COMMAND = "/start";

    @Transactional(readOnly = true)
    public Chat findById(Long id) {
        LOGGER.info("findById START: id " + id);

        Optional<Chat> optionalChat = chatRepository.findById(id);
        if (optionalChat.isPresent()) {
            return optionalChat.get();
        }
        LOGGER.info("findById RESULT: NULL ");
        return null;

    }

    @Transactional(readOnly = true)
    public Chat findByUser_id(Long user_id) throws Exception {
        LOGGER.info("findByUserId START: user_id " + user_id);

        Optional<Chat> chatOptional = chatRepository.findByUser_id(user_id);

        if (chatOptional.isPresent()) {
            return chatOptional.get();
        }

        LOGGER.info("findByUserId RESULT: NULL ");
        return null;


    }

    @Transactional(readOnly = true)
    public Chat findByChat_id(Long chat_id) throws Exception {
        LOGGER.info("findByChatId START: chat_id " + chat_id);

        Optional<Chat> optionalChat = chatRepository.findByChat_id(chat_id);
        if (optionalChat.isPresent()) {
            return optionalChat.get();
        }
        LOGGER.info("findByChatId RESULT: NULL ");
        return null;
    }

    @Transactional(readOnly = true)
    public List<Chat> findAllChats() throws Exception {
        return chatRepository.findAll();
    }

    @Transactional
    public Chat addChat(Chat chat) throws Exception {
        LOGGER.info("addChat START " + "CHAT: " + chat);
        return chatRepository.save(chat);
    }

    @Transactional
    public void reciveMessage(Chat chat, Message message) throws Exception {
        LOGGER.info("reciveMessage START: \n" + "CHAT: " + chat + "MESSAGE: " + message);

        Long chatId = chat.getChat_id();
        String input = message.getText();

        User user = userService.findByChat_id(chatId);

        BotContext contex = BotContext.of(chat, user, input);

        LOGGER.info("BotContext: " + contex);
        try {
            camundaClient.processStart(contex);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public Long createNewUser(Long chatId) throws Exception {
        LOGGER.info("createNewUser START " + "CHATID: " + chatId);
        User user = User.builder()
                .chat_id(chatId)
                .build();

        LOGGER.info("NEW USER: " + user.toString());

        return userService.addUser(user).getId();
    }

    public Chat createNewChat(Long chatId, Long userId) throws Exception {
        LOGGER.info("createNewChat START: " + "CHATID: " + chatId + "USERID: " + userId);
        Chat chat = Chat.builder()
                .chat_id(chatId)
                .user_id(userId)
                .build();

        Message message = Message.builder()
                .chat_id(chatId)
                .user_id(userId)
                .text(REGISTRATION_COMMAND)
                .build();

        addChat(chat);

        reciveMessage(chat, message);

        return chat;

    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChat_id())
                .setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
