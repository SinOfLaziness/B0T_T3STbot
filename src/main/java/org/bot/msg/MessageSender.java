package org.bot.msg;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSender {
    private final SendMessage newMessage;
    private final TelegramLongPollingBot bot;

    public MessageSender(TelegramLongPollingBot bot) {
        newMessage = new SendMessage();
        this.bot = bot;
    }

    public void send (long chatID, Message message){
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

}
