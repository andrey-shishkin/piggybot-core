package ru.piggybox.core.bot.command.callback.dto;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.piggybox.core.bot.common.dto.BotRequest;

@Data
@Builder
public class BotCallbackRequest implements BotRequest {

    private String command;
    private User user;
}
