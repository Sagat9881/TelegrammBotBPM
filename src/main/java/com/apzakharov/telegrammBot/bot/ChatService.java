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

    private static final String REGISTRATION_COMMAND = "/start";


    @Transactional(readOnly = true)
    public Chat findByChatId(long id) {
        return chatRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public Chat findByUserId(long id) {
        return chatRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public List<Chat> findAllChats() {
        return chatRepository.findAll();
    }

    @Transactional
    public Chat addChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Long createNewUser(long id) {
        User user = new User(id);

        return userService.addUser(user).getId();
    }

    @Transactional
    public void reciveMessage(Chat chat, Message message) {

        Long chatId  = chat.getChatId();
        String input = message.getText();
        User user    = userService.findByChatId(chatId);

        BotContext contex = BotContext.of(chat, user, input);
        try {
            camundaClient.processStart(contex);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public Chat createNewChat(Long chatId, LinkedHashMap<Message, Message> chatMap, Long userId) {
        Chat chat = new Chat(chatId, userId, chatMap);

        reciveMessage(chat, new Message(chatId, userId, REGISTRATION_COMMAND));

        return addChat(chat);

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
