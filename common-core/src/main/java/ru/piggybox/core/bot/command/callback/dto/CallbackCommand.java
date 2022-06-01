package ru.piggybox.core.bot.command.callback.dto;

import ru.piggybox.core.bot.command.callback.controller.CallbackCommandController;
import ru.piggybox.core.bot.common.command.BotCommand;

public interface CallbackCommand extends BotCommand<BotCallbackRequest, BotCallbackResponse, CallbackCommandController> {
}
