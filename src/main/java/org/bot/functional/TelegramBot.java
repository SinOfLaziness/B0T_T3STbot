package org.bot.functional;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;

public class TelegramBot extends TelegramLongPollingBot {

    private final String botName = System.getenv("botname");
    private final String botToken = System.getenv("bottoken");
    private final UpdateHandler updateHandler;

    public TelegramBot() {
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
        try {
            updateHandler.handleUpdate(update);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}