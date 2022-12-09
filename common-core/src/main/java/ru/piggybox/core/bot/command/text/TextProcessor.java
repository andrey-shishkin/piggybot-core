package ru.piggybox.core.bot.command.text;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.piggybox.core.bot.common.dto.BotResponse;

public interface TextProcessor {

    BotResponse processTextMessage(Update update);

}
