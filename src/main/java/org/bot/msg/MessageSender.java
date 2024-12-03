package org.bot.msg;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.File;

public class MessageSender {
    private final SendMessage newMessage;
    private final SendPhoto newPhoto;
    private final TelegramLongPollingBot bot;

    public MessageSender(TelegramLongPollingBot bot) {
        newMessage = new SendMessage();
        newPhoto = new SendPhoto();
        this.bot = bot;
    }

    public void send(long chatID, Message message) {
        newMessage.setChatId(String.valueOf(chatID));
        newMessage.setText(message.answerToSend());
        if (message.replyKeyboard() != null) {
            newMessage.setReplyMarkup(message.replyKeyboard());
        }
        if (message.inlineButtons() != null) {
            newMessage.setReplyMarkup(message.inlineButtons());
        }
        try {
            bot.execute(newMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhoto(long chatID, byte[] file, String text) {
        newPhoto.setChatId(String.valueOf(chatID));
        newPhoto.setPhoto(new InputFile(new ByteArrayInputStream(file), "1.jpg"));
        newPhoto.setCaption(text);
        try {
            bot.execute(newPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
