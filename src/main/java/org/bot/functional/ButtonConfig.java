package org.bot.functional;

import org.bot.database.ConstantDB;

import java.util.HashMap;
import java.util.Map;

public class ButtonConfig {
    private static final Map<String, String> expensesButtonMap = new HashMap<>();
    private static final Map<String, String> registrationButtonMap = new HashMap<>();
    private static final Map<String, String> askForPeriodFormatButtonMap = new HashMap<>();

    static {
        expensesButtonMap.put(ConstantDB.USERS_HOME_AND_RENOVATION, ConstantDB.HOME_AND_RENOVATION);
        expensesButtonMap.put(ConstantDB.USERS_TRANSPORT, ConstantDB.TRANSPORT);
        expensesButtonMap.put(ConstantDB.USERS_FOOD, ConstantDB.FOOD);
        expensesButtonMap.put(ConstantDB.USERS_ENTERTAINMENT, ConstantDB.ENTERTAINMENT);
        expensesButtonMap.put(ConstantDB.USERS_PHARMACIES, ConstantDB.PHARMACIES);
        expensesButtonMap.put(ConstantDB.USERS_COSMETICS, ConstantDB.COSMETICS);
        expensesButtonMap.put(ConstantDB.USERS_ITEMS_OF_CLOTHING, ConstantDB.ITEMS_OF_CLOTHING);
        expensesButtonMap.put(ConstantDB.USERS_SUPERMARKETS, ConstantDB.SUPERMARKETS);
        expensesButtonMap.put(ConstantDB.USERS_TRANSFERS, ConstantDB.TRANSFERS);
        expensesButtonMap.put(ConstantDB.USERS_ELECTRONICS, ConstantDB.ELECTRONICS);
        expensesButtonMap.put(ConstantDB.USERS_BILL_PAYMENT, ConstantDB.BILL_PAYMENT);
        expensesButtonMap.put(ConstantDB.USERS_OTHER, ConstantDB.OTHER);

        registrationButtonMap.put(ConstantDB.USERS_REGISTRATION, ConstantDB.REGISTRATION);
        registrationButtonMap.put(ConstantDB.USERS_COMMANDS, ConstantDB.COM_LIST);

        askForPeriodFormatButtonMap.put(ConstantDB.USERS_MONTH, ConstantDB.MONTH);
        askForPeriodFormatButtonMap.put(ConstantDB.USERS_PERIOD, ConstantDB.PERIOD);
    }

    public static Map<String, String> getExpensesButtonMap() {
        return expensesButtonMap;
    }

    public static Map<String, String> getRegistrationButtonMap() {
        return registrationButtonMap;
    }

    public static Map<String, String> getPeriodFormatButtonMap() {
        return askForPeriodFormatButtonMap;
    }
}