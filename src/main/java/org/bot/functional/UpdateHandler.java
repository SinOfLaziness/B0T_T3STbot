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
                if (userStates.get(chatID).equals(Constants.WAIT_AMOUNT)) {
                    handleAmountInput(chatID, sourceText);
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
        userStates.put(chatID, Constants.WAIT_AMOUNT);
        buttonInfoState.put(chatID, buttonInfo);
    }

    private void handleAmountInput(long chatID, String amount) {
        String buttonInfo = buttonInfoState.get(chatID);
        userStates.remove(chatID);
        buttonInfoState.remove(chatID);
        if (!amount.matches("\\d+(\\.\\d+)?") | amount.equals("0")) {
            messageSender.send(chatID, Constants.INVALID_SUM);
            return;
        }
        messageSender.send(chatID, new Message("Вы ввели сумму: " + amount));
        pressedButtonCase(chatID, buttonInfo, amount);
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