package ru.piggybox.core.bot.bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramBot {

    void sendMessage(String message, String userId) throws TelegramApiException;
}
