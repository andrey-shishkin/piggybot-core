package ru.piggybox.core.bot.common;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.function.Function;

public class SequentialBotCommand extends BotCommand implements Comparable<SequentialBotCommand> {

    private int sequencePriority;
    private Function<User, Void> function;

    public SequentialBotCommand(String commandIdentifier, String description, int sequencePriority, Function<User, Void> function) {
        super(commandIdentifier, description);
        this.sequencePriority = sequencePriority;
        this.function = function;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        function.apply(user);
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
