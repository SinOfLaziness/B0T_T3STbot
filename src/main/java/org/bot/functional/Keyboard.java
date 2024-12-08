package org.bot.functional;

import com.vdurmont.emoji.EmojiParser;
import org.bot.msg.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    private ReplyKeyboardMarkup createKeyboard(List<String> buttons) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (String button : buttons) {
            row.add(EmojiParser.parseToUnicode(button));
        }
        keyboardRowList.add(row);
        replyKeyboard.setKeyboard(keyboardRowList);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);
        return replyKeyboard;
    }

    public ReplyKeyboardMarkup generateGeneralKeyboard() {
        List<String> buttons = new ArrayList<>();
        buttons.add(Constants.SET_EXP);
        buttons.add(Constants.SEND_EXP);
        return createKeyboard(buttons);
    }
    
}
