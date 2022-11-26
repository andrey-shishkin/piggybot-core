package ru.piggybox.core.bot.command.slash.controller;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.piggybox.core.bot.command.slash.dto.BotSlashRequest;
import ru.piggybox.core.bot.command.slash.dto.BotSlashResponse;
import ru.piggybox.core.bot.common.controller.CommandController;

import java.io.Serializable;

public interface SlashCommandController extends CommandController<BotSlashRequest, BotSlashResponse> {

    default BotSlashResponse buildResponse(BotSlashRequest request, String commandMenu, String messageText, InlineKeyboardMarkup markup) {
        return new BotSlashResponse() {
            @Override
            public String getCommand() {
                return commandMenu;
            }

            @Override
            public BotApiMethod<? extends Serializable> getMethod() {
                return SendMessage.builder()
                        .text(messageText)
                        .chatId(request.getUser().getId().toString())
                        .replyMarkup(markup)
                        .build();
            }
        };
    }

}
