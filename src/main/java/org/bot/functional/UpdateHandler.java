package org.bot.functional;

import org.bot.database.DatabaseHandler;
import org.bot.msg.Constants;
import org.bot.msg.Message;
import org.bot.msg.MessageSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class UpdateHandler {

    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, String> userAmounts = new HashMap<>();
    private final MessageSender messageSender;

    public UpdateHandler(TelegramLongPollingBot bot) {
        messageSender = new MessageSender(bot);
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
                    String name = Constants.START_TEXT_TEMPL.formatted(update.getMessage().getChat().getFirstName());
                    messageSender.send(chatID,new Message(name));
                } else {
                    messageSender.send(chatID,Constants.ALR_REG);
                }
                break;
            case "Список команд":
                messageSender.send(chatID,Constants.HELP_COM);
                break;
            case "Зарегистрироваться":
                if (!checkIfSigned(chatID)) {
                    caseSignUpUsers(chatID);
                } else {
                    messageSender.send(chatID,Constants.ALR_REG);
                }
                break;
            case "Записать расходы":
                if (checkIfSigned(chatID)) {
                    messageSender.send(chatID, Constants.EXP_LIST);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            case "Вывести список расходов":
                if (checkIfSigned(chatID)) {
                    messageSender.send(chatID,Constants.NOT_IMPLEMENTED);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            default:
                messageSender.send(chatID, Constants.UNK_COM);
        }
    }

    private void handleCallbackQuery(long chatID, String buttonInfo) {
        messageSender.send(chatID, Constants.EXP_SUM);
        userStates.put(chatID, "WAITING_FOR_AMOUNT");
        userAmounts.put(chatID, buttonInfo);
    }

    private void handleAmountInput(long chatID, String amount) {
        String buttonInfo = userAmounts.get(chatID);
        userAmounts.put(chatID, amount);
        if (!amount.matches("\\d+(\\.\\d+)?")) {
            messageSender.send(chatID, Constants.INVALID_SUM);
            return;
        }
        messageSender.send(chatID, new Message("Вы ввели сумму: " + amount));
        pressedButtonCase(chatID, buttonInfo, amount);
    }

    private void caseSignUpUsers(long chatID) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.signUpUser(String.valueOf(chatID));
        messageSender.send(chatID, Constants.NOW_REG);
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