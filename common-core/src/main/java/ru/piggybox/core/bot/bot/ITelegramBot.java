package ru.piggybox.core.bot.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface ITelegramBot {

    void init();

    void sendMessage(String message, String userId);

    void sendMessage(String message, InlineKeyboardMarkup markup, String userId);

    //void execute(BotApiMethod<? extends Serializable> method);

    /*void registerCommand(IBotCommand botCommand);*/

    /*void executeMethod(BotApiMethod<Boolean> method);*/

}
