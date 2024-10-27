package org.bot.functional;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttachedButtons {
    public enum Button {
        HOME_AND_RENOVATION("Дом и ремонт", "HomeAndRenovation"),
        TRANSPORT("Транспорт", "Transport"),
        FOOD("Еда", "Food"),
        ENTERTAINMENT("Развлечения", "Entertainment"),
        PHARMACIES("Аптека", "Pharmacies"),
        COSMETICS("Косметика", "Cosmetics"),
        ITEMS_OF_CLOTHING("Предметы одежды", "ItemsOfClothing"),
        SUPERMARKETS("Супермаркет", "Supermarkets"),
        SOUVENIRS("Сувениры", "Souvenirs"),
        ELECTRONICS_AND_TECHNOLOGY("Электроника и технологии", "ElectronicsAndTechnology"),
        BOOKS("Книги", "Books");

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
