package org.bot.functional;

import org.bot.database.DatabaseHandler;
import org.bot.msg.Constants;
import org.bot.msg.Message;
import org.bot.msg.MessageSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bot.database.ConstantDB;

public class UpdateHandler {

    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, String> buttonInfoState = new HashMap<>();
    private final MessageSender messageSender;
    private final DatabaseHandler dbHandler;

    public UpdateHandler(TelegramLongPollingBot bot) {
        messageSender = new MessageSender(bot);
        dbHandler = new DatabaseHandler();
    }

    public void handleUpdate(Update update) throws SQLException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatID = update.getMessage().getChatId();
            String sourceText = update.getMessage().getText();
            if (userStates.containsKey(chatID)) {
                switch(userStates.get(chatID)) {
                    case ConstantDB.USERS_HOME_AND_RENOVATION:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_HOME_AND_RENOVATION);
                        return;
                    case ConstantDB.USERS_TRANSPORT:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_TRANSPORT);
                        return;
                    case ConstantDB.USERS_FOOD:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_FOOD);
                        return;
                    case ConstantDB.USERS_ENTERTAINMENT:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_ENTERTAINMENT);
                        return;
                    case ConstantDB.USERS_PHARMACIES:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_PHARMACIES);
                        return;
                    case ConstantDB.USERS_COSMETICS:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_COSMETICS);
                        return;
                    case ConstantDB.USERS_ITEMS_OF_CLOTHING:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_ITEMS_OF_CLOTHING);
                        return;
                    case ConstantDB.USERS_SUPERMARKETS:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_SUPERMARKETS);
                        return;
                    case ConstantDB.USERS_SOUVENIRS:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_SOUVENIRS);
                        return;
                    case ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY);
                        return;
                    case ConstantDB.USERS_BOOKS:
                        handleAmountInput(chatID, sourceText, ConstantDB.USERS_BOOKS);
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

    private void handleCommand(long chatID, String sourceText, Update update) throws SQLException {
        switch (sourceText) {
            case Constants.START:
                if (!dbHandler.checkIfSigned(chatID)) {
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
                if (!dbHandler.checkIfSigned(chatID)) {
                    caseSignUpUsers(chatID);
                } else {
                    messageSender.send(chatID,Constants.ALR_REG);
                }
                break;
            case Constants.SET_EXP:
                if (dbHandler.checkIfSigned(chatID)) {
                    messageSender.send(chatID, Constants.EXP_LIST);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            case Constants.SEND_EXP:
                if (dbHandler.checkIfSigned(chatID)) {
                    String all_amounts = dbHandler.getAllAmounts(chatID);
                    messageSender.send(chatID, new Message(all_amounts));
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
        userStates.put(chatID, buttonInfo);
        buttonInfoState.put(chatID, buttonInfo);
    }

    private void handleAmountInput(long chatID, String amount, String type_amount) throws SQLException {
        String buttonInfo = buttonInfoState.get(chatID);
        userStates.remove(chatID);
        buttonInfoState.remove(chatID);
        if (!amount.matches("\\d+(\\.\\d+)?") | amount.equals("0")) {
            messageSender.send(chatID, Constants.INVALID_SUM);
            return;
        }
        messageSender.send(chatID, new Message("Вы ввели сумму: " + amount));
        pressedButtonCase(chatID, buttonInfo, amount);
        float amount_in_DB = dbHandler.getFloatField(chatID, type_amount);
        amount_in_DB += Float.parseFloat(amount);
        dbHandler.InputFloatField(chatID, type_amount, amount_in_DB);
    }

    private void caseSignUpUsers(long chatID) {
        dbHandler.signUpUser(String.valueOf(chatID));
        messageSender.send(chatID, Constants.NOW_REG);
    }

    private void pressedButtonCase(long chatID, String buttonInfo, String amount) {
        // Черновой вариант записи суммы в базу данных
        double amountValue = Double.parseDouble(amount);
        dbHandler.addToDatabase(chatID,buttonInfo,amountValue);
    }
}