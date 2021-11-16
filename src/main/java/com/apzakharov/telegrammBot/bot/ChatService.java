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
        LOGGER.info("findById START: "+ id);
        return chatRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Chat findByUserId(Long userId) {
        LOGGER.info("findById START: "+ userId);
        return chatRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Chat findByChatId(Long chatId) {
        LOGGER.info("findById START: "+ chatId);
        return chatRepository.findByChatId(chatId);
    }

    @Transactional(readOnly = true)
    public List<Chat> findAllChats() {
        return chatRepository.findAll();
    }

    @Transactional
    public Chat addChat(Chat chat) {
        LOGGER.info("addChat START " + "CHAT: "+chat);
        return chatRepository.save(chat);
    }

    public Long createNewUser(Long chatId) {
        LOGGER.info("createNewUser START " + "CHATID: "+chatId);
        User user = User.builder()
                .chatId(chatId)
                .build();

        LOGGER.info("NEW USER: " + user.toString());

        return userService.addUser(user).getId();
    }

    @Transactional
    public void reciveMessage(Chat chat, Message message) {
        LOGGER.info("reciveMessage START: \n" + "CHAT: "+chat+"MESSAGE: "+ message);

        Long chatId  = chat.getChatId();
        String input = message.getText();

        User user    = userService.findByChatId(chatId);

        BotContext contex = BotContext.of(chat, user, input);

        LOGGER.info("BotContext: " + contex);
        try {
            camundaClient.processStart(contex);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public Chat createNewChat(Long chatId, Long userId, LinkedHashMap<Message, Message> chatMap) {
        LOGGER.info("createNewChat START: " +"CHATID: "+ chatId+"USERID: "+userId);
        Chat chat =Chat.builder()
                .chatId(chatId)
                .userId(userId)
                .chatMap(chatMap)
                .build();

        Message message = Message.builder()
                .chatId(chatId)
                .userId(userId)
                .text(REGISTRATION_COMMAND)
                .build();

        addChat(chat);

        reciveMessage(chat, message);

        return chat;

    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
