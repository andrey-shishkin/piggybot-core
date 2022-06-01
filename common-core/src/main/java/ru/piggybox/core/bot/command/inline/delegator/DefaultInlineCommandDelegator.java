package ru.piggybox.core.bot.command.inline.delegator;

import com.google.common.collect.ImmutableMap;
import ru.piggybox.core.bot.command.inline.dto.InlineCommand;
import ru.piggybox.core.bot.command.inline.controller.InlineCommandController;
import ru.piggybox.core.bot.command.inline.dto.BotInlineRequest;
import ru.piggybox.core.bot.command.inline.dto.BotInlineResponse;
import ru.piggybox.core.bot.common.command.delegator.GenericCommandDelegator;

public class DefaultInlineCommandDelegator extends GenericCommandDelegator<BotInlineRequest, BotInlineResponse,
        InlineCommandController, InlineCommand> implements InlineCommandDelegator {
    public DefaultInlineCommandDelegator(ImmutableMap<String, InlineCommand> registry) {
        super(registry);
    }
}
