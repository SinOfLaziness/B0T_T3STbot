package org.bot.functional;

import org.bot.database.ConstantDB;

import java.util.HashMap;
import java.util.Map;

public class ButtonConfig {
    private static final Map<String, String> expensesButtonMap = new HashMap<>();
    private static final Map<String, String> registrationButtonMap = new HashMap<>();
    private static final Map<String, String> askForPeriodFormatButtonMap = new HashMap<>();

    static {
        expensesButtonMap.put(ConstantDB.HOME_AND_RENOVATION, ConstantDB.USERS_HOME_AND_RENOVATION);
        expensesButtonMap.put(ConstantDB.TRANSPORT, ConstantDB.USERS_TRANSPORT);
        expensesButtonMap.put(ConstantDB.FOOD, ConstantDB.USERS_FOOD);
        expensesButtonMap.put(ConstantDB.ENTERTAINMENT, ConstantDB.USERS_ENTERTAINMENT);
        expensesButtonMap.put(ConstantDB.PHARMACIES, ConstantDB.USERS_PHARMACIES);
        expensesButtonMap.put(ConstantDB.COSMETICS, ConstantDB.USERS_COSMETICS);
        expensesButtonMap.put(ConstantDB.ITEMS_OF_CLOTHING, ConstantDB.USERS_ITEMS_OF_CLOTHING);
        expensesButtonMap.put(ConstantDB.SUPERMARKETS, ConstantDB.USERS_SUPERMARKETS);
        expensesButtonMap.put(ConstantDB.TRANSFERS, ConstantDB.USERS_TRANSFERS);
        expensesButtonMap.put(ConstantDB.ELECTRONICS, ConstantDB.USERS_ELECTRONICS);
        expensesButtonMap.put(ConstantDB.BILL_PAYMENT, ConstantDB.USERS_BILL_PAYMENT);
        expensesButtonMap.put(ConstantDB.OTHER, ConstantDB.USERS_OTHER);

        registrationButtonMap.put(ConstantDB.REGISTRATION, ConstantDB.USERS_REGISTRATION);
        registrationButtonMap.put(ConstantDB.COM_LIST, ConstantDB.USERS_COMMANDS);

        askForPeriodFormatButtonMap.put(ConstantDB.MONTH, ConstantDB.USERS_MONTH);
        askForPeriodFormatButtonMap.put(ConstantDB.PERIOD, ConstantDB.USERS_PERIOD);
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