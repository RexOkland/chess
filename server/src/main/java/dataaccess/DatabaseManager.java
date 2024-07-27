package dataaccess;

import com.google.gson.Gson;
import models.AuthData;
import models.GameData;
import models.UserData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void createChessTables() throws DataAccessException {
        String initialStatement = "USE " + DATABASE_NAME + ";";
        String strA = "CREATE TABLE IF NOT EXISTS user (" +
                "username VARCHAR(250) PRIMARY KEY," +
                "password VARCHAR(250)," +
                "email VARCHAR(250)" +
                ");";
        String strB = "CREATE TABLE IF NOT EXISTS authentication (" +
                "token VARCHAR(250) PRIMARY KEY," +
                "username VARCHAR(250)" +
                ");";
        String strC = "CREATE TABLE IF NOT EXISTS game (" +
                "gameID INT PRIMARY KEY," +
                "whiteUsername VARCHAR(250)," +
                "blackUsername VARCHAR(250)," +
                "gameName VARCHAR(250)," +
                "gameData BLOB" +
                ");";

        try (var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            try (var statement = conn.createStatement()) {
                // This is the 'use' statement - identifies the database //
                statement.execute(initialStatement);

                // Creating the individual tables in identified database //
                statement.executeUpdate(strA);
                statement.executeUpdate(strB);
                statement.executeUpdate(strC);
            }
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw new DataAccessException("Error creating tables: " + e.getMessage());
        }
    }

    public static void addToSQL(Object loQueSea) throws DataAccessException{
        String initialStatement = "USE " + DATABASE_NAME + ";";
        String secondStatement;

        try (var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            try (var statement = conn.createStatement()) {

                if(loQueSea.getClass() == AuthData.class){
                    secondStatement = "INSERT INTO authentication (token, username) VALUES (?, ?)";
                    var preparedStatement = conn.prepareStatement(secondStatement);
                    preparedStatement.setString(1, ((AuthData) loQueSea).authToken());
                    preparedStatement.setString(2, ((AuthData) loQueSea).username());
                }
                else if(loQueSea.getClass() == GameData.class){
                    secondStatement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameData)"
                            + " VALUES (?, ?, ?, ?, ?)";
                    var preparedStatement = conn.prepareStatement(secondStatement);
                    preparedStatement.setInt(1, ((GameData) loQueSea).gameID());
                    preparedStatement.setString(2, ((GameData) loQueSea).whiteUsername());
                    preparedStatement.setString(3, ((GameData) loQueSea).blackUsername());
                    preparedStatement.setString(4, ((GameData) loQueSea).gameName());
                    //serialize the chessGame for the fifth parameter//
                    Gson gson = new Gson();
                    String gameData = gson.toJson(((GameData) loQueSea).game());
                    preparedStatement.setString(5, gameData);
                }
                else if(loQueSea.getClass() == UserData.class){
                    secondStatement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
                    var preparedStatement = conn.prepareStatement(secondStatement);
                    preparedStatement.setString(1, ((UserData) loQueSea).username());
                    preparedStatement.setString(2, ((UserData) loQueSea).password());
                    //TODO: encrypt this guy^//
                    preparedStatement.setString(3, ((UserData) loQueSea).email());
                }
                else{
                    throw new DataAccessException("table not found");
                }
                // This is the 'use' statement - identifies the database //
                statement.execute(initialStatement);
                // inserts data into corresponding table//
                statement.executeUpdate(secondStatement);
            }
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw new DataAccessException("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
