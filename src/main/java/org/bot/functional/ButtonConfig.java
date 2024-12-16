package org.bot.functional;

import org.bot.database.ConstantDB;

import java.util.HashMap;
import java.util.Map;

public class ButtonConfig {
    private static final Map<String, String> expensesButtonMap = new HashMap<>();
    private static final Map<String, String> incomeButtonMap = new HashMap<>();
    private static final Map<String, String> registrationButtonMap = new HashMap<>();

    static {
        expensesButtonMap.put(ConstantDB.KEY_HOME_AND_RENOVATION, ConstantDB.HOME_AND_RENOVATION);
        expensesButtonMap.put(ConstantDB.KEY_TRANSPORT, ConstantDB.TRANSPORT);
        expensesButtonMap.put(ConstantDB.KEY_FOOD, ConstantDB.FOOD);
        expensesButtonMap.put(ConstantDB.KEY_ENTERTAINMENT, ConstantDB.ENTERTAINMENT);
        expensesButtonMap.put(ConstantDB.KEY_PHARMACIES, ConstantDB.PHARMACIES);
        expensesButtonMap.put(ConstantDB.KEY_COSMETICS, ConstantDB.COSMETICS);
        expensesButtonMap.put(ConstantDB.KEY_ITEMS_OF_CLOTHING, ConstantDB.ITEMS_OF_CLOTHING);
        expensesButtonMap.put(ConstantDB.KEY_SUPERMARKETS, ConstantDB.SUPERMARKETS);
        expensesButtonMap.put(ConstantDB.KEY_TRANSFERS, ConstantDB.TRANSFERS);
        expensesButtonMap.put(ConstantDB.KEY_ELECTRONICS, ConstantDB.ELECTRONICS);
        expensesButtonMap.put(ConstantDB.KEY_BILL_PAYMENT, ConstantDB.BILL_PAYMENT);
        expensesButtonMap.put(ConstantDB.KEY_OTHER, ConstantDB.OTHER);
        expensesButtonMap.put(ConstantDB.KEY_ONLINE_PAYMENTS, ConstantDB.ONLINE_PAYMENTS);
        expensesButtonMap.put(ConstantDB.KEY_ANIMALS, ConstantDB.ANIMALS);
        expensesButtonMap.put(ConstantDB.KEY_USERS_CATEGORY, ConstantDB.USERS_CATEGORY);

        incomeButtonMap.put(ConstantDB.KEY_WAGES, ConstantDB.WAGES);
        incomeButtonMap.put(ConstantDB.KEY_BUSINESS, ConstantDB.BUSINESS);
        incomeButtonMap.put(ConstantDB.KEY_INVESTMENTS, ConstantDB.INVESTMENTS);
        incomeButtonMap.put(ConstantDB.KEY_RENT, ConstantDB.RENT);
        incomeButtonMap.put(ConstantDB.KEY_SOCIAL_BENEFITS, ConstantDB.SOCIAL_BENEFITS);
        incomeButtonMap.put(ConstantDB.KEY_PENSION, ConstantDB.PENSION);
        incomeButtonMap.put(ConstantDB.KEY_FREELANCING, ConstantDB.FREELANCING);
        incomeButtonMap.put(ConstantDB.KEY_USERS_INCOME, ConstantDB.USERS_INCOME);

        registrationButtonMap.put(ConstantDB.KEY_REGISTRATION, ConstantDB.REGISTRATION);
        registrationButtonMap.put(ConstantDB.KEY_COMMANDS, ConstantDB.COM_LIST);

    }

    public static Map<String, String> getExpensesButtonMap() {
        return expensesButtonMap;
    }

    public static Map<String, String> getIncomeButtonMap() {
        return incomeButtonMap;
    }

    public static Map<String, String> getRegistrationButtonMap() {
        return registrationButtonMap;
    }

    public static Map<String, String> getPeriodFormatButtonMap(boolean isExpense, boolean isIncome) {
        Map<String, String> buttonMap = new HashMap<>();
        if (isExpense) {
            buttonMap.put(ConstantDB.KEY_MONTH_EXP, ConstantDB.MONTH);
            buttonMap.put(ConstantDB.KEY_PERIOD_EXP, ConstantDB.PERIOD);
        } else if (isIncome) {
            buttonMap.put(ConstantDB.KEY_MONTH_INCOME, ConstantDB.MONTH);
            buttonMap.put(ConstantDB.KEY_PERIOD_INCOME, ConstantDB.PERIOD);
        } else {
            buttonMap.put(ConstantDB.KEY_MONTH_TOTAL, ConstantDB.MONTH);
            buttonMap.put(ConstantDB.KEY_PERIOD_TOTAL, ConstantDB.PERIOD);

        }
        return buttonMap;
    }
}