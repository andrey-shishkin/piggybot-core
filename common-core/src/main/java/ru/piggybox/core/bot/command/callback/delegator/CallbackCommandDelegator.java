package ru.piggybox.core.bot.command.callback.delegator;

import ru.piggybox.core.bot.command.callback.dto.CallbackCommand;
import ru.piggybox.core.bot.command.callback.controller.CallbackCommandController;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackRequest;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackResponse;
import ru.piggybox.core.bot.common.command.delegator.CommandDelegator;

public interface CallbackCommandDelegator extends CommandDelegator<BotCallbackRequest, BotCallbackResponse,
        CallbackCommandController, CallbackCommand> {

    BotCallbackResponse delegate(BotCallbackRequest request);

}
