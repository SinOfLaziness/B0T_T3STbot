package org.bot.functional;

import org.bot.database.ConstantDB;

import java.util.HashMap;
import java.util.Map;

public class ButtonConfig {
    private static final Map<String, String> expensesButtonMap = new HashMap<>();
    private static final Map<String, String> registrationButtonMap = new HashMap<>();

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
        expensesButtonMap.put(ConstantDB.ELECTRONICS_AND_TECHNOLOGY, ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY);
        expensesButtonMap.put(ConstantDB.BOOKS, ConstantDB.USERS_BOOKS);
        expensesButtonMap.put(ConstantDB.OTHER, ConstantDB.USERS_OTHER);

        registrationButtonMap.put(ConstantDB.REGISTRATION, ConstantDB.USERS_REGISTRATION);
    }

    public static Map<String, String> getExpensesButtonMap() {
        return expensesButtonMap;
    }

    public static Map<String, String> getRegistrationButtonMap() {
        return registrationButtonMap;
    }
}