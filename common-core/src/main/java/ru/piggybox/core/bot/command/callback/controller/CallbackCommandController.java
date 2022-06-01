package ru.piggybox.core.bot.command.callback.controller;

import ru.piggybox.core.bot.command.callback.dto.BotCallbackRequest;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackResponse;
import ru.piggybox.core.bot.common.controller.CommandController;

public interface CallbackCommandController extends CommandController<BotCallbackRequest, BotCallbackResponse> {
}
