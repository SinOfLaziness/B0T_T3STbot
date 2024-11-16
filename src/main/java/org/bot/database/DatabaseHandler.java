package org.bot.database;

import java.sql.*;

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
                ConstantDB.USER_TABLE, ConstantDB.USERS_ID, ConstantDB.USERS_BOOKS , ConstantDB.USERS_COSMETICS , ConstantDB.USERS_FOOD ,
                ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY , ConstantDB.USERS_ENTERTAINMENT , ConstantDB.USERS_HOME_AND_RENOVATION ,
                ConstantDB.USERS_ITEMS_OF_CLOTHING , ConstantDB.USERS_PHARMACIES , ConstantDB.USERS_SOUVENIRS , ConstantDB.USERS_SUPERMARKETS ,
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

    public void addToDatabase(long chatID, String Info , double amountValue) {
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

    public String getAllAmounts(long chatID) throws SQLException {
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
        String out = "Все записанные расходы: \n";
        String[] list_type_amounts = {ConstantDB.HOME_AND_RENOVATION, ConstantDB.TRANSPORT, ConstantDB.FOOD,
                ConstantDB.ENTERTAINMENT, ConstantDB.PHARMACIES, ConstantDB.COSMETICS, ConstantDB.ITEMS_OF_CLOTHING,
                ConstantDB.SUPERMARKETS, ConstantDB.SOUVENIRS, ConstantDB.ELECTRONICS_AND_TECHNOLOGY, ConstantDB.BOOKS};
        String[] list_amounts = {ConstantDB.USERS_HOME_AND_RENOVATION, ConstantDB.USERS_TRANSPORT, ConstantDB.USERS_FOOD,
                ConstantDB.USERS_ENTERTAINMENT, ConstantDB.USERS_PHARMACIES, ConstantDB.USERS_COSMETICS,
                ConstantDB.USERS_ITEMS_OF_CLOTHING, ConstantDB.USERS_SUPERMARKETS, ConstantDB.USERS_SOUVENIRS,
                ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY, ConstantDB.USERS_BOOKS};
        for (int i = 0; i < list_type_amounts.length; ++i)
        {
            out = String.format("%s%s: %s\n",out, list_type_amounts[i], getFloatField(chatID, list_amounts[i]));
        }
        return out;
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

    public void InputFloatField(long chatID, String field, float insertable){
        String insert = String.format("UPDATE %s SET %s = %s WHERE %s =?",
                ConstantDB.USER_TABLE, field, insertable ,ConstantDB.USERS_ID);
        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(insert)) {
            prSt.setString(1, String.valueOf(chatID));
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}