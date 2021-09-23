package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bot.BotContext;

import javax.annotation.Nonnull;

public interface UserProcessService {

    void processStart(@Nonnull BotContext botContext) throws Exception;
}
