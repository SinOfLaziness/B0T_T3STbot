package org.example.functional;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Telegrambot extends TelegramLongPollingBot {

    private final String botName=System.getenv("botname");
    private final String botToken=System.getenv("bottoken");

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
                default:
                    sendIfUnknownCommand(chatID);
            }
        }
    }

    private void sendStartCommandAnswer(long chatID, String name) {
        String answerToSend = "Приветствую тебя, " + name + ". Чем я могу тебе помочь?";
        sendMessage(chatID, answerToSend);
    }

    private void sendIfUnknownCommand(long chatID) {
        String answerToSend = "Извини, такую команду я не знаю. Напиши /help, чтобы увидеть полный список команд";
        sendMessage(chatID, answerToSend);
    }
    private void sendHelpMessage(long chatID) {
        String answerToSend =  "Функционал бота \n\n/start - начинает работу с ботом \n/help - выводит список доступных команд";
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
}