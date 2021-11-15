package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class);

    private static final String BROADCAST = "broadcast ";
    private static final String LIST_USERS = "users";
    private org.telegram.telegrambots.meta.api.objects.Message message;

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final ChatService chatService;

    @Override
    public String toString() {
        return "ChatBot{" +
                "  botName='" + botName + '\'' +
                ", botToken='" + botToken + '\'' +
                '}';
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        message = update.getMessage();
        final long chatId = message.getChatId();

        Chat chat = chatService.findByChatId(chatId);

        if (chat == null) {
            Long userId = chatService.createNewUser(chatId);
            LinkedHashMap<Message, Message> chatMap = new LinkedHashMap<>();

            chat = chatService.createNewChat(chatId,chatMap,userId);
        }

        chatService.reciveMessage(chat, new Message(message));


    }

//    @Override
//    public void onUpdateReceived(Update update) {
//        if (!update.hasMessage() || !update.getMessage().hasText())
//            return;
//
//        final String text = update.getMessage().getText();
//        final long chatId = update.getMessage().getChatId();
//
//        User user = userService.findByChatId(chatId);
//
////        if (checkIfAdminCommand(user, text))
////            return;
//
//        BotContext context;
//
//        if (user == null) {
//
//
//            user = new User(chatId, BotStateBPMN.REG.getBotStateBPMNID());
//            userService.addUser(user);
//
//            context = BotContext.of(this, user, text);
//            LOGGER.info("BotContext of user with id" + chatId + "Input: " + text);
//            user.setStateId(BotStateBPMN.WAIT.getBotStateBPMNID());
//
//            try {
//                LOGGER.info("processStart for user with id:  " + chatId);
//                userProcessService.processStart(context);
//            } catch (Exception e) {
//                LOGGER.info("processStart fail for user with id:  " + chatId);
//                e.printStackTrace();
//            }
//
//            LOGGER.info("New user registered: " + chatId);
//        } else {
//            context = BotContext.of(this, user, text);
//            LOGGER.info("BotContext of user with id" + chatId + "Input: " + text);
////            state = BotState.byId(user.getStateId());
//
//            try {
//                LOGGER.info("processStart for user with id: " + chatId);
//                userProcessService.processStart(context);
//            } catch (Exception e) {
//                LOGGER.info("processStart fail for user with id:  " + chatId);
//                e.printStackTrace();
//            }
//
//            LOGGER.info("Update received for user with id: " + chatId);
//        }
//
//        userService.updateUser(user);
//    }


    public void sendMessage(Long chatId, String text) {
        LOGGER.info("Bot service try to sendMessag. ChatID: " + chatId + ", and text: " + text);
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);

        LOGGER.info("messageText: " + message.getText() + ", messageChatID: " + message.getChatId() + "messageMethod: " + message.getMethod());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }
    }

    private void sendPhoto(long chatId) {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("test.png");

        SendPhoto message = new SendPhoto()
                .setChatId(chatId)
                .setPhoto("test", is);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
