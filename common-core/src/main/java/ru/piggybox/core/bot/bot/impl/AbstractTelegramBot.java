package ru.piggybox.core.bot.bot.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
    @Override
    public void init() {
        try {
            /*List<BotCommand> mainMenu = new ArrayList<>();
            getMainMenuItems().forEach(p -> mainMenu.add(new BotCommand(p.getLeft(), p.getRight())));
            BOT.execute(new SetMyCommands(mainMenu, null, null));*/
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Couldn't initialize bot");
        }
    }

    public void registerCommand(IBotCommand command) {
        register(command);
        SendDocument document = new SendDocument();
    }

    @Override
    public void sendMessage(String message, String userId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(userId);
        msg.setText(message);
        msg.setParseMode(ParseMode.MARKDOWN);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            System.out.println("Error during sending the answer " + msg);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sendMessage(String message, InlineKeyboardMarkup markup, String userId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(userId);
        msg.setText(message);
        msg.setParseMode(ParseMode.MARKDOWN);
        msg.setReplyMarkup(markup);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            System.out.println("Error during sending the answer " + msg);
            System.out.println(e.getMessage());
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
            try {
                execute(response.getMethod());
            } catch (TelegramApiException e) {
                System.out.println("Couldn't perform inline query. " + query);
                System.out.println(e.getMessage());
            }
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
                System.out.println("Couldn't perform callback query. " + query);
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
