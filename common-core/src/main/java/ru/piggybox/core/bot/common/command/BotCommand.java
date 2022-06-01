package ru.piggybox.core.bot.common.command;

import ru.piggybox.core.bot.common.controller.CommandController;

public interface BotCommand<I, O> {

    String getCommand();

    O processCommand(I input);
}
