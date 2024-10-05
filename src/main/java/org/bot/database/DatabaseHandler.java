package org.bot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName +
                "?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void signUpUser(String telegramID) {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" + Const.USERS_ID + ")" + " VALUES(?)";
        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(insert)) {
            prSt.setString(1, telegramID);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(String telegramID) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USERS_ID + "=?";
        try{
            PreparedStatement prSt = getDbConnection().prepareStatement(select) ;
            prSt.setString(1, telegramID);
            resultSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}