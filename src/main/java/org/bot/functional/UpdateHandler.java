package org.bot.functional;

import org.bot.database.ConstantDB;
import org.bot.database.DatabaseInitializer;
import org.bot.msg.Constants;
import org.bot.msg.MessageSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class UpdateHandler {

    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, String> buttonInfoState = new HashMap<>();
    private final MessageSender messageSender;
    private final DatabaseInitializer dbHandler;

    public UpdateHandler(TelegramLongPollingBot bot) {
        messageSender = new MessageSender(bot);
        dbHandler = new DatabaseInitializer();
    }

    public void handleUpdate(Update update) throws SQLException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatID = update.getMessage().getChatId();
            String sourceText = update.getMessage().getText();
            if (userStates.containsKey(chatID)) {
                handleUserStates(chatID, sourceText);
                return;
            }
            handleCommand(chatID, sourceText);
        } else if (update.hasCallbackQuery()) {
            long chatID = update.getCallbackQuery().getMessage().getChatId();
            String buttonInfo = update.getCallbackQuery().getData();
            handleCallbackQuery(chatID, buttonInfo);
        }
    }

    private void handleCommand(long chatID, String sourceText) throws SQLException {
        switch (sourceText) {
            case Constants.START:
                if (!dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    messageSender.send(chatID, Constants.START_TEXT_TEMPL);
                } else {
                    messageSender.send(chatID, Constants.ALR_REG);
                }
                break;
            case ConstantDB.KEY_COMMANDS:
                messageSender.send(chatID, Constants.HELP_COM);
                break;
            case ConstantDB.KEY_REGISTRATION:
                if (!dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    dbHandler.getDatabaseTools().signUpUser(String.valueOf(chatID));
                    messageSender.send(chatID, Constants.NOW_REG);
                } else {
                    messageSender.send(chatID, Constants.ALR_REG);
                }
                break;
            case Constants.SET_EXP:
                if (dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    messageSender.send(chatID, Constants.EXP_LIST);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            case Constants.SEND_EXP:
                if (dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    messageSender.send(chatID, Constants.ASK_PERIOD_EXP);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            case Constants.SET_INCOME:
                if (dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    messageSender.send(chatID, Constants.INCOME_LIST);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            case Constants.SEND_INCOME:
                if (dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    dbHandler.getDatabaseStatistics().makeStatisticAboutIncome(chatID, messageSender);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            case Constants.COMPARE:
                if (dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    messageSender.send(chatID, Constants.ASK_PERIOD_TOTAL);
                } else {
                    messageSender.send(chatID, Constants.ASK_FOR_REG);
                }
                break;
            default:
                messageSender.send(chatID, Constants.UNK_COM);
        }
    }

    private void handleUserStates(long chatID, String sourceText) throws SQLException {
        switch (userStates.get(chatID)) {
            case ConstantDB.KEY_MONTH_EXP:
            case ConstantDB.KEY_PERIOD_EXP:
                dbHandler.getDatabaseStatistics().makeStatisticAboutExpenses(chatID, sourceText, messageSender);
                break;
            case ConstantDB.KEY_MONTH_TOTAL:
            case ConstantDB.KEY_PERIOD_TOTAL:
                dbHandler.getDatabaseStatistics().makeStatisticAboutTotal(chatID, sourceText, messageSender);
                break;
            case ConstantDB.KEY_USERS_CATEGORY:
                dbHandler.getDatabaseTools().makeEntryAboutExpenses(chatID, sourceText, messageSender);
                break;
            case ConstantDB.KEY_USERS_INCOME:
                dbHandler.getDatabaseTools().makeEntryAboutIncome(chatID, sourceText, messageSender);
                break;
            case ConstantDB.KEY_EXPENSES:
                String buttonInfo = buttonInfoState.get(chatID);
                buttonInfoState.remove(chatID);
                dbHandler.getDatabaseTools().makeEntryAboutExpenses(chatID, sourceText, buttonInfo, messageSender);
                break;
            case ConstantDB.KEY_INCOME:
                buttonInfo = buttonInfoState.get(chatID);
                buttonInfoState.remove(chatID);
                dbHandler.getDatabaseTools().makeEntryAboutIncome(chatID, sourceText, buttonInfo, messageSender);
                break;
        }
        userStates.remove(chatID);
    }

    private void handleCallbackQuery(long chatID, String buttonInfo) throws SQLException {
        switch (buttonInfo) {
            case ConstantDB.KEY_REGISTRATION:
                if (!dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    dbHandler.getDatabaseTools().signUpUser(String.valueOf(chatID));
                    messageSender.send(chatID, Constants.NOW_REG);
                } else {
                    messageSender.send(chatID, Constants.ALR_REG);
                }
                break;
            case ConstantDB.KEY_COMMANDS:
                messageSender.send(chatID, Constants.HELP_COM);
                break;
            case ConstantDB.KEY_MONTH_EXP:
                messageSender.send(chatID, Constants.MONTH_PATTERN);
                userStates.put(chatID, ConstantDB.KEY_MONTH_EXP);
                break;
            case ConstantDB.KEY_PERIOD_EXP:
                messageSender.send(chatID, Constants.PERIOD_PATTERN);
                userStates.put(chatID, ConstantDB.KEY_PERIOD_EXP);
                break;
            case ConstantDB.KEY_MONTH_TOTAL:
                messageSender.send(chatID, Constants.MONTH_PATTERN);
                userStates.put(chatID, ConstantDB.KEY_MONTH_TOTAL);
                break;
            case ConstantDB.KEY_PERIOD_TOTAL:
                messageSender.send(chatID, Constants.PERIOD_PATTERN);
                userStates.put(chatID, ConstantDB.KEY_PERIOD_TOTAL);
                break;
            case ConstantDB.KEY_USERS_CATEGORY:
                messageSender.send(chatID, Constants.USR_CAT);
                userStates.put(chatID, ConstantDB.KEY_USERS_CATEGORY);
                break;
            case ConstantDB.KEY_USERS_INCOME:
                messageSender.send(chatID, Constants.USR_INCOME);
                userStates.put(chatID, ConstantDB.KEY_USERS_INCOME);
                break;
            default:
                if (ConstantDB.allExpenses.contains(buttonInfo)){
                    messageSender.send(chatID, Constants.EXP_SUM);
                    userStates.put(chatID, ConstantDB.KEY_EXPENSES);
                }else{
                    messageSender.send(chatID, Constants.INCOME_SUM);
                    userStates.put(chatID, ConstantDB.KEY_INCOME);
                }
                buttonInfoState.put(chatID, buttonInfo);
                break;
        }
    }
}