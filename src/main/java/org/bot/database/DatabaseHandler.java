package org.bot.database;

import java.sql.*;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void signUpUser(String telegramID) {
        String insert = String.format("INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)VALUES(?,0,0,0,0,0,0,0,0,0,0,0,none)",
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

    public ResultSet getUserCount(long chatID) {
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

    public ResultSet getField(long chatID, String field) {
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

    public void addToDatabase(long chatID, String Info , double amountValue){

    }
}