package ru.piggybox.core.bot.common.command;

import ru.piggybox.core.bot.common.controller.CommandController;

public interface BotCommand<I, O, T extends CommandController<I, O>> {

    String getCommand();

    T getOwner();

    O processCommand(I input);
}
