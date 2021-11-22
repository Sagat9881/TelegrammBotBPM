package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.model.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;

import static com.apzakharov.telegrammBot.bot.BotContext.getFromCamundaClientContextMap;


@Builder
@RequiredArgsConstructor
public class ChatBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class);
    private static final String REGISTRATION_COMMAND = "/start";

    private final ChatService chatService;

    private static final String BROADCAST = "broadcast ";
    private static final String LIST_USERS = "users";
//    private org.telegram.telegrambots.meta.api.objects.Message messageFromUpdate;

    private final String botName;

    private final String botToken;

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

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("UPDATE RECIVE START : ");
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            LOGGER.info("FAIL: message is not exist ");
            return;
        }

        org.telegram.telegrambots.meta.api.objects.Message messageFromUpdate = update.getMessage();
        LOGGER.info("INCOME MESSAGE: " + messageFromUpdate);

        Chat chat = null;
        Long chatId = update.getMessage().getChatId();

        try {
            chat = chatService.findByChat_id(chatId);

            LOGGER.info("CHATID: " + chatId);
            LOGGER.info("CHAT: " + chat);
        } catch (Exception e) {
            LOGGER.info("FIND CHAT PROCESS FAIL: \nExeptionMessage:\n" + e.getLocalizedMessage() + "\n");
            return;
        }


        if (chat == null) {
            try {
                User user = chatService.createNewUser(chatId);
                LOGGER.info("NEW USER : " + user);

                chat = chatService.createNewChat(chatId, user.getId());
                LOGGER.info("NEW CHAT ID: " + chatId + "\nNEW CHAT: " + chat);

                Message message = Message.builder()
                        .chatId(chatId)
                        .userId(user.getId())
                        .text(REGISTRATION_COMMAND)
                        .build();
                Message addedMessage = chatService.addMessage(message);

                LOGGER.info("REGISTRATION COMMAND MESSAGE: " + message);

                getFromCamundaClientContextMap(getBotUsername())
                        .processStart(chat,user,addedMessage.getText());

            } catch (Exception e) {
                LOGGER.info("NEW USER PROCESS FAIL: \nExeptionMessage:\n" + e.getLocalizedMessage() + "\n");
                return;
            }
        }
        User user = chatService.findUserByChatIdOrCreateNew(chatId);

        Message message = chatService.addMessage(
                Message.builder()
                        .chatId(chatId)
                        .userId(chat.getUserId())
                        .text(messageFromUpdate.getText())
                        .build()
        );

        LOGGER.info("Message: " + message);

        try {

            sendMessage(chat.getChatId(), message.getText());

            LOGGER.info("reciveMessage END");
        } catch (Exception e) {
            LOGGER.info("RECIVE MESSAGE PROCESS FAIL: \nExeptionMessage:\n" + e.getLocalizedMessage() + "\n");
            return;
        }

        LOGGER.info("UPDATE RECIVE END");
    }

    public void sendMessage(Long chatId, String text) {
        LOGGER.info("Bot service try to sendMessag. ChatID: " + chatId + ", and text: " + text);
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);

        LOGGER.info("messageText: " + message.getText() + ", messageChatID: " + message.getChatId() + "messageMethod: " + message.getMethod());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.getLocalizedMessage();

        }
        LOGGER.info("ChatBot.sendMessage END");
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
            e.getLocalizedMessage();
        }
    }

    public void addMessage(Message message) {
        try {
            chatService.addMessage(message);
        }catch (Exception e){
            LOGGER.info("ADD MESSAGE FAIL: \n"+e.getLocalizedMessage());
        }

    }

    public Chat findByChatId(Long chatId) {
        try {
           return chatService.findByChat_id(chatId);
        }catch (Exception e){
            LOGGER.info("findByChatId FAIL: \n"+e.getLocalizedMessage());
        }

        return null;

    }


}
