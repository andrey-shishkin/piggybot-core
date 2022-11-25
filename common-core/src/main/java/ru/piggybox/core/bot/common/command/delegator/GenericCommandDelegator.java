package ru.piggybox.core.bot.common.command.delegator;

import com.google.common.collect.ImmutableMap;
import ru.piggybox.core.bot.common.command.BotExecutable;
import ru.piggybox.core.bot.common.command.mapping.exception.CommandMappingException;
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
        String requestCommand = request.getCommand();
        if (requestCommand == null) {
            throw new CommandMappingException("Command can't be null");
        }
        Cmd command = Optional.of(registry.get(requestCommand))
                .orElseThrow(() -> new CommandMappingException("Can't delegate command " + requestCommand + ". Mapping is not found."));
        return command.processCommand(request);
    }
}
