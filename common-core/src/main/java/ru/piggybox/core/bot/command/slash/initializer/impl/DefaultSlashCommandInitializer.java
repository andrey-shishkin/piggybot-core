package ru.piggybox.core.bot.command.slash.initializer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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
    public void init() {
        Set<SequentialBotCommand> commands = new TreeSet<>();
        controllers
                .stream()
                .map(this::createBotCommands)
                .flatMap(Set::stream)
                .forEach(commands::add);

        commands.forEach(c -> bot.register(c));
        bot.register(new HelpCommand());
    }

    private Set<SequentialBotCommand> createBotCommands(CommandController<?, ?> controller) {
        return Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(SlashQueryMapping.class))
                .map(m -> {
                    SlashQueryMapping annotation = m.getAnnotation(SlashQueryMapping.class);
                    String command = annotation.value();
                    String description = annotation.description();
                    int sequencePriority = annotation.sequencePriority();
                    m.setAccessible(true);

                    return new SequentialBotCommand(command, description, sequencePriority,
                            (user) -> {
                                try {
                                    BotResponse response = (BotResponse) m.invoke(controller, user); // TODO !!! Need to put not only user here.
                                    bot.execute(response.getMethod());
                                } catch (IllegalAccessException | InvocationTargetException | TelegramApiException e) {
                                    e.printStackTrace(); //TODO Fix it
                                }
                                return null;
                            });

                    /*return new BotCommand(command, description) {
                        @Override
                        public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
                            try {
                                m.invoke(controller, user);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace(); //TODO Fix it
                            }
                        }
                    };*/
                })
                .collect(Collectors.toSet());
    }
}
