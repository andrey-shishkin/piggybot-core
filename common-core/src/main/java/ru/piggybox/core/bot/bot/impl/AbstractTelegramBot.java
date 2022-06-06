package ru.piggybox.core.bot.bot.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.piggybox.core.bot.bot.ITelegramBot;
import ru.piggybox.core.bot.command.callback.delegator.CallbackCommandDelegator;
import ru.piggybox.core.bot.command.callback.delegator.factory.CallbackCommandDelegatorFactory;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackRequest;
import ru.piggybox.core.bot.command.inline.delegator.InlineCommandDelegator;
import ru.piggybox.core.bot.command.inline.delegator.factory.InlineCommandDelegatorFactory;
import ru.piggybox.core.bot.command.inline.dto.BotInlineRequest;
import ru.piggybox.core.bot.common.dto.BotResponse;

import javax.annotation.PostConstruct;

public abstract class AbstractTelegramBot extends TelegramLongPollingCommandBot implements ITelegramBot {

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    private CallbackCommandDelegatorFactory callbackFactory;
    @Autowired
    private InlineCommandDelegatorFactory inlineFactory;

    private CallbackCommandDelegator callbackDelegator;

    private InlineCommandDelegator inlineDelegator;

    @PostConstruct
    public void init() {
        try {
            callbackDelegator = callbackFactory.getDelegator();
            inlineDelegator = inlineFactory.getDelegator();
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Couldn't initialize bot. " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        BotResponse response = null;
        String query = null;
        if (update.hasInlineQuery()) {
            query = update.getCallbackQuery().getData();
            response = inlineDelegator.delegate(BotInlineRequest.builder()
                    .command(query)
                    .user(update.getCallbackQuery().getFrom())
                    .build());
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            query = callbackQuery.getData();
            response = callbackDelegator.delegate(BotCallbackRequest.builder()
                    .command(query)
                    .user(update.getCallbackQuery().getFrom())
                    .build());
        }
        if (response != null) {
            try {
                execute(response.getMethod());
            } catch (TelegramApiException e) {
                System.out.println("Couldn't perform callback or inline query. " + query);
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
