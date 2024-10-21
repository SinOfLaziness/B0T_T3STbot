package org.bot.functional;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.bot.database.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UpdateHandler {

    private final Keyboard keyboard = new Keyboard();
    private final AttachedButtons attachedButtons = new AttachedButtons();

    private final TelegramLongPollingBot bot;
    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, String> userAmounts = new HashMap<>();

    public UpdateHandler(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatID = update.getMessage().getChatId();
            String sourceText = update.getMessage().getText();
            if (userStates.containsKey(chatID)) {
                String state = userStates.get(chatID);
                if (state.equals("WAITING_FOR_AMOUNT")) {
                    handleAmountInput(chatID, sourceText);
                    userStates.remove(chatID);
                    return;
                }
            }
            switch (sourceText) {
                case "/start":
                    sendStartCommandAnswer(chatID, update.getMessage().getChat().getFirstName());
                    break;
                case "Список команд":
                    sendHelpMessage(chatID);
                    break;
                case "Зарегистрироваться":
                    caseSignUpUsers(chatID);
                    break;
                case "Записать расходы":
                    sendExpensesList(chatID);
                    break;
                case "Вывести список расходов":
                    sendUsersExpenses(chatID);
                    break;
                default:
                    sendIfUnknownCommand(chatID);
            }
        } else if (update.hasCallbackQuery()) {
            long chatID = update.getCallbackQuery().getMessage().getChatId();
            String buttonInfo = update.getCallbackQuery().getData();
            sendMessage(chatID, "Впишите потраченную сумму", keyboard.generateGeneralKeyboard(), null);
            userStates.put(chatID, "WAITING_FOR_AMOUNT");
            userAmounts.put(chatID, buttonInfo);
        }
    }

    private void handleAmountInput(long chatID, String amount) {
        // Возможно будет переделан или удалён
        String buttonInfo = userAmounts.get(chatID);
        userAmounts.put(chatID, amount);
        String answerToSend = "Вы ввели сумму: " + amount;
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), null);
        pressedButtonCase(chatID, buttonInfo, amount);
    }

    private void pressedButtonCase(long chatID, String buttonInfo, String amount) {
        // TO DO:
        switch (buttonInfo) {
            case "HomeAndRenovation":
                break;
            case "Transport":
                break;
            case "Food":
                break;
            case "Entertainment":
                break;
            case "Pharmacies":
                break;
            case "Cosmetics":
                break;
            case "ItemsOfClothing":
                break;
            case "Supermarkets":
                break;
            case "Souvenirs":
                break;
            case "ElectronicsAndTechnology":
                break;
            case "Books":
                break;
        }
        userAmounts.remove(chatID);
    }

    private void sendUsersExpenses(long chatID) {
        if(checkIfSigned(chatID))
            return;
        String answerToSend = "Функция на этапе разработки. Пока я такое не могу делать";
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), null);
    }

    private void sendExpensesList(long chatID) {
        if(checkIfSigned(chatID))
            return;
        String answerToSend = "Какие расходы ты хочешь указать?";
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), attachedButtons.createButtonsForExpenses());
    }

    private void sendStartCommandAnswer(long chatID, String name) {
        if (checkIfSigned(chatID))
            return;
        String answerToSend = "Приветствую тебя, " + name + ". Перед началом пользования прошу тебя зарегистрироваться. Для этого напиши команду Зарегистрироваться";
        sendMessage(chatID, answerToSend, keyboard.generateStartKeyboard(), null);
    }

    private void sendIfUnknownCommand(long chatID) {
        String answerToSend = "Извини, такую команду я не знаю. Напиши Список команд, чтобы увидеть полный список команд";
        sendMessage(chatID, answerToSend);
    }

    private void sendHelpMessage(long chatID) {
        String answerToSend = "Функционал бота \n\n/start - начинает работу с ботом \nСписок команд - выводит список доступных команд\nЗарегистрироваться - регистрирует пользователя";
        sendMessage(chatID, answerToSend, keyboard.generateStartKeyboard(), null);
    }

    private void sendMessage(long chatID, String answerToSend) {
        sendMessage(chatID, answerToSend, null, null);
    }

    private void sendMessage(long chatID, String answerToSend, ReplyKeyboardMarkup replyKeyboard, InlineKeyboardMarkup inlineButtons) {
        SendMessage newMessage = new SendMessage();
        newMessage.setChatId(String.valueOf(chatID));
        newMessage.setText(answerToSend);
        if (replyKeyboard != null) {
            newMessage.setReplyMarkup(replyKeyboard);
        }
        if (inlineButtons != null) {
            newMessage.setReplyMarkup(inlineButtons);
        }
        try {
            bot.execute(newMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void caseSignUpUsers(long chatID) {
        if (checkIfSigned(chatID))
            return;
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.signUpUser(String.valueOf(chatID));
        String answerToSend = "Поздравляю! Теперь, ты можешь пользоваться всеми моими полезными штуками!";
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), null);
    }

    private boolean checkIfSigned(long chatID) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        int counter = 0;
        try (ResultSet result = dbHandler.getUserCount(chatID)) {
            if (result.next()) {
                counter = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (counter >= 1) {
            sendMessage(chatID, "Ты уже зарегистрирован, можешь продолжить свою работу", keyboard.generateGeneralKeyboard(), null);
            return true;
        }
        return false;
    }
}