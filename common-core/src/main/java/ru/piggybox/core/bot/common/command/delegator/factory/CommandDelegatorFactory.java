package ru.piggybox.core.bot.common.command.delegator.factory;

import ru.piggybox.core.bot.common.command.delegator.CommandDelegator;

public interface CommandDelegatorFactory<T extends CommandDelegator<?, ?, ?, ?>> {

    T getDelegator();

}
