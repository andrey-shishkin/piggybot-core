package ru.piggybox.core.bot.bot.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.piggybox.core.bot.bot.TelegramBot;
import ru.piggybox.core.bot.bot.TelegramBotInitializer;
import ru.piggybox.core.bot.command.callback.delegator.CallbackCommandDelegator;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackRequest;
import ru.piggybox.core.bot.command.inline.delegator.InlineCommandDelegator;
import ru.piggybox.core.bot.command.inline.dto.BotInlineRequest;
import ru.piggybox.core.bot.command.text.TextProcessor;
import ru.piggybox.core.bot.common.dto.BotResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public abstract class GenericTelegramBot extends TelegramLongPollingCommandBot implements TelegramBot, TelegramBotInitializer {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.console-log-enabled}")
    private Boolean consoleLogEnabled;

    private CallbackCommandDelegator callbackDelegator; // TODO Refactor this as it is look like TextProcessor

    private InlineCommandDelegator inlineDelegator;

    @Autowired(required = false)
    private TextProcessor textProcessor;

    public void init() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Couldn't initialize bot. " + e.getMessage());
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        CompletableFuture.supplyAsync(new NonCommandSupplier(update), executor)
                .thenAccept(resp -> {
                    try {
                        execute(resp.getMethod());
                    } catch (TelegramApiException e) {
                        System.out.println("Couldn't perform callback or inline query.");
                        System.out.println(e.getMessage());
                    }
                });
    }

    private CallbackData parseCallbackQuery(String query) {

        final String delimiter = ";";

        String[] parts = query.split(delimiter);
        if (parts.length == 2) {
            return new CallbackData(parts[0], parts[1]);
        }
        if (parts.length == 1) {
            return new CallbackData(parts[0], null);
        }
        return new CallbackData(query, null);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void sendMessage(String message, String userId) throws TelegramApiException {
        execute(SendMessage.builder()
                .text(message)
                .chatId(userId)
                .parseMode(ParseMode.MARKDOWN)
                .build());
    }

    @Override
    public TelegramLongPollingCommandBot getBot() {
        return this;
    }

    @Override
    public void setCallbackCommandDelegator(CallbackCommandDelegator callbackCommandDelegator) {
        this.callbackDelegator = callbackCommandDelegator;
    }

    @Override
    public void setInlineCommandDelegator(InlineCommandDelegator inlineCommandDelegator) {
        this.inlineDelegator = inlineCommandDelegator;
    }

    private static class CallbackData {
        private String command;
        private String parameters;

        CallbackData(String command, String parameters) {
            this.command = command;
            this.parameters = parameters;
        }
    }

    private class NonCommandSupplier implements Supplier<BotResponse> {

        private final Update update;

        public NonCommandSupplier(Update update) {
            this.update = update;
        }

        @Override
        public BotResponse get() {
            BotResponse response = null;
            String query;
            if (update.hasInlineQuery()) {
                query = update.getCallbackQuery().getData();
                response = inlineDelegator.delegate(BotInlineRequest.builder()
                        .command(query)
                        .update(update)
                        .build());
            } else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                query = callbackQuery.getData();
                // Max callback query data length is 64 characters
                CallbackData callbackData = parseCallbackQuery(query);
                if (consoleLogEnabled != null && consoleLogEnabled) {
                    System.out.println("User " + update.getCallbackQuery().getFrom().getId() + ". Query: " + query);
                }
                response = callbackDelegator.delegate(BotCallbackRequest.builder()
                        .command(callbackData.command)
                        .parameters(callbackData.parameters)
                        .update(update)
                        .build());
            } else {
                if (textProcessor != null) {
                    response = textProcessor.processTextMessage(update);
                }
            }
            return response;
        }
    }
}
