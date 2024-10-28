package org.bot.functional;

import org.bot.database.ConstantDB;
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
    private final Map<Long, String> buttonInfoState = new HashMap<>();
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
            case Constants.START:
                if (!checkIfSigned(chatID)) {
                    String name = Constants.START_TEXT_TEMPL.formatted(update.getMessage().getChat().getFirstName());
                    messageSender.send(chatID,new Message(name));
                } else {
                    messageSender.send(chatID,Constants.ALR_REG);
                }
                break;
            case Constants.COM_LIST:
                messageSender.send(chatID,Constants.HELP_COM);
                break;
            case Constants.REGISTRATION:
                if (!checkIfSigned(chatID)) {
                    caseSignUpUsers(chatID);
                } else {
                    messageSender.send(chatID,Constants.ALR_REG);
                }
                break;
            case Constants.SET_EXP:
                if (checkIfSigned(chatID)) {
                    messageSender.send(chatID, Constants.EXP_LIST);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            case Constants.SEND_EXP:
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
        buttonInfoState.put(chatID, buttonInfo);
    }

    private void handleAmountInput(long chatID, String amount) {
        if (!amount.matches("\\d+(\\.\\d+)?")) {
            messageSender.send(chatID, Constants.INVALID_SUM);
            buttonInfoState.remove(chatID);
            return;
        }
        String buttonInfo = buttonInfoState.get(chatID);
        messageSender.send(chatID, new Message("Вы ввели сумму: " + amount));
        pressedButtonCase(chatID, buttonInfo, amount);
        buttonInfoState.remove(chatID);
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
            case ConstantDB.USERS_HOME_AND_RENOVATION:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_HOME_AND_RENOVATION, amountValue);
                break;
            case ConstantDB.USERS_TRANSPORT:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_TRANSPORT, amountValue);
                break;
            case ConstantDB.USERS_FOOD:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_FOOD, amountValue);
                break;
            case ConstantDB.USERS_ENTERTAINMENT:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_ENTERTAINMENT, amountValue);
                break;
            case ConstantDB.USERS_PHARMACIES:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_PHARMACIES, amountValue);
                break;
            case ConstantDB.USERS_COSMETICS:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_COSMETICS, amountValue);
                break;
            case ConstantDB.USERS_ITEMS_OF_CLOTHING:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_ITEMS_OF_CLOTHING, amountValue);
                break;
            case ConstantDB.USERS_SUPERMARKETS:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_SUPERMARKETS, amountValue);
                break;
            case ConstantDB.USERS_SOUVENIRS:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_SOUVENIRS, amountValue);
                break;
            case ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY, amountValue);
                break;
            case ConstantDB.USERS_BOOKS:
                dbHandler.addToDatabase(chatID, ConstantDB.USERS_BOOKS, amountValue);
                break;
        }
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