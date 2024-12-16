package com.github.FinancialAssistant.database;

import com.github.FinancialAssistant.msg.Constants;
import com.github.FinancialAssistant.msg.Message;
import com.github.FinancialAssistant.msg.MessageSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DatabaseTools extends Configs {
    private final Connection dbConnection;

    public DatabaseTools(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void signUpUser(String telegramID) {
        String insert = String.format("INSERT INTO %s(%s) VALUES (?)",
                ConstantDB.USER_TABLE, ConstantDB.USERS_ID);
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setInt(1, Integer.parseInt(telegramID));
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfSigned(long chatID) throws SQLException {
        int counter = 0;
        try (ResultSet result = getUserCount(chatID)) {
            if (result.next()) {
                counter = result.getInt(1);
            }
        }
        return counter >= 1;
    }

    private ResultSet getUserCount(long chatID) {
        ResultSet resultSet = null;
        String insert = "SELECT COUNT(*) FROM " + ConstantDB.USER_TABLE + " WHERE " + ConstantDB.USERS_ID + "=?";
        try {
            PreparedStatement prSt = dbConnection.prepareStatement(insert);
            prSt.setLong(1, chatID);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private float parseFloat(String string_amount) {
        if (string_amount.matches("(\\d{1,12}(\\.[0-9]{1,2})?)")) {
            return Float.parseFloat(string_amount);
        } else {
            return -1;
        }
    }

    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formattedDatePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formattedDatePattern);
    }

    private boolean ifExistsInSideTable(String value, String tableName, String columnName) {
        String insert = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + "=?";
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setString(1, value);
            try (ResultSet resultSet = prSt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) >= 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addNewSideTableValue(String value, String tableName, String columnName) {
        if (ifExistsInSideTable(value, tableName, columnName)) {
            return;
        }
        String insert = String.format("INSERT INTO %s(%s) VALUES (?)",
                tableName, columnName);
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setString(1, value);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewCategory(String category) {
        addNewSideTableValue(category, ConstantDB.CATEGORIES_TABLE, ConstantDB.TABLE_CATEGORY);
    }

    private void addNewIncome(String category) {
        addNewSideTableValue(category, ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME);
    }

    private void inputExpense(long chatID, String category, float value) {
        addNewCategory(category);
        String currentDate = getCurrentDate();
        String insert = String.format(
                "INSERT INTO %s(%s.%s,%s.%s,%s.%s,%s.%s) " +
                        "VALUES ((SELECT %s.%s FROM %s WHERE %s.%s = ?),(SELECT %s.%s FROM %s WHERE %s.%s = ?),?,?)",
                ConstantDB.ACCOUNTINGS_TABLE,
                ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_USER_ID,
                ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_CATEGORIES,
                ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_DATE,
                ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_AMOUNT,
                ConstantDB.USER_TABLE, ConstantDB.TABLE_USER_ID,
                ConstantDB.USER_TABLE,
                ConstantDB.USER_TABLE, ConstantDB.USERS_ID,
                ConstantDB.CATEGORIES_TABLE, ConstantDB.TABLE_CATEGORIES,
                ConstantDB.CATEGORIES_TABLE,
                ConstantDB.CATEGORIES_TABLE, ConstantDB.TABLE_CATEGORY
        );
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setLong(1, chatID);
            prSt.setString(2, category);
            prSt.setString(3, currentDate);
            prSt.setFloat(4, value);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean incomeAlreadyDefined(long chatID, String income) {
        String currentDate = getCurrentDate();
        String insert = String.format(
                "SELECT COUNT(*) FROM %s " +
                        "WHERE %s.%s = (SELECT %s.%s FROM %s WHERE %s.%s = ?) " +
                        "AND %s.%s = (SELECT %s.%s FROM %s WHERE %s.%s = ?) " +
                        "AND YEAR(%s.%s) = YEAR(?) AND MONTH(%s.%s) = MONTH(?)",
                ConstantDB.REVENUE_TABLE,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_USER_ID,
                ConstantDB.USER_TABLE, ConstantDB.TABLE_USER_ID, ConstantDB.USER_TABLE, ConstantDB.USER_TABLE, ConstantDB.USERS_ID,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_INCOME_ID,
                ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME_ID, ConstantDB.INCOMES_TABLE, ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE
        );
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setLong(1, chatID);
            prSt.setString(2, income);
            prSt.setString(3, currentDate);
            prSt.setString(4, currentDate);
            try (ResultSet resultSet = prSt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void inputIncome(long chatID, String income, float value) {
        addNewIncome(income);
        String currentDate = getCurrentDate();
        String insert = "";
        if (value == 0.0) {
            insert = String.format(
                    "DELETE FROM %s " +
                            "WHERE %s.%s = (SELECT %s.%s FROM %s WHERE %s.%s = ?) " +
                            "AND %s.%s = (SELECT %s.%s FROM %s WHERE %s.%s = ?) " +
                            "AND YEAR(%s.%s) = YEAR(?) AND MONTH(%s.%s) = MONTH(?)",
                    ConstantDB.REVENUE_TABLE,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_USER_ID,
                    ConstantDB.USER_TABLE, ConstantDB.TABLE_USER_ID, ConstantDB.USER_TABLE, ConstantDB.USER_TABLE, ConstantDB.USERS_ID,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_INCOME_ID,
                    ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME_ID, ConstantDB.INCOMES_TABLE, ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE
            );
        } else if (incomeAlreadyDefined(chatID, income)) {
            insert = String.format(
                    "UPDATE %s " +
                            "SET %s.%s = ? " +
                            "WHERE %s.%s = (SELECT %s.%s FROM %s WHERE %s.%s = ?) " +
                            "AND %s.%s = (SELECT %s.%s FROM %s WHERE %s.%s = ?) " +
                            "AND YEAR(%s.%s) = YEAR(?) AND MONTH(%s.%s) = MONTH(?)",
                    ConstantDB.REVENUE_TABLE,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_AMOUNT,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_USER_ID,
                    ConstantDB.USER_TABLE, ConstantDB.TABLE_USER_ID, ConstantDB.USER_TABLE, ConstantDB.USER_TABLE, ConstantDB.USERS_ID,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_INCOME_ID,
                    ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME_ID, ConstantDB.INCOMES_TABLE, ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE
            );
        } else {
            insert = String.format(
                    "INSERT INTO %s(%s.%s,%s.%s,%s.%s,%s.%s) " +
                            "VALUES ((SELECT %s.%s FROM %s WHERE %s.%s = ?),(SELECT %s.%s FROM %s WHERE %s.%s = ?),?,?)",
                    ConstantDB.REVENUE_TABLE,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_USER_ID,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_INCOME_ID,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE,
                    ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_AMOUNT,
                    ConstantDB.USER_TABLE, ConstantDB.TABLE_USER_ID,
                    ConstantDB.USER_TABLE,
                    ConstantDB.USER_TABLE, ConstantDB.USERS_ID,
                    ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME_ID,
                    ConstantDB.INCOMES_TABLE,
                    ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME
            );
        }
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            if (value == 0.0) {
                prSt.setLong(1, chatID);
                prSt.setString(2, income);
                prSt.setString(3, currentDate);
                prSt.setString(4, currentDate);
            } else if (incomeAlreadyDefined(chatID, income)) {
                prSt.setFloat(1, value);
                prSt.setLong(2, chatID);
                prSt.setString(3, income);
                prSt.setString(4, currentDate);
                prSt.setString(5, currentDate);
            } else {
                prSt.setLong(1, chatID);
                prSt.setString(2, income);
                prSt.setString(3, currentDate);
                prSt.setFloat(4, value);
            }
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeEntryAboutIncome(long chatID, String stringAmount, String buttonInfo, MessageSender messageSender) throws SQLException {
        float amount = parseFloat(stringAmount);
        if (amount == -1) {
            messageSender.send(chatID, Constants.INVALID_SUM);
            return;
        }
        inputIncome(chatID, buttonInfo, amount);
        if (amount == 0.0) {
            messageSender.send(chatID, new Message("\uD83D\uDDD1\uFE0FУказанная вами категория дохода была успешно удалена"));
        } else {
            messageSender.send(chatID, new Message("Ваш доход в " + amount + " рублей был успешно записан\uD83C\uDF89"));
        }
    }

    public void makeEntryAboutExpenses(long chatID, String stringAmount, String buttonInfo, MessageSender messageSender) throws SQLException {
        float amount = parseFloat(stringAmount);
        if (amount == -1 || amount == 0.0) {
            messageSender.send(chatID, Constants.INVALID_SUM);
            return;
        }
        inputExpense(chatID, buttonInfo, amount);
        messageSender.send(chatID, new Message("Ваша сумма в " + amount + " рублей была успешно записана\uD83C\uDF89"));
    }

    public void makeEntryAboutExpenses(long chatID, String userInput, MessageSender messageSender) throws SQLException {
        processUserInput(chatID, userInput, messageSender, true);
    }

    public void makeEntryAboutIncome(long chatID, String userInput, MessageSender messageSender) throws SQLException {
        processUserInput(chatID, userInput, messageSender, false);
    }

    private void processUserInput(long chatID, String userInput, MessageSender messageSender, boolean isExpense) throws SQLException {
        String regex = "(.+)\\s+(.+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);
        if (!matcher.find()) {
            messageSender.send(chatID, Constants.INVALID_INPUT);
            return;
        }
        String category = matcher.group(1);
        String amount = matcher.group(2);
        if (category.length() > 45) {
            messageSender.send(chatID, Constants.TOO_LONG_CAT);
            return;
        }
        if (isExpense) {
            makeEntryAboutExpenses(chatID, amount, category, messageSender);
        } else {
            makeEntryAboutIncome(chatID, amount, category, messageSender);
        }
    }
}
