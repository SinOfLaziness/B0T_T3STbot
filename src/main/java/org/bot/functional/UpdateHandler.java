package org.bot.functional;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.bot.database.DatabaseHandler;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateHandler {

    private final Keyboard keyboard = new Keyboard();
    private final TelegramLongPollingBot bot;

    public UpdateHandler(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatID = update.getMessage().getChatId();
            String sourceText = update.getMessage().getText();
            switch (sourceText) {
                case "/start":
                    sendStartCommandAnswer(chatID, update.getMessage().getChat().getFirstName());
                    break;
                case "Список команд":
                    sendHelpMessage(chatID);
                    break;
                case "Зарегистрироваться":
                    caseSignUpUsers(chatID);
                    break;
                default:
                    sendIfUnknownCommand(chatID);
            }
        }
    }

    private void sendStartCommandAnswer(long chatID, String name) {
        if (checkIfSigned(chatID))
            return;
        String answerToSend = "Приветствую тебя, " + name + ". Перед началом пользования прошу тебя зарегистрироваться. Для этого напиши команду Зарегистрироваться";
        sendMessage(chatID, answerToSend, keyboard.generateStartKeyboard());
    }

    private void sendIfUnknownCommand(long chatID) {
        String answerToSend = "Извини, такую команду я не знаю. Напиши Список команд, чтобы увидеть полный список команд";
        sendMessage(chatID, answerToSend);
    }

    private void sendHelpMessage(long chatID) {
        String answerToSend = "Функционал бота \n\n/start - начинает работу с ботом \nСписок команд - выводит список доступных команд\nЗарегистрироваться - регистрирует пользователя";
        sendMessage(chatID, answerToSend, keyboard.generateStartKeyboard());
    }

    private void sendMessage(long chatID, String answerToSend) {
        sendMessage(chatID, answerToSend, null);
    }

    private void sendMessage(long chatID, String answerToSend, ReplyKeyboardMarkup replyKeyboard) {
        SendMessage newMessage = new SendMessage();
        newMessage.setChatId(String.valueOf(chatID));
        newMessage.setText(answerToSend);
        if (replyKeyboard != null) {
            newMessage.setReplyMarkup(replyKeyboard);
        }
        try {
            bot.execute(newMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void caseSignUpUsers(long chatID) {
        if (checkIfSigned(chatID))
            return;
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.signUpUser(String.valueOf(chatID));
        String answerToSend = "Поздравляю! Теперь, ты можешь пользоваться всеми моими полезными штуками!";
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard());
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
            sendMessage(chatID, "Ты уже зарегистрирован, можешь продолжить свою работу", keyboard.generateGeneralKeyboard());
            return true;
        }
        return false;
    }
}