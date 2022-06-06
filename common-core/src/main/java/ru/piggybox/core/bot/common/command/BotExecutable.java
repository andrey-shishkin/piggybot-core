package ru.piggybox.core.bot.common.command;

public interface BotExecutable<I, O> {

    String getCommand();

    O processCommand(I input);
}
