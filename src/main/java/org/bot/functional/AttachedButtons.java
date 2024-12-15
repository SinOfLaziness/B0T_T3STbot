package org.bot.functional;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttachedButtons {

    private InlineKeyboardMarkup createButtons(Map<String, String> buttonMap) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Map.Entry<String, String> entry : buttonMap.entrySet()) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
            inlineButton.setText(EmojiParser.parseToUnicode(entry.getValue()));
            inlineButton.setCallbackData(EmojiParser.parseToUnicode(entry.getKey()));
            row.add(inlineButton);
            if (row.size() == 2) {
                keyboardRowList.add(row);
                row = new ArrayList<>();
            }
        }
        if (!row.isEmpty()) {
            keyboardRowList.add(row);
        }
        inlineKeyboard.setKeyboard(keyboardRowList);
        return inlineKeyboard;
    }

    public InlineKeyboardMarkup createButtonsForExpenses() {
        Map<String, String> buttonMap = ButtonConfig.getExpensesButtonMap();
        return createButtons(buttonMap);
    }

    public InlineKeyboardMarkup createButtonsForIncome() {
        Map<String, String> buttonMap = ButtonConfig.getIncomeButtonMap();
        return createButtons(buttonMap);
    }

    public InlineKeyboardMarkup createButtonsForRegistration() {
        Map<String, String> buttonMap = ButtonConfig.getRegistrationButtonMap();
        return createButtons(buttonMap);
    }

    public InlineKeyboardMarkup createButtonsForPeriodFormat() {
        Map<String, String> buttonMap = ButtonConfig.getPeriodFormatButtonMap();
        return createButtons(buttonMap);
    }
}