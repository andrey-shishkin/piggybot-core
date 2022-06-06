package ru.piggybox.core.bot.common.command.delegator;

import com.google.common.collect.ImmutableMap;
import ru.piggybox.core.bot.common.command.BotExecutable;
import ru.piggybox.core.bot.common.controller.CommandController;
import ru.piggybox.core.bot.common.dto.BotRequest;
import ru.piggybox.core.bot.common.dto.BotResponse;

import java.util.Optional;

public abstract class GenericCommandDelegator<I extends BotRequest, O extends BotResponse,
        T extends CommandController<I, O>, Cmd extends BotExecutable<I, O>> implements CommandDelegator<I, O, T, Cmd> {

    protected final ImmutableMap<String, Cmd> registry;

    protected GenericCommandDelegator(ImmutableMap<String, Cmd> registry) {
        this.registry = registry;
    }

    @Override
    public O delegate(I request) {
        Cmd command = Optional.of(registry.get(request.getCommand()))
                .orElseThrow(IllegalArgumentException::new); // TODO Throw custom exception (Unknown command exception)
        return command.processCommand(request);
    }
}
