package org.bot.database;

import org.bot.functional.ExpenseChart;
import org.bot.msg.MessageSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseTools extends Configs {
    private final Connection dbConnection;

    public DatabaseTools(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private ResultSet getField(long chatID, String field) {
        ResultSet resultSet = null;
        String insert = String.format("SELECT %s, %s FROM %s WHERE %s =?", ConstantDB.USERS_ID, field, ConstantDB.USER_TABLE, ConstantDB.USERS_ID);
        try {
            PreparedStatement prSt = dbConnection.prepareStatement(insert);
            prSt.setLong(1, chatID);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Float getFloatField(long chatID, String field) throws SQLException {
        ResultSet result = getField(chatID, field);
        float value = 0.0F;
        try {
            if (result.next()) {
                value = result.getFloat(2);
            }
        } finally {
            result.close();
        }
        return value;
    }

    public ArrayList<Float> getAllAmounts(long chatID) throws SQLException {
        ResultSet resultSet = null;
        String insert = String.format("SELECT * FROM %s WHERE %s =?",
                ConstantDB.USER_TABLE, ConstantDB.USERS_ID);
        try {
            PreparedStatement prSt = dbConnection.prepareStatement(insert);
            prSt.setLong(1, chatID);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Float> all_amounts = new ArrayList<Float>();
        for (int i = 0; i < ConstantDB.list_type_amounts.length; ++i) {
            all_amounts.add(getFloatField(chatID, ConstantDB.list_amounts[i]));
        }
        return all_amounts;
    }

    public void sendAllAmounts(long chatID, MessageSender messageSender) throws SQLException {
        ArrayList<Float> all_amounts = getAllAmounts(chatID);
        ExpenseChart chart = new ExpenseChart();
        String out = "Все записанные расходы: \n";
        for (int i = 0; i < all_amounts.size(); ++i)
            out = String.format("%s%s: %s\n", out, ConstantDB.list_type_amounts[i],
                    all_amounts.get(i));
        messageSender.sendPhoto(chatID, chart.createChart(all_amounts), out);
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

    public float parseFloat(String string_amount) throws SQLException {
        int iFlag = 0;
        if (string_amount.matches("(\\d+(\\.\\d+)?)+ .*?") ||
                string_amount.matches("\\d+ .*?")) {
            iFlag = 1;
        } else if (string_amount.matches("(\\d+(\\.\\d+)?)+") ||
                string_amount.matches("\\d+")) {
            iFlag = 2;
        } else {
            return -1;
        }
        float amount = 0;
        if (iFlag == 1) {
            amount = Float.parseFloat(string_amount.split(" ")[0]);
        } else if (iFlag == 2) {
            amount = Float.parseFloat(string_amount);
        }
        return amount;
    }

    private String getCurrentData() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formattedDatePattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
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
