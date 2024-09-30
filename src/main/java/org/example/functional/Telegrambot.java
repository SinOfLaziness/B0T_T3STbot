package org.example.functional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class Telegrambot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    public String getBotUsername() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            long chatID = update.getMessage().getChatId();
            String sourceText = update.getMessage().getText();

            switch (sourceText){
                case "/start": {
                    sendStartCommandAnswer(chatID, update.getMessage().getChat().getFirstName());
                    break;
                }
                default:
                    sendIfUnknownCommand(chatID);
            }
            System.out.println("Hello");
        }
    }

    private void sendStartCommandAnswer(long chatID, String name){
        String answerToSend = "Приветствую тебя, дорогой" + name + ". Чем я могу тебе помочь?";
        sendMessage(chatID, answerToSend);
    }
    private void sendIfUnknownCommand(long chatID) {
        String answerToSend = "Извини, такую команду я не знаю. Напиши /help, чтобы увидеть полный список команд";
        sendMessage(chatID, answerToSend);
    }
    private void sendMessage (long chatID, String answerToSend){
        SendMessage newMessage = new SendMessage();
        newMessage.setChatId(String.valueOf(chatID));
        newMessage.setText(answerToSend);
    }

}