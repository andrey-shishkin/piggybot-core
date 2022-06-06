package ru.piggybox.core.bot.command.slash.dto;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.piggybox.core.bot.common.dto.BotRequest;

public interface BotSlashRequest extends BotRequest {

    AbsSender getAbsSender();

    Chat getChat();

    String[] getArguments();

}
