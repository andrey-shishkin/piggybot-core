package ru.piggybox.core.bot.common.dto;

import org.telegram.telegrambots.meta.api.objects.User;

public interface BotRequest {

    String getCommand();

    User getUser();
}
