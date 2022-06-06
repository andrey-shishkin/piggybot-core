package ru.piggybox.core.bot.common;

import lombok.Getter;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SequentialBotCommand extends ManCommand implements Comparable<SequentialBotCommand> {

    private final int sequencePriority;
    private final Function<List<Object>, Void> function;
    @Getter
    private final boolean isVisible;

    public SequentialBotCommand(String commandIdentifier, String description, String extendedDescription, int sequencePriority, boolean isVisible, Function<List<Object>, Void> function) {
        super(commandIdentifier, description, extendedDescription);
        this.sequencePriority = sequencePriority;
        this.isVisible = isVisible;
        this.function = function;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        List<Object> input = new ArrayList<>();
        input.add(absSender);
        input.add(user);
        input.add(chat);
        input.add(arguments);
        function.apply(input);
    }

    @Override
    public int compareTo(SequentialBotCommand botCommand) {

        // Compare by sequence priority. If it's equals then compare by command letters

        int compareValue = Integer.compare(sequencePriority, botCommand.sequencePriority);
        if (compareValue == 0) {
            return getCommandIdentifier().compareTo(botCommand.getCommandIdentifier());
        } else {
            return compareValue;
        }
    }
}
