package lesson_24;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseManager {


    private static final String URL = "jdbc:postgresql://localhost:55000/ok";
    private static final String USER = "ok";
    private static final String PASSWORD = "ok";

    private static Connection connection;

    private DataBaseManager() {
        //prive constructor to prevent instantiation
    }

    public static Connection getConnection() {
        if (connection == null ) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("connected");
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
