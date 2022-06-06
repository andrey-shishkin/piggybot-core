package ru.piggybox.core.bot.common.dto;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotRequest {

    String getCommand();

    String getPreviousCommand();

    Update getUpdate();
}
