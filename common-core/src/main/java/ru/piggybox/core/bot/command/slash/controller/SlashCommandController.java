package ru.piggybox.core.bot.command.slash.controller;

import ru.piggybox.core.bot.command.slash.dto.BotSlashRequest;
import ru.piggybox.core.bot.command.slash.dto.BotSlashResponse;
import ru.piggybox.core.bot.common.controller.CommandController;

public interface SlashCommandController extends CommandController<BotSlashRequest, BotSlashResponse> {
}
