package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import Exceptions.DatabaseConnectionException;

public class DatabaseController {

	// @R1ccardo puoi cambiare le credenziali per accedere

	private static final String connectionURL = "jdbc:postgresql://localhost:5432/progetto";
	private static final String connectionUser = "postgres";
	private static final String connectionPassword = "tarallo";

	public static Connection getConnection() throws DatabaseConnectionException {
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
			return connection;
		} catch (Exception e) {
			throw new DatabaseConnectionException("Impossibile stabilire una connessione al database");
		}
	}
}