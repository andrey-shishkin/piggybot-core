package ru.piggybox.core.bot.command.inline.dto;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.piggybox.core.bot.common.dto.BotRequest;

@Data
@Builder
public class BotInlineRequest implements BotRequest {

    private String command;
    private String parameters;
    private Update update;
}
