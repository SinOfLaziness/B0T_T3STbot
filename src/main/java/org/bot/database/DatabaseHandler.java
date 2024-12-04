package org.bot.database;

import org.bot.functional.ExpenseChart;
import org.bot.msg.Constants;
import org.bot.msg.MessageSender;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler extends Configs {
    private Connection dbConnection;
    private DatabaseTools databaseTools;

    public DatabaseHandler() {
        try {
            dbConnection = getDbConnection();
            databaseTools = new DatabaseTools(dbConnection);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

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
        try (PreparedStatement prSt = dbConnection.prepareStatement(insert)) {
            prSt.setString(1, telegramID);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfSigned(long chatID) throws SQLException {
        int counter = 0;
        try (ResultSet result = databaseTools.getUserCount(chatID)) {
            if (result.next()) {
                counter = result.getInt(1);
            }
        }
        return counter >= 1;
    }

    public void caseSignUpUsers(long chatID, MessageSender messageSender) {
        signUpUser(String.valueOf(chatID));
        messageSender.send(chatID, Constants.NOW_REG);
    }

    public DatabaseTools getDatabaseTools() {
        return databaseTools;
    }

}
