package org.bot.functional;

import org.bot.database.ConstantDB;
import org.bot.database.DatabaseHandler;
import org.bot.msg.Constants;
import org.bot.msg.Message;
import org.bot.msg.MessageSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
                handleAmountInput(chatID, sourceText);
                return;
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
                    messageSender.send(chatID, Constants.START_TEXT_TEMPL);
                } else {
                    messageSender.send(chatID, Constants.ALR_REG);
                }
                break;
            case Constants.COM_LIST:
                messageSender.send(chatID, Constants.HELP_COM);
                break;
            case Constants.REGISTRATION:
                if (!dbHandler.checkIfSigned(chatID)) {
                    dbHandler.caseSignUpUsers(chatID, messageSender);
                } else {
                    messageSender.send(chatID, Constants.ALR_REG);
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
                    dbHandler.sendAllAmounts(chatID, messageSender);
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

    private void handleAmountInput(long chatID, String string_amount) throws SQLException {
        String buttonInfo = buttonInfoState.get(chatID);
        userStates.remove(chatID);
        buttonInfoState.remove(chatID);

        int iFlag = 0;
        if (string_amount.matches("(\\d+(\\.\\d+)?)+ .*?") ||
                string_amount.matches("\\d+ .*?")) {
            iFlag = 1;
        } else if (string_amount.matches("(\\d+(\\.\\d+)?)+") ||
                string_amount.matches("\\d+")) {
            iFlag = 2;
        } else {
            messageSender.send(chatID, Constants.INVALID_SUM);
            return;
        }

        float amount = 0;
        if (iFlag == 1) {
            amount = Float.parseFloat(string_amount.split(" ")[0]);
        } else if (iFlag == 2) {
            amount = Float.parseFloat(string_amount);
        }
        messageSender.send(chatID, new Message("Вы ввели сумму: " + amount));
        float amount_in_DB = dbHandler.getFloatField(chatID, buttonInfo);
        amount_in_DB += amount;
        dbHandler.InputFloatField(chatID, buttonInfo, amount_in_DB);
    }


}