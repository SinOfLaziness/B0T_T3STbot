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
            case ConstantDB.USERS_COMMANDS:
                messageSender.send(chatID, Constants.HELP_COM);
                break;
            case ConstantDB.USERS_REGISTRATION:
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
                    messageSender.send(chatID, Constants.ASK_PERIOD);
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
            case ConstantDB.USERS_MONTH:
                int flag = 1;
                dbHandler.getDatabaseTools().makeStatisticAboutExpenses(chatID, sourceText, flag, messageSender);
                break;
            case ConstantDB.USERS_PERIOD:
                flag = 2;
                dbHandler.getDatabaseTools().makeStatisticAboutExpenses(chatID, sourceText, flag, messageSender);
                break;
            default:
                String buttonInfo = buttonInfoState.get(chatID);
                buttonInfoState.remove(chatID);
                dbHandler.getDatabaseTools().makeEntryAboutExpenses(chatID, sourceText, buttonInfo, messageSender);
                break;
        }
        userStates.remove(chatID);
    }

    private void handleCallbackQuery(long chatID, String buttonInfo) throws SQLException {
        switch (buttonInfo) {
            case ConstantDB.USERS_REGISTRATION:
                if (!dbHandler.getDatabaseTools().checkIfSigned(chatID)) {
                    dbHandler.getDatabaseTools().signUpUser(String.valueOf(chatID));
                    messageSender.send(chatID, Constants.NOW_REG);
                } else {
                    messageSender.send(chatID, Constants.ALR_REG);
                }
                break;
            case ConstantDB.USERS_COMMANDS:
                messageSender.send(chatID, Constants.HELP_COM);
                break;
            case ConstantDB.USERS_MONTH:
                messageSender.send(chatID, Constants.MONTH_PATTERN);
                userStates.put(chatID, ConstantDB.USERS_MONTH);
                break;
            case ConstantDB.USERS_PERIOD:
                messageSender.send(chatID, Constants.PERIOD_PATTERN);
                userStates.put(chatID, ConstantDB.USERS_PERIOD);
                break;
            default:
                messageSender.send(chatID, Constants.EXP_SUM);
                userStates.put(chatID, buttonInfo);
                buttonInfoState.put(chatID, buttonInfo);
                break;
        }
    }
}