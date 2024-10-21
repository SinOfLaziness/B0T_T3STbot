package org.bot.functional;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    private ReplyKeyboardMarkup createKeyboard(List<String> buttons) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.addAll(buttons);
        keyboardRowList.add(row);
        replyKeyboard.setKeyboard(keyboardRowList);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);
        return replyKeyboard;
    }

    public ReplyKeyboardMarkup generateGeneralKeyboard() {
        List<String> buttons = new ArrayList<>();
        buttons.add("Записать расходы");
        buttons.add("Вывести список расходов");
        return createKeyboard(buttons);
    }

    public ReplyKeyboardMarkup generateStartKeyboard() {
        List<String> buttons = new ArrayList<>();
        buttons.add("Зарегистрироваться");
        buttons.add("Список команд");
        return createKeyboard(buttons);
    }
}