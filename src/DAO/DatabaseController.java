package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseController {
    private final String connectionURL = "jdbc:postgresql://localhost:5432/url";
    private final String connectionUser = "user";
    private final String connectionPassword = "password";

    private static DatabaseController Instance = null;

    public static DatabaseController getInstance() throws Exception {
        if (Instance == null)
            Instance = new DatabaseController();

        return Instance;
    }

    private DatabaseController() throws Exception {
        Class.forName("org.postgresql.Driver");
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
    }

}