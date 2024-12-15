package org.bot.database;

import org.bot.functional.ButtonConfig;
import org.bot.msg.Constants;
import org.bot.msg.Message;
import org.bot.msg.MessageSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private Map<String, Double> getAllAmounts(long chatID, List<String> datesList) throws SQLException {
        String firstDate = datesList.get(0);
        String secondDate = datesList.get(1);
        String insert = String.format(
                "SELECT %s.%s, %s.%s FROM %s " +
                        "JOIN %s ON %s.%s = %s.%s " +
                        "JOIN %s ON %s.%s = %s.%s " +
                        "WHERE %s.%s = ? AND %s.%s BETWEEN ? AND ? " +
                        "ORDER BY %s.%s",
                ConstantDB.CATEGORIES_TABLE, ConstantDB.TABLE_CATEGORY, ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_AMOUNT, ConstantDB.ACCOUNTINGS_TABLE,
                ConstantDB.CATEGORIES_TABLE, ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_CATEGORIES, ConstantDB.CATEGORIES_TABLE, ConstantDB.TABLE_CATEGORIES,
                ConstantDB.USER_TABLE, ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_USER_ID, ConstantDB.USER_TABLE, ConstantDB.TABLE_USER_ID,
                ConstantDB.USER_TABLE, ConstantDB.USERS_ID, ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_DATE,
                ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.TABLE_DATE
        );
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setLong(1, chatID);
            prSt.setString(2, firstDate);
            prSt.setString(3, secondDate);
            ResultSet resultSet = prSt.executeQuery();
            return summarizeExpenses(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    private Map<String, Double> summarizeExpenses(ResultSet resultSet) throws SQLException {
        Map<String, Double> categorySumMap = new HashMap<>();
        while (resultSet.next()) {
            String category = resultSet.getString(ConstantDB.TABLE_CATEGORY);
            double amount = resultSet.getDouble(ConstantDB.TABLE_AMOUNT);
            categorySumMap.put(category, categorySumMap.getOrDefault(category, 0.0) + amount);
        }
        return categorySumMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
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

    private List<String> parsePeriod(String period, int flag) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (period.matches("(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{4}-\\d{2}-\\d{2})") && flag == 2) {
            String[] splitDates = period.split("\\s+");
            for (String dateStr : splitDates) {
                try {
                    LocalDate date = LocalDate.parse(dateStr, formatter2);
                    if (!dateStr.equals(date.format(formatter2))) {
                        return Collections.emptyList();
                    }
                    dates.add(dateStr);
                } catch (DateTimeParseException e) {
                    return Collections.emptyList();
                }
            }
        } else if (period.matches("\\d{4}-\\d{2}") && flag == 1) {
            try {
                YearMonth yearMonth = YearMonth.parse(period, formatter1);
                if (!period.equals(yearMonth.format(formatter1))) {
                    return Collections.emptyList();
                }
                dates.add(yearMonth.atDay(1).format(formatter2));
                dates.add(yearMonth.atEndOfMonth().format(formatter2));
            } catch (DateTimeParseException e) {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
        Collections.sort(dates, (date1, date2) -> {
            LocalDate d1 = LocalDate.parse(date1, formatter2);
            LocalDate d2 = LocalDate.parse(date2, formatter2);
            return d1.compareTo(d2);
        });
        return dates;
    }

    private float parseFloat(String string_amount) {
        if (string_amount.matches("(\\d{1,12}(\\.[0-9]{1,2})?)") && !string_amount.matches("0")) {
            return Float.parseFloat(string_amount);
        } else {
            return -1;
        }
    }

    private String getCurrentData() {
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
        String currentData = getCurrentData();
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
            prSt.setString(3, currentData);
            prSt.setFloat(4, value);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void inputIncome(long chatID, String income, float value) {
        addNewIncome(income);
        String insert = String.format(
                "INSERT INTO %s(%s.%s,%s.%s,%s.%s) " +
                        "VALUES ((SELECT %s.%s FROM %s WHERE %s.%s = ?),(SELECT %s.%s FROM %s WHERE %s.%s = ?),?)",
                ConstantDB.REVENUE_TABLE,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_USER_ID,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_INCOME_ID,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_AMOUNT,
                ConstantDB.USER_TABLE, ConstantDB.TABLE_USER_ID,
                ConstantDB.USER_TABLE,
                ConstantDB.USER_TABLE, ConstantDB.USERS_ID,
                ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME_ID,
                ConstantDB.INCOMES_TABLE,
                ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME
        );
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setLong(1, chatID);
            prSt.setString(2, income);
            prSt.setFloat(3, value);
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
        messageSender.send(chatID, new Message("Ваш заработок в " + amount + " рублей был успешно записан\uD83C\uDF89"));
    }

    public void makeEntryAboutExpenses(long chatID, String stringAmount, String buttonInfo, MessageSender messageSender) throws SQLException {
        float amount = parseFloat(stringAmount);
        if (amount == -1) {
            messageSender.send(chatID, Constants.INVALID_SUM);
            return;
        }
        inputExpense(chatID, buttonInfo, amount);
        messageSender.send(chatID, new Message("Ваша сумма в " + amount + " рублей была успешно записана\uD83C\uDF89"));
    }

    public void makeEntryAboutExpenses(long chatID, String userInput, MessageSender messageSender) throws SQLException {
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
        makeEntryAboutExpenses(chatID, amount, category, messageSender);
    }

    public void makeStatisticAboutExpenses(long chatID, String period, int flag, MessageSender messageSender) throws SQLException {
        List<String> datesList = parsePeriod(period, flag);
        if (datesList.isEmpty()) {
            messageSender.send(chatID, Constants.INV_PERIOD);
            return;
        }
        Map<String, Double> categorySumMap = getAllAmounts(chatID, datesList);
        if (categorySumMap.isEmpty()) {
            messageSender.send(chatID, Constants.EMPTY_RESULT);
            return;
        }
        Map<String, String> expensesButtonMap = ButtonConfig.getExpensesButtonMap();
        StringBuilder response = new StringBuilder();
        Double total = 0.0D;
        response.append("\uD83D\uDCC9Расходы:\n---\n");
        for (Map.Entry<String, Double> entry : categorySumMap.entrySet()) {
            String category = entry.getKey();
            Double amount = entry.getValue();
            total += amount;
            String buttonText = expensesButtonMap.getOrDefault(category, "\uD83D\uDC64" + category);
            response.append(buttonText).append(":   ").append(amount).append("₽\n");
        }
        response.append("---\n\uD83E\uDEE3ИТОГО").append(":   ").append(total).append("₽\n");
        messageSender.send(chatID, new Message(response.toString()));
    }

}