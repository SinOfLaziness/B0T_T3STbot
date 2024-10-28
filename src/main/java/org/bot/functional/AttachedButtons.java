package org.bot.functional;

import org.bot.database.ConstantDB;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttachedButtons {
    public enum Button {
        HOME_AND_RENOVATION(ConstantDB.HOME_AND_RENOVATION, ConstantDB.USERS_HOME_AND_RENOVATION),
        TRANSPORT(ConstantDB.TRANSPORT, ConstantDB.USERS_TRANSPORT),
        FOOD(ConstantDB.FOOD, ConstantDB.USERS_FOOD),
        ENTERTAINMENT(ConstantDB.ENTERTAINMENT, ConstantDB.USERS_ENTERTAINMENT),
        PHARMACIES(ConstantDB.PHARMACIES, ConstantDB.USERS_PHARMACIES),
        COSMETICS(ConstantDB.COSMETICS, ConstantDB.USERS_COSMETICS),
        ITEMS_OF_CLOTHING(ConstantDB.ITEMS_OF_CLOTHING, ConstantDB.USERS_ITEMS_OF_CLOTHING),
        SUPERMARKETS(ConstantDB.SUPERMARKETS, ConstantDB.USERS_SUPERMARKETS),
        SOUVENIRS(ConstantDB.SOUVENIRS, ConstantDB.USERS_SOUVENIRS),
        ELECTRONICS_AND_TECHNOLOGY(ConstantDB.ELECTRONICS_AND_TECHNOLOGY, ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY),
        BOOKS(ConstantDB.BOOKS, ConstantDB.USERS_BOOKS);

        private final String text;
        private final String callbackData;

        Button(String text, String callbackData) {
            this.text = text;
            this.callbackData = callbackData;
        }

        public String getText() {
            return text;
        }

        public String getCallbackData() {
            return callbackData;
        }
    }

    private InlineKeyboardMarkup createButtons(List<Button> buttons) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Button button : buttons) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
            inlineButton.setText(button.getText());
            inlineButton.setCallbackData(button.getCallbackData());
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
        List<Button> buttons = Arrays.asList(Button.values());
        return createButtons(buttons);
    }

}
