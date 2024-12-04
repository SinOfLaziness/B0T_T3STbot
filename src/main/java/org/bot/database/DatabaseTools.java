package org.bot.database;

import org.bot.functional.ExpenseChart;
import org.bot.msg.Constants;
import org.bot.msg.MessageSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public String getStringField(long chatID, String field) throws SQLException {
        ResultSet result = getField(chatID, field);
        String value = "";
        try {
            if (result.next()) {
                value = result.getString(2);
            }
        } finally {
            result.close();
        }
        return value;
    }

    public void InputFloatField(long chatID, String field, float insertable) {
        String insert = String.format("UPDATE %s SET %s = %s WHERE %s =?",
                ConstantDB.USER_TABLE, field, insertable, ConstantDB.USERS_ID);
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setString(1, String.valueOf(chatID));
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

}
