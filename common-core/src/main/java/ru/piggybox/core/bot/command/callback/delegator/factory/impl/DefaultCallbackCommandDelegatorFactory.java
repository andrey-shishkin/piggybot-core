package ru.piggybox.core.bot.command.callback.delegator.factory.impl;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.piggybox.core.bot.command.callback.controller.CallbackCommandController;
import ru.piggybox.core.bot.command.callback.delegator.CallbackCommandDelegator;
import ru.piggybox.core.bot.command.callback.delegator.DefaultCallbackCommandDelegator;
import ru.piggybox.core.bot.command.callback.delegator.factory.CallbackCommandDelegatorFactory;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackRequest;
import ru.piggybox.core.bot.command.callback.dto.BotCallbackResponse;
import ru.piggybox.core.bot.command.callback.dto.CallbackCommand;
import ru.piggybox.core.bot.common.command.mapping.annotation.CallbackQueryMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultCallbackCommandDelegatorFactory implements CallbackCommandDelegatorFactory {

    private CallbackCommandDelegator delegator;

    @Autowired(required = false)
    private List<CallbackCommandController> callbackCommandControllers;

    @PostConstruct
    void init() {
        Map<String, CallbackCommand> commandsMap;
        if (CollectionUtils.isNotEmpty(callbackCommandControllers)) {
            commandsMap = callbackCommandControllers.stream().map(controller -> {

                        Class<? extends CallbackCommandController> clazz = controller.getClass();

                        return Arrays.stream(clazz.getDeclaredMethods())
                                .filter(m -> m.isAnnotationPresent(CallbackQueryMapping.class))
                                .map(m -> {
                                    CallbackQueryMapping annotation = m.getAnnotation(CallbackQueryMapping.class);
                                    String value = annotation.value();
                                    m.setAccessible(true);
                                    System.out.println("Registered callback mapping for query " + value);
                                    return new CallbackCommand() {

                                        @Override
                                        public String getCommand() {
                                            return value;
                                        }

                                        @Override
                                        public BotCallbackResponse processCommand(BotCallbackRequest input) {
                                            try {
                                                return (BotCallbackResponse) m.invoke(controller, input);
                                            } catch (IllegalAccessException | InvocationTargetException e) {
                                                throw new RuntimeException("Couldn't perform query " + input.getCommand()); //TODO Add custom exception
                                            }
                                        }
                                    };
                                })
                                .collect(Collectors.toList());
                    })
                    .flatMap(List::stream)
                    .collect(Collectors.toMap(CallbackCommand::getCommand, cmd -> cmd));
        } else {
            commandsMap = Collections.emptyMap();
            System.out.println("INFO: Callback mappings not found");
        }

        delegator = new DefaultCallbackCommandDelegator(ImmutableMap.copyOf(commandsMap));
    }

    @Override
    public CallbackCommandDelegator getDelegator() {
        return delegator;
    }
}
