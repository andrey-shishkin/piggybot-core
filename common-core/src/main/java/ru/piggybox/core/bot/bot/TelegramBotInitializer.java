package ru.piggybox.core.bot.bot;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import ru.piggybox.core.bot.command.callback.delegator.CallbackCommandDelegator;
import ru.piggybox.core.bot.command.inline.delegator.InlineCommandDelegator;

public interface TelegramBotInitializer {

    void setCallbackCommandDelegator(CallbackCommandDelegator callbackCommandDelegator);

    void setInlineCommandDelegator(InlineCommandDelegator inlineCommandDelegator);

    void init();

    TelegramLongPollingCommandBot getBot();


}
