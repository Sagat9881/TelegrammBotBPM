package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartMessageCorrelationRequest;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartResult;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.Message;
import com.apzakharov.telegrammBot.model.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.apzakharov.telegrammBot.bot.BotContext.getFromAwaitingChatMap;
import static com.apzakharov.telegrammBot.bot.BotContext.getFromCamundaClientContextMap;


@Builder
@RequiredArgsConstructor
public class ChatBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class);
    private static final String REGISTRATION_COMMAND = "/start";
    private static final String JSON_TYPE_STRING = "String";

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
        LOGGER.info("========================================================================================");
        LOGGER.info("onUpdateReceived : update" + update);
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            LOGGER.info("FAIL: message is not exist ");
            LOGGER.info("========================================================================================");
            return;
        }
        LOGGER.info("========================================================================================");
        org.telegram.telegrambots.meta.api.objects.Message messageFromUpdate = update.getMessage();
        LOGGER.info("update.getMessage(): " + messageFromUpdate);
        LOGGER.info("========================================================================================");
        Chat chat = null;
        Long chatId = update.getMessage().getChatId();

        try {
            LOGGER.info("========================================================================================");
            chat = chatService.findByChat_id(chatId);

            LOGGER.info("CHATID: " + chatId);
            LOGGER.info("CHAT: " + chat);
            LOGGER.info("========================================================================================");
        } catch (Exception e) {
            LOGGER.info("FIND CHAT PROCESS FAIL: " + e.getClass().getSimpleName());
            LOGGER.info("========================================================================================");
            return;
        }


        if (chat == null) {
            try {
                LOGGER.info("========================================================================================");
                User user = chatService.createNewUser(chatId);
                LOGGER.info("chatID : " + chatId);
                LOGGER.info("chatService.createNewUser(chatId) : " + user);

                chat = chatService.createNewChat(chatId, user.getId());
                LOGGER.info("user.getId(): " + user.getId());
                LOGGER.info("chatId: " + chatId);
                LOGGER.info("chatService.createNewChat(chatId, user.getId()): " + chat);
                LOGGER.info("========================================================================================");
                Message message = Message.builder()
                        .chatId(chatId)
                        .userId(user.getId())
                        .text(REGISTRATION_COMMAND)
                        .build();

                Message addedMessage = chatService.addMessage(message);
                LOGGER.info("========================================================================================");
                LOGGER.info("REGISTRATION COMMAND MESSAGE: " + message);
                LOGGER.info("========================================================================================");

                getFromCamundaClientContextMap(getBotUsername())
                        .startCommand(chat.getChatId(), addedMessage.getText());

            } catch (Exception e) {
                LOGGER.info("NEW USER PROCESS FAIL: " + e.getClass().getSimpleName());
                LOGGER.info("========================================================================================");
            }
        } else {
            try {
                User user = chatService.findUserByChatId(chatId);

                Message message = chatService.addMessage(
                        Message.builder()
                                .chatId(chatId)
                                .userId(chat.getUserId())
                                .text(messageFromUpdate.getText())
                                .build()
                );
                LOGGER.info("========================================================================================");
                LOGGER.info("Message: " + message);
                LOGGER.info("========================================================================================");

                if (message.getText().contains("/")) {
                    ProcessStartResult processStartResult = getFromCamundaClientContextMap(getBotUsername())
                            .startCommand(chat.getChatId(), message.getText());

                    LOGGER.info("onUpdateReceived END: processStartResult");
                    LOGGER.info(processStartResult);

                } else {

                    ProcessStartMessageCorrelationRequest request = getFromAwaitingChatMap(String.valueOf(chatId));
                    Map<String, ProcessVariable> processVariabelsMap = new HashMap<>();

                    processVariabelsMap.put("Input", ProcessVariable.builder()
                            .value(message.getText())
                            .type(JSON_TYPE_STRING)
                            .build());

                    request.setProcessVariables(processVariabelsMap);

                    ProcessStartResult messageCorrelationStartResult = getFromCamundaClientContextMap(getBotUsername())
                            .createCorrelation(request);

                    LOGGER.info("onUpdateReceived END: messageCorrelationStartResult: ");
                    LOGGER.info(messageCorrelationStartResult);

                }

                LOGGER.info("========================================================================================");
            } catch (Exception e) {
                LOGGER.info("RECEIVE MESSAGE PROCESS FAIL: " + e.getClass().getSimpleName());
                LOGGER.info("========================================================================================");

            }
        }
    }

    public void sendMessage(Long chatId, String text) {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatBot.sendMessage. ChatID: " + chatId + ", and text: " + text);
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);

        LOGGER.info("messageText: " + message.getText() + ", messageChatID: " + message.getChatId() + "messageMethod: " + message.getMethod());
        LOGGER.info("========================================================================================");
        try {
            execute(message);
            LOGGER.info("ChatBot.sendMessage END");
            LOGGER.info("========================================================================================");
        } catch (TelegramApiException e) {
            LOGGER.info("ChatBot.sendMessage PROCESS FAIL: " + e.getClass().getSimpleName());
            LOGGER.info("========================================================================================");
            return;

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
            e.getLocalizedMessage();
        }
    }

    public void addMessage(Message message) {
        LOGGER.info("========================================================================================");
        LOGGER.info("ChatBot.addMessage: message: " + message);
        try {
            chatService.addMessage(message);
            LOGGER.info("ChatBot.addMessage END");
            LOGGER.info("========================================================================================");
        } catch (Exception e) {
            LOGGER.info("ChatBot.addMessage: PROCESS FAIL: " + e.getClass().getSimpleName());
            LOGGER.info("========================================================================================");
            return;
        }

    }

    public Chat findByChatId(Long chatId) {
        LOGGER.info("ChatBot.findByChatId: chatId: " + chatId);
        LOGGER.info("========================================================================================");
        Chat chat = null;
        try {
            LOGGER.info("========================================================================================");
            Optional<Chat> chatOptional = Optional.of(chatService.findByChat_id(chatId));
            chat = chatOptional.orElseGet(() -> null);
            LOGGER.info("ChatBot.findByChatId: chat: " + chat);
            LOGGER.info("========================================================================================");
        } catch (Exception e) {
            LOGGER.info("ChatBot.findByChatId: PROCESS FAI: \n" + e.getClass().getSimpleName());
            LOGGER.info("========================================================================================");
        }

        return chat;

    }


}
