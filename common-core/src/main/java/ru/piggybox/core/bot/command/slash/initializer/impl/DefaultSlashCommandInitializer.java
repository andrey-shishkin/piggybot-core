package ru.piggybox.core.bot.command.slash.initializer.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.piggybox.core.bot.command.slash.controller.SlashCommandController;
import ru.piggybox.core.bot.command.slash.custom.command.CustomHelpCommand;
import ru.piggybox.core.bot.common.SlashBotCommand;
import ru.piggybox.core.bot.common.command.mapping.annotation.SlashQueryMapping;
import ru.piggybox.core.bot.common.dto.BotResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class DefaultSlashCommandInitializer {

    @Autowired(required = false)
    private List<SlashCommandController> controllers;

    public void init(TelegramLongPollingCommandBot bot) throws TelegramApiException {
        Set<SlashBotCommand> commands = new TreeSet<>();

        // Scanning controllers to find SlashQueryMapping
        if (CollectionUtils.isNotEmpty(controllers)) {
            controllers
                    .stream()
                    .map(ctrl -> createBotCommands(ctrl, bot))
                    .flatMap(Set::stream)
                    .forEach(commands::add);
        }

        commands.add(getHelpCommand(commands));

        // Initializing BotCommands for the main menu
        List<BotCommand> mainMenu = commands.stream()
                .filter(SlashBotCommand::isVisible)
                .map(s -> new BotCommand(s.getCommandIdentifier(), s.getDescription()))
                .collect(Collectors.toList());

        // Registering commands
        commands.forEach(bot::register);

        // Registering main menu
        // May throw TelegramApiException. We don't catch it, because we need to crash on startup.
        bot.execute(new SetMyCommands(mainMenu, null, null));
    }

    protected Set<SlashBotCommand> createBotCommands(SlashCommandController controller, TelegramLongPollingCommandBot bot) {
        return Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(SlashQueryMapping.class))
                .map(m -> {
                    SlashQueryMapping annotation = m.getAnnotation(SlashQueryMapping.class);
                    String command = annotation.value();
                    String description = annotation.description();
                    String extendedDescription = annotation.extendedDescription();
                    int sequencePriority = annotation.sequencePriority();
                    boolean visibility = annotation.visibility();
                    m.setAccessible(true);

                    return new SlashBotCommand(command, description, extendedDescription, sequencePriority, visibility,
                            (request) -> {
                                try {
                                    BotResponse response = (BotResponse) m.invoke(controller, request); // TODO !!! Need to put not only user here.
                                    if (response != null) {
                                        bot.execute(response.getMethod());
                                    }
                                } catch (IllegalAccessException | InvocationTargetException | TelegramApiException e) {
                                    e.printStackTrace(); //TODO Fix it
                                }
                                return null;
                            });
                })
                .collect(Collectors.toSet());
    }

    protected SlashBotCommand getHelpCommand(Set<SlashBotCommand> allSequentialCommands) {

        HelpCommand helpCommand = new CustomHelpCommand(allSequentialCommands);

        return new SlashBotCommand(helpCommand.getCommandIdentifier(), helpCommand.getDescription(),
                helpCommand.getExtendedDescription(), Integer.MAX_VALUE, true, (request) -> {
            helpCommand.execute(request.getAbsSender(), request.getUser(), request.getChat(), request.getArguments());
            return null;
        });
    }
}
