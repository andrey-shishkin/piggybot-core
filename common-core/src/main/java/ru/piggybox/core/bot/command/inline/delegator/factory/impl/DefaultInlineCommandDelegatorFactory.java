package ru.piggybox.core.bot.command.inline.delegator.factory.impl;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.piggybox.core.bot.command.inline.controller.InlineCommandController;
import ru.piggybox.core.bot.command.inline.delegator.DefaultInlineCommandDelegator;
import ru.piggybox.core.bot.command.inline.delegator.InlineCommandDelegator;
import ru.piggybox.core.bot.command.inline.delegator.factory.InlineCommandDelegatorFactory;
import ru.piggybox.core.bot.command.inline.dto.BotInlineRequest;
import ru.piggybox.core.bot.command.inline.dto.BotInlineResponse;
import ru.piggybox.core.bot.command.inline.dto.InlineCommand;
import ru.piggybox.core.bot.common.command.mapping.InlineQueryMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultInlineCommandDelegatorFactory implements InlineCommandDelegatorFactory {

    private InlineCommandDelegator delegator;

    @Autowired
    private List<InlineCommandController> inlineCommandControllers;

    @PostConstruct
    void init() {
        Map<String, InlineCommand> commandsMap = inlineCommandControllers.stream().map(controller -> {

                    Class<? extends InlineCommandController> clazz = controller.getClass();

                    return Arrays.stream(clazz.getDeclaredMethods())
                            .filter(m -> m.isAnnotationPresent(InlineQueryMapping.class))
                            .map(m -> {
                                InlineQueryMapping annotation = m.getAnnotation(InlineQueryMapping.class);
                                String value = annotation.value();
                                m.setAccessible(true);
                                System.out.println("Registered inline mapping for query " + value);
                                return new InlineCommand() {

                                    @Override
                                    public String getCommand() {
                                        return value;
                                    }

                                    @Override
                                    public BotInlineResponse processCommand(BotInlineRequest input) {
                                        try {
                                            return (BotInlineResponse) m.invoke(controller, input);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            throw new RuntimeException("Couldn't perform query " + input.getCommand()); //TODO Add custom exception
                                        }
                                    }
                                };
                            })
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toMap(InlineCommand::getCommand, cmd -> cmd));
        delegator = new DefaultInlineCommandDelegator(ImmutableMap.copyOf(commandsMap));
    }

    @Override
    public InlineCommandDelegator getDelegator() {
        return delegator;
    }
}
