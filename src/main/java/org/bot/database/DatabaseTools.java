package org.bot.database;

import org.bot.functional.ExpenseChart;
import org.bot.msg.MessageSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseTools extends Configs {
    private final Connection dbConnection;

    public DatabaseTools(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public ArrayList<Float> getAllAmounts(long chatID) throws SQLException {
        return null;
    }

    public void sendAllAmounts(long chatID, MessageSender messageSender, ArrayList<String> datesList) throws SQLException {

    }

    public ResultSet getUserCount(long chatID) {
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

    public ArrayList<String> parsePeriod(String period) {
        ArrayList<String> dates = new ArrayList<>();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM");
        if (period.matches("(\\d{4}-\\d{2}-\\d{2}) (\\d{4}-\\d{2}-\\d{2})")) {
            String[] splitDates = period.split(" ");
            for (String dateStr : splitDates) {
                try {
                    LocalDate date = LocalDate.parse(dateStr, formatter1);
                    dates.add(dateStr);
                } catch (DateTimeParseException e) {
                    return new ArrayList<>();
                }
            }
        } else if (period.matches("\\d{4}-\\d{2}")) {
            try {
                YearMonth yearMonth = YearMonth.parse(period, formatter2);
                dates.add(period);
            } catch (DateTimeParseException e) {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        if (dates.size() > 1) {
            Collections.sort(dates, (date1, date2) -> {
                LocalDate d1 = LocalDate.parse(date1, formatter1);
                LocalDate d2 = LocalDate.parse(date2, formatter1);
                return d1.compareTo(d2);
            });
        }
        return dates;
    }

    public float parseFloat(String string_amount) throws SQLException {
        if (string_amount.matches("(\\d+(\\.\\d+)?)") && !string_amount.matches("0")) {
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

    private boolean ifCategoryExists(String category) {
        String insert = "SELECT COUNT(*) FROM " + ConstantDB.CATEGORIES_TABLE + " WHERE " + ConstantDB.TABLE_CATEGORY + "=?";
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setString(1, category);
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

    private void addNewCategory(String category) {
        if (ifCategoryExists(category)) {
            return;
        }
        String insert = String.format("INSERT INTO %s(%s) VALUES (?)",
                ConstantDB.CATEGORIES_TABLE, ConstantDB.TABLE_CATEGORY);
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setString(1, category);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inputEntry(long chatID, String category, float insertable) {
        addNewCategory(category);
        String currentData = getCurrentData();
        String insert = String.format("INSERT INTO %s(%s,%s,%s,%s) " +
                        "VALUES ((SELECT %s FROM %s WHERE %s = ?),(SELECT %s FROM %s WHERE %s = ?),?,?)",
                ConstantDB.ACCOUNTINGS_TABLE, ConstantDB.ACCOUNTINGS_TABLE + '.' + ConstantDB.TABLE_USER_ID,
                ConstantDB.ACCOUNTINGS_TABLE + '.' + ConstantDB.TABLE_CATEGORIES,
                ConstantDB.ACCOUNTINGS_TABLE + '.' + ConstantDB.TABLE_DATE,
                ConstantDB.ACCOUNTINGS_TABLE + '.' + ConstantDB.TABLE_AMOUNT,
                ConstantDB.USER_TABLE + '.' + ConstantDB.TABLE_USER_ID, ConstantDB.USER_TABLE,
                ConstantDB.USER_TABLE + '.' + ConstantDB.USERS_ID,
                ConstantDB.CATEGORIES_TABLE + '.' + ConstantDB.TABLE_CATEGORIES, ConstantDB.CATEGORIES_TABLE,
                ConstantDB.CATEGORIES_TABLE + '.' + ConstantDB.TABLE_CATEGORY);
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setInt(1, (int) chatID);
            prSt.setString(2, category);
            prSt.setString(3, currentData);
            prSt.setFloat(4, insertable);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
