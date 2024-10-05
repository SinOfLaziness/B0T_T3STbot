package org.bot.functional;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Telegrambot extends TelegramLongPollingBot {

    private final String botName = System.getenv("botname");
    private final String botToken = System.getenv("bottoken");
    private final UpdateHandler updateHandler;

    public Telegrambot() {
        this.updateHandler = new UpdateHandler(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handleUpdate(update);
    }
}