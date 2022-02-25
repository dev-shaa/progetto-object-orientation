package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import Exceptions.Database.DatabaseConnectionException;

/**
 * Controller per stabilire una connessione col database.
 */
public class ConnectionController {

	private static final String connectionURL = "jdbc:postgresql://localhost:5432/progetto";
	private static final String connectionUser = "postgres";
	private static final String connectionPassword = "tarallo";

	/**
	 * Ottiene una connessione per il database.
	 * 
	 * @return nuova connessione
	 * @throws DatabaseConnectionException
	 *             se non è possibile stabilire una connessione al database
	 */
	public static Connection getConnection() throws DatabaseConnectionException {
		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
		} catch (Exception e) {
			throw new DatabaseConnectionException("Impossibile stabilire una connessione al database.");
		}
	}

}