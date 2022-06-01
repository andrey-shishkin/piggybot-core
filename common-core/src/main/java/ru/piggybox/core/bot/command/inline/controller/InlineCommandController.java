package ru.piggybox.core.bot.command.inline.controller;

import ru.piggybox.core.bot.command.inline.dto.BotInlineRequest;
import ru.piggybox.core.bot.command.inline.dto.BotInlineResponse;
import ru.piggybox.core.bot.common.controller.CommandController;

public interface InlineCommandController extends CommandController<BotInlineRequest, BotInlineResponse> {
}
