package ru.piggybox.core.bot.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.piggybox.core.bot.bot.TelegramBotInitializer;
import ru.piggybox.core.bot.command.callback.delegator.factory.CallbackCommandDelegatorFactory;
import ru.piggybox.core.bot.command.inline.delegator.factory.InlineCommandDelegatorFactory;
import ru.piggybox.core.bot.command.slash.initializer.impl.DefaultSlashCommandInitializer;

import javax.annotation.PostConstruct;

import static ru.piggybox.core.bot.common.Constants.DUMMY_BOT_PROFILE_NAME;

@Component
@Profile("!" + DUMMY_BOT_PROFILE_NAME)
public class BotBuilder {
    @Autowired
    private TelegramBotInitializer botInitializer;
    @Autowired
    private CallbackCommandDelegatorFactory callbackFactory;
    @Autowired
    private InlineCommandDelegatorFactory inlineFactory;
    @Autowired
    private DefaultSlashCommandInitializer slashCommandInitializer;

    @PostConstruct
    void init() throws TelegramApiException {
        System.out.println("Building bot");
        slashCommandInitializer.init(botInitializer.getBot());
        botInitializer.setCallbackCommandDelegator(callbackFactory.getDelegator());
        botInitializer.setInlineCommandDelegator(inlineFactory.getDelegator());
        botInitializer.init();
        System.out.println("Building bot completed");
    }
}
