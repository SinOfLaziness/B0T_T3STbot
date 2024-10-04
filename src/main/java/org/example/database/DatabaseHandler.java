package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler extends Configs
{
    Connection dbConnection;
    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException
    {
        String connectionString = String.format("jdbc:mysql://%s:%s/%sautoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC?",
                dbHost , dbPort , dbName);
        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }
    public void signUpUser(String telegramID){
        String insert = String.format("INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)VALUES(?,0,0,0,0,0,0,0,0,0,0,0)",
                Const.USER_TABLE, Const.USERS_ID, Const.USERS_BOOKS , Const.USERS_COSMETICS , Const.USERS_FOOD ,
                Const.USERS_ELECTRONICS_AND_TECHNOLOGY , Const.USERS_ENTERTAINMENT , Const.USERS_HOME_AND_RENOVATION ,
                Const.USERS_ITEMS_OF_CLOTHING , Const.USERS_PHARMACIES , Const.USERS_SOUVENIRS , Const.USERS_SUPERMARKETS ,
                Const.USERS_TRANSPORT);
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, telegramID);

            prSt.executeUpdate();
        } catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
