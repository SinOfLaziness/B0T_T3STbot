package org.bot.msg;

import org.bot.functional.Keyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


public record Message (String answerToSend, ReplyKeyboardMarkup replyKeyboard, InlineKeyboardMarkup inlineButtons) {
    public Message(String answerToSend){
        this(answerToSend, new Keyboard().generateGeneralKeyboard(),null);
    }
    public Message(String answerToSend,ReplyKeyboardMarkup replyKeyboard){
        this(answerToSend,replyKeyboard,null);
    }
    public Message(String answerToSend,InlineKeyboardMarkup inlineButtons){
        this(answerToSend,null, inlineButtons);
    }
}
