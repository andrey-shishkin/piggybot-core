package ru.piggybox.core.bot.command.inline.delegator;

import ru.piggybox.core.bot.command.inline.dto.InlineCommand;
import ru.piggybox.core.bot.command.inline.controller.InlineCommandController;
import ru.piggybox.core.bot.command.inline.dto.BotInlineRequest;
import ru.piggybox.core.bot.command.inline.dto.BotInlineResponse;
import ru.piggybox.core.bot.common.command.delegator.CommandDelegator;

public interface InlineCommandDelegator extends CommandDelegator<BotInlineRequest, BotInlineResponse,
        InlineCommandController, InlineCommand> {

    BotInlineResponse delegate(BotInlineRequest request);

}
