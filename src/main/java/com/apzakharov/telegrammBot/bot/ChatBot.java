package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.bpmn.service.UserProcessService;
import com.apzakharov.telegrammBot.model.User;
import com.apzakharov.telegrammBot.service.UserService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class);

    private static final String BROADCAST = "broadcast ";
    private static final String LIST_USERS = "users";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final UserService userService;
    private final UserProcessService userProcessService;
//    private final UserProcessService serProcessService;

    public ChatBot(UserService userService, UserProcessService userProcessService) {
        this.userService = userService;
        this.userProcessService = userProcessService;
    }

    @Override
    public String toString() {
        return "ChatBot{" +
                "botName='" + botName + '\'' +
                ", botToken='" + botToken + '\'' +
                ", userService=" + userService +
                ", userProcessService=" + userProcessService +
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

        final String text = update.getMessage().getText();
        final long chatId = update.getMessage().getChatId();

        User user = userService.findByChatId(chatId);

//        if (checkIfAdminCommand(user, text))
//            return;

        BotContext context;


        if (user == null) {


            user = new User(chatId, BotStateBPMN.REG.getBotStateBPMNID());
            userService.addUser(user);

            context = BotContext.of(this, user, text);
            LOGGER.info("BotContext of user with id" + chatId+ "Input: "+ text);
            user.setStateId(BotStateBPMN.WAIT.getBotStateBPMNID());

            try {
                LOGGER.info("processStart for user with id:  " + chatId);
                userProcessService.processStart(context);
            } catch (Exception e) {
                LOGGER.info("processStart fail for user with id:  " + chatId);
                e.printStackTrace();
            }

            LOGGER.info("New user registered: " + chatId);
        } else {
            context = BotContext.of(this, user, text);
            LOGGER.info("BotContext of user with id" + chatId+ "Input: "+ text);
//            state = BotState.byId(user.getStateId());

            try {
                LOGGER.info("processStart for user with id: " + chatId);
                userProcessService.processStart(context);
            } catch (Exception e) {
                LOGGER.info("processStart fail for user with id:  " + chatId);
                e.printStackTrace();
            }

           LOGGER.info("Update received for user with id: "+ chatId);
        }

        userService.updateUser(user);
    }

    private boolean checkIfAdminCommand(User user, String text) {
        if (user == null || !user.getAdmin())
            return false;

        if (text.startsWith(BROADCAST)) {
            LOGGER.info("Admin command received: " + BROADCAST);

            text = text.substring(BROADCAST.length());
            broadcast(text);

            return true;
        } else if (text.equals(LIST_USERS)) {
            LOGGER.info("Admin command received: " + LIST_USERS);

            listUsers(user);
            return true;
        }

        return false;
    }

    public void sendMessage(Long chatId, String text) {
        LOGGER.info("Bot service try to sendMessag. ChatID: "+chatId+", and text: "+text);
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);

        LOGGER.info("messageText: "+message.getText()+", messageChatID: "+message.getChatId()+"messageMethod: "+message.getMethod());
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

    private void listUsers(User admin) {
        StringBuilder sb = new StringBuilder("All users list:\r\n");
        List<User> users = userService.findAllUsers();

        users.forEach(user ->
            sb.append(user.getId())
                    .append(' ')
                    .append(user.getPhone())
                    .append(' ')
                    .append(user.getEmail())
                    .append("\r\n")
        );

        sendPhoto(admin.getChatId());
        sendMessage(admin.getChatId(), sb.toString());
    }

    private void broadcast(String text) {
        List<User> users = userService.findAllUsers();
        users.forEach(user -> sendMessage(user.getChatId(), text));
    }
}
