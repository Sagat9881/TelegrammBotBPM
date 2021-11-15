package com.apzakharov.telegrammBot.bot;

import com.apzakharov.telegrammBot.model.Chat;
import com.apzakharov.telegrammBot.model.User;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BotContext {
    private ChatBot bot;
    private Chat chat;
    private final User user;
    private final String input;

    public static BotContext of(ChatBot bot, Chat chat,User user, String text) {
        return new BotContext(bot, chat, user, text);
    }
    public static BotContext of( Chat chat,User user, String text) {
        return new BotContext(chat, user, text);
    }
    public static BotContext of( ChatBot bot,User user, String text) {
        return new BotContext(bot, user, text);
    }

    private BotContext(ChatBot bot,Chat chat, User user, String input) {
        this.bot   = bot;
        this.user  = user;
        this.input = input;
        this.chat  = chat;
    }

    private BotContext(Chat chat, User user, String input) {
        this.user  = user;
        this.input = input;
        this.chat  = chat;
    }

    private BotContext(ChatBot bot,User user, String input) {
        this.user  = user;
        this.input = input;
        this.bot   = bot;
    }

    public ChatBot getBot() {
        return bot;
    }

    public User getUser() {
        return user;
    }

    public String getInput() {
        return input;
    }
}
