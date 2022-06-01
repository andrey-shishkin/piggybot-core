package ru.piggybox.core.bot.common.dto;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.io.Serializable;

public interface BotResponse {
    BotApiMethod<? extends Serializable> getMethod();
}
