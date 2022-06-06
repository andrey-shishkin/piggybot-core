package ru.piggybox.core.bot.command.inline.controller;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.piggybox.core.bot.command.inline.dto.BotInlineRequest;
import ru.piggybox.core.bot.command.inline.dto.BotInlineResponse;
import ru.piggybox.core.bot.common.controller.CommandController;

import java.io.Serializable;

public interface InlineCommandController extends CommandController<BotInlineRequest, BotInlineResponse> {

    default BotInlineResponse buildResponse(String command, BotApiMethod<? extends Serializable> method) {
        return new BotInlineResponse() {
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
