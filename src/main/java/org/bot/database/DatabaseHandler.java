package org.bot.database;

import org.bot.functional.ExpenseChart;
import org.bot.msg.MessageSender;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void signUpUser(String telegramID) {
        String insert = String.format("INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)VALUES(?,0,0,0,0,0,0,0,0,0,0,0,0)",
                ConstantDB.USER_TABLE, ConstantDB.USERS_ID, ConstantDB.USERS_BOOKS, ConstantDB.USERS_COSMETICS, ConstantDB.USERS_FOOD,
                ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY, ConstantDB.USERS_ENTERTAINMENT, ConstantDB.USERS_HOME_AND_RENOVATION,
                ConstantDB.USERS_ITEMS_OF_CLOTHING, ConstantDB.USERS_PHARMACIES, ConstantDB.USERS_SOUVENIRS, ConstantDB.USERS_SUPERMARKETS,
                ConstantDB.USERS_TRANSPORT, ConstantDB.USERS_CONDITION);
        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(insert)) {
            prSt.setString(1, telegramID);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
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

    public void addToDatabase(long chatID, String Info, double amountValue) {
        // TO DO:
    }

    private ResultSet getUserCount(long chatID) {
        ResultSet resultSet = null;
        String insert = "SELECT COUNT(*) FROM " + ConstantDB.USER_TABLE + " WHERE " + ConstantDB.USERS_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setLong(1, chatID);
            resultSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private ResultSet getField(long chatID, String field) {
        ResultSet resultSet = null;
        String insert = String.format("SELECT %s, %s FROM %s WHERE %s =?", ConstantDB.USERS_ID, field, ConstantDB.USER_TABLE, ConstantDB.USERS_ID);
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setLong(1, chatID);
            resultSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
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
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setLong(1, chatID);
            resultSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
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
        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(insert)) {
            prSt.setString(1, String.valueOf(chatID));
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
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
}