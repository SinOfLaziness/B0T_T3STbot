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
            handleCommand(chatID, sourceText, update);
        } else if (update.hasCallbackQuery()) {
            long chatID = update.getCallbackQuery().getMessage().getChatId();
            String buttonInfo = update.getCallbackQuery().getData();
            handleCallbackQuery(chatID, buttonInfo);
        }
    }

    private void handleCommand(long chatID, String sourceText, Update update) {
        switch (sourceText) {
            case "/start":
                if (!checkIfSigned(chatID)) {
                    sendStartCommandAnswer(chatID, update.getMessage().getChat().getFirstName());
                } else {
                    alreadyRegistered(chatID);
                }
                break;
            case "Список команд":
                sendHelpMessage(chatID);
                break;
            case "Зарегистрироваться":
                if (!checkIfSigned(chatID)) {
                    caseSignUpUsers(chatID);
                } else {
                    alreadyRegistered(chatID);
                }
                break;
            case "Записать расходы":
                if (checkIfSigned(chatID)) {
                    sendExpensesList(chatID);
                } else {
                    mustRegister(chatID);
                }
                break;
            case "Вывести список расходов":
                if (checkIfSigned(chatID)) {
                    sendUsersExpenses(chatID);
                } else {
                    mustRegister(chatID);
                }
                break;
            default:
                sendIfUnknownCommand(chatID);
        }
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
    private void sendMessage(long chatID, String answerToSend) {
        sendMessage(chatID, answerToSend, null, null);
    }

    private void handleCallbackQuery(long chatID, String buttonInfo) {
        sendMessage(chatID, "Впишите потраченную сумму", keyboard.generateGeneralKeyboard(), null);
        userStates.put(chatID, "WAITING_FOR_AMOUNT");
        userAmounts.put(chatID, buttonInfo);
    }

    private void handleAmountInput(long chatID, String amount) {
        String buttonInfo = userAmounts.get(chatID);
        userAmounts.put(chatID, amount);
        if (!amount.matches("\\d+(\\.\\d+)?")) {
            String answerToSend = "Пожалуйста, введите числовое значение.";
            sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), null);
            return;
        }
        String answerToSend = "Вы ввели сумму: " + amount;
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), null);
        pressedButtonCase(chatID, buttonInfo, amount);
    }

    private void caseSignUpUsers(long chatID) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.signUpUser(String.valueOf(chatID));
        String answerToSend = "Поздравляю! Теперь, ты можешь пользоваться всеми моими полезными штуками!";
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), null);
    }

    private void pressedButtonCase(long chatID, String buttonInfo, String amount) {
        // Черновой вариант записи суммы в базу данных
        double amountValue = Double.parseDouble(amount);
        DatabaseHandler dbHandler = new DatabaseHandler();
        switch (buttonInfo) {
            case "HomeAndRenovation":
                dbHandler.addToDatabase(chatID, "HomeAndRenovation", amountValue);
                break;
            case "Transport":
                dbHandler.addToDatabase(chatID, "Transport", amountValue);
                break;
            case "Food":
                dbHandler.addToDatabase(chatID, "Food", amountValue);
                break;
            case "Entertainment":
                dbHandler.addToDatabase(chatID, "Entertainment", amountValue);
                break;
            case "Pharmacies":
                dbHandler.addToDatabase(chatID, "Pharmacies", amountValue);
                break;
            case "Cosmetics":
                dbHandler.addToDatabase(chatID, "Cosmetics", amountValue);
                break;
            case "ItemsOfClothing":
                dbHandler.addToDatabase(chatID, "ItemsOfClothing", amountValue);
                break;
            case "Supermarkets":
                dbHandler.addToDatabase(chatID, "Supermarkets", amountValue);
                break;
            case "Souvenirs":
                dbHandler.addToDatabase(chatID, "Souvenirs", amountValue);
                break;
            case "ElectronicsAndTechnology":
                dbHandler.addToDatabase(chatID, "ElectronicsAndTechnology", amountValue);
                break;
            case "Books":
                dbHandler.addToDatabase(chatID, "Books", amountValue);
                break;
        }
        userAmounts.remove(chatID);
    }

    private void sendUsersExpenses(long chatID) {
        String answerToSend = "Функция на этапе разработки. Пока я такое не могу делать";
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), null);
    }

    private void sendExpensesList(long chatID) {
        String answerToSend = "Какие расходы ты хочешь указать?";
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), attachedButtons.createButtonsForExpenses());
    }

    private void sendStartCommandAnswer(long chatID, String name) {
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

    private void mustRegister (long chatID){
        String answerToSend = "Ты должен зарегистрироваться, чтобы использовать эту команду";
        sendMessage(chatID, answerToSend, keyboard.generateStartKeyboard(), null);
    }

    private void alreadyRegistered (long chatID){
        String answerToSend = "Ты уже зарегистрирован, можешь продолжить свою работу";
        sendMessage(chatID, answerToSend, keyboard.generateGeneralKeyboard(), null);
    }

    private boolean checkIfSigned(long chatID) {
//        DatabaseHandler dbHandler = new DatabaseHandler();
//        int counter = 0;
//        try (ResultSet result = dbHandler.getUserCount(chatID)) {
//            if (result.next()) {
//                counter = result.getInt(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (counter >= 1) {
//            return true;
//        }
//        return false;
        return true;
    }
}