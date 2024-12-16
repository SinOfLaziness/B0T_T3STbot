package com.github.FinancialAssistant.database;

import java.sql.*;

public class DatabaseInitializer extends Configs {
    private Connection dbConnection;
    private DatabaseTools databaseTools;
    private DatabaseStatistics databaseStatistics;

    public DatabaseInitializer() {
        try {
            dbConnection = getDbConnection();
            databaseTools = new DatabaseTools(dbConnection);
            databaseStatistics = new DatabaseStatistics(dbConnection);
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


    public DatabaseTools getDatabaseTools() {
        return databaseTools;
    }

    public DatabaseStatistics getDatabaseStatistics() {
        return databaseStatistics;
    }

}
