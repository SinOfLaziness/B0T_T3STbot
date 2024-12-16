package com.github.FinancialAssistant.database;

import com.github.FinancialAssistant.functional.ButtonConfig;
import com.github.FinancialAssistant.msg.Constants;
import com.github.FinancialAssistant.msg.Message;
import com.github.FinancialAssistant.msg.MessageSender;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseStatistics extends Configs {
    private final Connection dbConnection;

    public DatabaseStatistics(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private List<String> parsePeriod(String period) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (period.matches("(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{4}-\\d{2}-\\d{2})")) {
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
        } else if (period.matches("\\d{4}-\\d{2}")) {
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

    private Map<String, Double> getAllExpenses(long chatID, List<String> datesList) throws SQLException {
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
            return summarizeResult(resultSet, ConstantDB.TABLE_CATEGORY, ConstantDB.TABLE_AMOUNT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    private Map<String, Double> getAllIncomes(long chatID, List<String> datesList) throws SQLException {
        String firstDate = datesList.get(0);
        String secondDate = datesList.get(1);
        String insert = String.format(
                "SELECT %s.%s, %s.%s FROM %s " +
                        "JOIN %s ON %s.%s = %s.%s " +
                        "JOIN %s ON %s.%s = %s.%s " +
                        "WHERE %s.%s = ? AND %s.%s BETWEEN ? AND ? " +
                        "ORDER BY %s.%s",
                ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_AMOUNT,
                ConstantDB.REVENUE_TABLE,
                ConstantDB.INCOMES_TABLE, ConstantDB.INCOMES_TABLE, ConstantDB.TABLE_INCOME_ID, ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_INCOME_ID,
                ConstantDB.USER_TABLE, ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_USER_ID, ConstantDB.USER_TABLE, ConstantDB.TABLE_USER_ID,
                ConstantDB.USER_TABLE, ConstantDB.USERS_ID, ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE,
                ConstantDB.REVENUE_TABLE, ConstantDB.TABLE_DATE
        );
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setLong(1, chatID);
            prSt.setString(2, firstDate);
            prSt.setString(3, secondDate);
            ResultSet resultSet = prSt.executeQuery();
            return summarizeResult(resultSet, ConstantDB.TABLE_INCOME, ConstantDB.TABLE_AMOUNT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }


    private Map<String, Double> summarizeResult(ResultSet resultSet, String columnName1, String columnName2) throws SQLException {
        Map<String, Double> categorySumMap = new HashMap<>();
        while (resultSet.next()) {
            String category = resultSet.getString(columnName1);
            double amount = resultSet.getDouble(columnName2);
            categorySumMap.put(category, categorySumMap.getOrDefault(category, 0.0) + amount);
        }
        return categorySumMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void makeStatisticAboutExpenses(long chatID, String period, MessageSender messageSender) throws SQLException {
        List<String> datesList = parsePeriod(period);
        if (datesList.isEmpty()) {
            messageSender.send(chatID, Constants.INV_PERIOD);
            return;
        }
        String firstDate = datesList.get(0);
        String secondDate = datesList.get(1);
        Map<String, Double> categorySumMap = getAllExpenses(chatID, datesList);
        if (categorySumMap.isEmpty()) {
            messageSender.send(chatID, Constants.EMPTY_RESULT);
            return;
        }
        Map<String, String> expensesButtonMap = ButtonConfig.getExpensesButtonMap();
        StringBuilder response = new StringBuilder();
        Double total = 0.0D;
        response.append(String.format("\uD83D\uDCC9Расходы c %s по %s:\n---\n", firstDate, secondDate));
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

    public void makeStatisticAboutIncome(long chatID, String period, MessageSender messageSender) throws SQLException {
        List<String> datesList = parsePeriod(period);
        if (datesList.isEmpty()) {
            messageSender.send(chatID, Constants.INV_PERIOD);
            return;
        }
        String firstDate = datesList.get(0);
        String secondDate = datesList.get(1);
        Map<String, Double> incomeSumMap = getAllIncomes(chatID, datesList);
        if (incomeSumMap.isEmpty()) {
            messageSender.send(chatID, Constants.NO_INCOMES);
            return;
        }
        Map<String, String> incomeButtonMap = ButtonConfig.getIncomeButtonMap();
        StringBuilder response = new StringBuilder();
        Double total = 0.0D;
        response.append(String.format("\uD83D\uDCC8Доходы за период с %s по %s:\n---\n", firstDate, secondDate));
        for (Map.Entry<String, Double> entry : incomeSumMap.entrySet()) {
            String category = entry.getKey();
            Double amount = entry.getValue();
            total += amount;
            String buttonText = incomeButtonMap.getOrDefault(category, "\uD83D\uDC64" + category);
            response.append(buttonText).append(":   ").append(amount).append("₽\n");
        }
        response.append("---\n\uD83E\uDD2DИТОГО").append(":   ").append(total).append("₽\n");
        messageSender.send(chatID, new Message(response.toString()));
    }

    public void makeStatisticAboutTotal(long chatID, String period, MessageSender messageSender) throws SQLException {
        List<String> datesList = parsePeriod(period);
        if (datesList.isEmpty()) {
            messageSender.send(chatID, Constants.INV_PERIOD);
            return;
        }
        Map<String, Double> expensesMap = getAllExpenses(chatID, datesList);
        Map<String, Double> incomesMap = getAllIncomes(chatID, datesList);
        if (expensesMap.isEmpty() && incomesMap.isEmpty()) {
            messageSender.send(chatID, Constants.NO_DATA);
            return;
        }
        double totalExpenses = 0.0;
        double totalIncome = 0.0;
        for (Map.Entry<String, Double> entry : expensesMap.entrySet()) {
            totalExpenses += entry.getValue();
        }
        for (Map.Entry<String, Double> entry : incomesMap.entrySet()) {
            totalIncome += entry.getValue();
        }
        double total = totalIncome - totalExpenses;
        BigDecimal totalOut = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
        StringBuilder response = new StringBuilder();
        response.append(String.format("\uD83D\uDCCAОтчёт за период с %s по %s:\n---\n", datesList.get(0), datesList.get(1)));
        response.append("\uD83D\uDCC8Доходы: ").append(totalIncome).append("₽\n\n");
        response.append("\uD83D\uDCC9Расходы: ").append(totalExpenses).append("₽\n");
        if (total > 0) {
            response.append("---\n\uD83D\uDFE2ИТОГО").append(":   ").append(totalOut).append("₽\n\n").append(Constants.GOOD_REPORT);
        } else if (total < 0) {
            response.append("---\n\uD83D\uDD34ИТОГО").append(":   ").append(totalOut).append("₽\n\n").append(Constants.BAD_REPORT);
        } else {
            response.append("---\n\uD83D\uDD35ИТОГО").append(":   ").append(totalOut).append("₽\n\n").append(Constants.NOT_BAD_REPORT);
        }
        messageSender.send(chatID, new Message(response.toString()));
    }

}
