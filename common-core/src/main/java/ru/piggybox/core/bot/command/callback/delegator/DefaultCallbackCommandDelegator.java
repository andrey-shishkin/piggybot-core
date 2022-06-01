package ru.piggybox.core.bot.command.callback.delegator;

import com.google.common.collect.ImmutableMap;
import ru.piggybox.core.bot.command.callback.dto.CallbackCommand;
import ru.piggybox.core.bot.command.callback.controller.CallbackCommandController;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackRequest;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackResponse;
import ru.piggybox.core.bot.common.command.delegator.GenericCommandDelegator;

public class DefaultCallbackCommandDelegator extends GenericCommandDelegator<BotCallbackRequest, BotCallbackResponse,
        CallbackCommandController, CallbackCommand> implements CallbackCommandDelegator {
    public DefaultCallbackCommandDelegator(ImmutableMap<String, CallbackCommand> registry) {
        super(registry);
    }
}
