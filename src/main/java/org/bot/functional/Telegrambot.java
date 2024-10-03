package org.bot.functional;

import org.bot.database.Const;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.bot.database.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Telegrambot extends TelegramLongPollingBot {

    private final String botName = System.getenv("botname");
    private final String botToken = System.getenv("bottoken");

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatID = update.getMessage().getChatId();
            String sourceText = update.getMessage().getText();
            switch (sourceText) {
                case "/start":
                    sendStartCommandAnswer(chatID, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendHelpMessage(chatID);
                    break;
                case "/sign":
                    caseSignUpUsers(chatID);
                    break;
                default:
                    sendIfUnknownCommand(chatID);
            }
        }
    }

    private void sendStartCommandAnswer(long chatID, String name) {
        if (checkIfSigned(chatID)) {
            return;
        }
        String answerToSend = "Приветствую тебя, " + name + ". Перед началом пользования прошу тебя зарегистрироваться. Для этого напиши команду /sign";
        sendMessage(chatID, answerToSend);
    }

    private void sendIfUnknownCommand(long chatID) {
        String answerToSend = "Извини, такую команду я не знаю. Напиши /help, чтобы увидеть полный список команд";
        sendMessage(chatID, answerToSend);
    }

    private void sendHelpMessage(long chatID) {
        String answerToSend = "Функционал бота \n\n/start - начинает работу с ботом \n/help - выводит список доступных команд\n/sign - регистрирует пользователя";
        sendMessage(chatID, answerToSend);
    }

    private void sendMessage(long chatID, String answerToSend) {
        SendMessage newMessage = new SendMessage();
        newMessage.setChatId(String.valueOf(chatID));
        newMessage.setText(answerToSend);
        try {
            this.execute(newMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void caseSignUpUsers(long chatID) {
        if (checkIfSigned(chatID)) {
            return;
        }
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.signUpUser(String.valueOf(chatID));
        String answerToSend = "Поздравляю! Теперь, ты можешь пользоваться всеми моими полезными штуками!";
        sendMessage(chatID, answerToSend);
    }

    private boolean checkIfSigned(long chatID) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getUser(String.valueOf(chatID));
        int counter = 0;
        try {
            if (result != null) {
                while (result.next()) {
                    counter++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (counter >= 1) {
            System.out.println(counter);
            sendMessage(chatID, "Ты уже зарегистрирован, можешь продолжить свою работу");
            return true;
        }
        return false;
    }
}
