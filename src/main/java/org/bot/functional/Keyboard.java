package org.bot.functional;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    public ReplyKeyboardMarkup generateGeneralKeyboard() {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Записать расходы");
        row.add("Вывести список расходов");
        keyboardRowList.add(row);
        replyKeyboard.setKeyboard(keyboardRowList);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);
        return replyKeyboard;
    }
    public ReplyKeyboardMarkup generateStartKeyboard() {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Зарегистрироваться");
        row.add("Список команд");
        keyboardRowList.add(row);
        replyKeyboard.setKeyboard(keyboardRowList);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);
        return replyKeyboard;
    }
}