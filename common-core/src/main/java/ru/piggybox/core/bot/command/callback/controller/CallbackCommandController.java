package ru.piggybox.core.bot.command.callback.controller;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackRequest;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackResponse;
import ru.piggybox.core.bot.common.controller.CommandController;

import java.io.Serializable;

public interface CallbackCommandController extends CommandController<BotCallbackRequest, BotCallbackResponse> {

    default BotCallbackResponse buildResponse(String command, BotApiMethod<? extends Serializable> method) {
        return new BotCallbackResponse() {
            @Override
            public String getCommand() {
                return command;
            }

            @Override
            public BotApiMethod<? extends Serializable> getMethod() {
                return method;
            }
        };
    }
}
