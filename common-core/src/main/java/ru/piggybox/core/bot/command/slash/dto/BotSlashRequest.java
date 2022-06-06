package ru.piggybox.core.bot.command.slash.dto;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface BotSlashRequest {

    AbsSender getAbsSender();

    Chat getChat();

    User getUser();

    String[] getArguments();

}
