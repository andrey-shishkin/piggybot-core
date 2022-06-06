package ru.piggybox.core.bot.common.command.delegator;

import ru.piggybox.core.bot.common.command.BotExecutable;
import ru.piggybox.core.bot.common.controller.CommandController;

public interface CommandDelegator<I, O, T extends CommandController<I, O>, C extends BotExecutable<I, O>> {

    O delegate(I request);

}
