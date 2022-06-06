package ru.piggybox.core.bot.common;

import lombok.Getter;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.piggybox.core.bot.command.slash.dto.BotSlashRequest;

import java.util.function.Function;

public class SlashBotCommand extends ManCommand implements Comparable<SlashBotCommand> {

    private final int sequencePriority;
    private final Function<BotSlashRequest, Void> function;
    @Getter
    private final boolean isVisible;

    public SlashBotCommand(String commandIdentifier, String description, String extendedDescription, int sequencePriority, boolean isVisible, Function<BotSlashRequest, Void> function) {
        super(commandIdentifier, description, extendedDescription);
        this.sequencePriority = sequencePriority;
        this.isVisible = isVisible;
        this.function = function;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        function.apply(new BotSlashRequest() {
            @Override
            public AbsSender getAbsSender() {
                return absSender;
            }

            @Override
            public Chat getChat() {
                return chat;
            }

            @Override
            public User getUser() {
                return user;
            }

            @Override
            public String[] getArguments() {
                return arguments;
            }
        });
    }

    @Override
    public int compareTo(SlashBotCommand botCommand) {

        // Compare by sequence priority. If it's equals then compare by command letters

        int compareValue = Integer.compare(sequencePriority, botCommand.sequencePriority);
        if (compareValue == 0) {
            return getCommandIdentifier().compareTo(botCommand.getCommandIdentifier());
        } else {
            return compareValue;
        }
    }
}
