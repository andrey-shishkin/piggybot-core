package ru.piggybox.core.bot.command.slash.initializer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.piggybox.core.bot.command.slash.custom.command.CustomHelpCommand;
import ru.piggybox.core.bot.common.SequentialBotCommand;
import ru.piggybox.core.bot.common.command.mapping.annotation.SlashQueryMapping;
import ru.piggybox.core.bot.common.controller.CommandController;
import ru.piggybox.core.bot.common.dto.BotResponse;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class DefaultSlashCommandInitializer {

    @Autowired
    private TelegramLongPollingCommandBot bot;

    @Autowired(required = false)
    private List<CommandController<?, ?>> controllers;

    @PostConstruct
    protected void init() throws TelegramApiException {
        Set<SequentialBotCommand> commands = new TreeSet<>();

        // Scanning controllers to find SlashQueryMapping
        controllers
                .stream()
                .map(this::createBotCommands)
                .flatMap(Set::stream)
                .forEach(commands::add);

        commands.add(getHelpCommand(commands));

        // Initializing BotCommands for the main menu
        List<BotCommand> mainMenu = commands.stream()
                .filter(SequentialBotCommand::isVisible)
                .map(s -> new BotCommand(s.getCommandIdentifier(), s.getDescription()))
                .collect(Collectors.toList());

        // Registering commands
        commands.forEach(c -> bot.register(c));

        /*// Creating default /help command
        HelpCommand helpCommand = new HelpCommand();
        // Registering default /help command
        bot.register(helpCommand);

        // Add help command to the main menu
        mainMenu.add(new BotCommand(helpCommand.getCommandIdentifier(), helpCommand.getDescription()));*/

        // Registering main menu
        bot.execute(new SetMyCommands(mainMenu, null, null)); // May throw TelegramApiException. We don't catch it, otherwise we crashing on startup.
    }

    protected Set<SequentialBotCommand> createBotCommands(CommandController<?, ?> controller) {
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

                    return new SequentialBotCommand(command, description, extendedDescription, sequencePriority, visibility,
                            (input) -> {
                                try {
                                    BotResponse response = (BotResponse) m.invoke(controller, input); // TODO !!! Need to put not only user here.
                                    bot.execute(response.getMethod());
                                } catch (IllegalAccessException | InvocationTargetException | TelegramApiException e) {
                                    e.printStackTrace(); //TODO Fix it
                                }
                                return null;
                            });
                })
                .collect(Collectors.toSet());
    }

    protected SequentialBotCommand getHelpCommand(Set<SequentialBotCommand> allSequentialCommands) {

        HelpCommand helpCommand = new CustomHelpCommand(allSequentialCommands);

        return new SequentialBotCommand(helpCommand.getCommandIdentifier(), helpCommand.getDescription(),
                helpCommand.getExtendedDescription(), Integer.MAX_VALUE, true, (input) -> {
            AbsSender absSender = (AbsSender) input.get(0);
            User user = (User) input.get(1);
            Chat chat = (Chat) input.get(2);
            String[] arguments = (String[]) input.get(3);
            helpCommand.execute(absSender, user, chat, arguments);
            return null;
        });
    }
}