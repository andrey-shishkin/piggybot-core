package ru.piggybox.core.bot.bot;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.piggybox.core.bot.message.MessageParseMode;

public interface TelegramBot {

    void sendMessage(String message, String userId, MessageParseMode parseMode) throws TelegramApiException;
}
