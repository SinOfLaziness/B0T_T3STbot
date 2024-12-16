package com.github.FinancialAssistant.functional;

import com.github.FinancialAssistant.msg.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    private ReplyKeyboardMarkup createKeyboard(List<String> buttons) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (int i = 0; i < buttons.size(); i++) {
            row.add(EmojiParser.parseToUnicode(buttons.get(i)));
            if ((i + 1) % 2 == 0 || i == buttons.size() - 1) {
                keyboardRowList.add(row);
                row = new KeyboardRow();
            }
        }
        replyKeyboard.setKeyboard(keyboardRowList);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);
        return replyKeyboard;
    }

    public ReplyKeyboardMarkup generateGeneralKeyboard() {
        List<String> buttons = new ArrayList<>();
        buttons.add(Constants.SET_EXP);
        buttons.add(Constants.SEND_EXP);
        buttons.add(Constants.SET_INCOME);
        buttons.add(Constants.SEND_INCOME);
        buttons.add(Constants.COMPARE);
        return createKeyboard(buttons);
    }
}
