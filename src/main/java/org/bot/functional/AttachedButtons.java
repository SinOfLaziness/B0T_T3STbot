package org.bot.functional;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class AttachedButtons {

    private InlineKeyboardMarkup createButtons(List<String> buttons) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (String buttonText : buttons) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonText);
            switch(buttonText){
                case "Дом и ремонт":
                    button.setCallbackData("HomeAndRenovation");
                    break;
                case "Транспорт":
                    button.setCallbackData("Transport");
                    break;
                case "Еда":
                    button.setCallbackData("Food");
                    break;
                case "Развлечения":
                    button.setCallbackData("Entertainment");
                    break;
                case "Аптека":
                    button.setCallbackData("Pharmacies");
                    break;
                case "Косметика":
                    button.setCallbackData("Cosmetics");
                    break;
                case "Предметы одежды":
                    button.setCallbackData("ItemsOfClothing");
                    break;
                case "Супермаркет":
                    button.setCallbackData("Supermarkets");
                    break;
                case "Сувениры":
                    button.setCallbackData("Souvenirs");
                    break;
                case "Электроника и технологии":
                    button.setCallbackData("ElectronicsAndTechnology");
                    break;
                case "Книги":
                    button.setCallbackData("Books");
                    break;
            }
            row.add(button);
            if(row.size()==2){
                keyboardRowList.add(row);
                row=new ArrayList<>();
            }
        }
        if (!row.isEmpty()){
            keyboardRowList.add(row);
        }
        inlineKeyboard.setKeyboard(keyboardRowList);
        return inlineKeyboard;
    }

    public InlineKeyboardMarkup createButtonsForExpenses(){
        List<String> buttons = new ArrayList<>();
        buttons.add("Дом и ремонт");
        buttons.add("Транспорт");
        buttons.add("Еда");
        buttons.add("Развлечения");
        buttons.add("Аптека");
        buttons.add("Косметика");
        buttons.add("Супермаркет");
        buttons.add("Сувениры");
        buttons.add("Электроника и технологии");
        buttons.add("Книги");
        return createButtons(buttons);
    }

}
