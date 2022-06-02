package ru.piggybox.core.bot.common.command.delegator;

import ru.piggybox.core.bot.common.command.BotCommand;
import ru.piggybox.core.bot.common.controller.CommandController;

public interface CommandDelegator<I, O, T extends CommandController<I, O>, C extends BotCommand<I, O>> {

    O delegate(I request);

}
