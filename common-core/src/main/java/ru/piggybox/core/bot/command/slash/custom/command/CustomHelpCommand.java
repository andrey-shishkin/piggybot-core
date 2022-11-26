package ru.piggybox.core.bot.command.slash.custom.command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.piggybox.core.bot.common.SlashBotCommand;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomHelpCommand extends HelpCommand {

    private Set<SlashBotCommand> botCommands;

    public CustomHelpCommand(Set<SlashBotCommand> botCommands) {
        super("help", "Need help ?", null);
        this.botCommands = botCommands;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (ICommandRegistry.class.isInstance(absSender)) {
            ICommandRegistry registry = (ICommandRegistry) absSender;

            if (arguments.length > 0) {
                SlashBotCommand command = (SlashBotCommand) registry.getRegisteredCommand(arguments[0]);
                if (command != null && command.isVisible()) {
                    String reply = getManText((IBotCommand) command);
                    try {
                        absSender.execute(SendMessage.builder().chatId(chat.getId().toString()).text(reply).parseMode("HTML").build());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Collection<IBotCommand> registeredCommands = registry.getRegisteredCommands();
                Collection<IBotCommand> filteredCommands = new HashSet<>();
                for (IBotCommand c : registeredCommands) {
                    if (((SlashBotCommand) c).isVisible()) {
                        filteredCommands.add(c);
                    }
                }
                String reply = getHelpText(filteredCommands);
                try {
                    absSender.execute(SendMessage.builder().chatId(chat.getId().toString()).text(reply).parseMode("HTML").build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}