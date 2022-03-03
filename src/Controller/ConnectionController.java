package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import Exceptions.Database.DatabaseConnectionException;

/**
 * Controller per stabilire una connessione col database.
 */
public class ConnectionController {

	private static final String connectionURL = "jdbc:postgresql://localhost:5432/progetto";
	private static final String connectionUser = "postgres";
	private static final String connectionPassword = "tarallo";

	private static boolean isTransactionActive = false;

	private static CustomConnection transactionConnection;
	private static int transactionConnectionKey;

	/**
	 * Ottiene una connessione per il database.
	 * 
	 * @return nuova connessione
	 * @throws DatabaseConnectionException
	 *             se non è possibile stabilire una connessione al database
	 */
	public static CustomConnection getConnection() throws DatabaseConnectionException {
		try {
			Class.forName("org.postgresql.Driver");

			CustomConnection connection;

			if (isTransactionActive)
				connection = getTransactionConnection();
			else
				connection = new CustomConnection(createConnection());

			return connection;
		} catch (Exception e) {
			throw new DatabaseConnectionException("Impossibile stabilire una connessione al database.");
		}
	}

	/**
	 * TODO: commenta
	 * 
	 * @return
	 */
	public static int beginTransaction() {
		if (!isTransactionActive) {
			transactionConnectionKey = createKey();
			isTransactionActive = true;
		}

		return transactionConnectionKey;
	}

	/**
	 * TODO: commenta
	 * 
	 * @param key
	 */
	public static void closeTransaction(int key) {
		try {
			if (isTransactionActive && key == transactionConnectionKey && transactionConnection != null) {
				transactionConnection.close(key);
				isTransactionActive = false;
				transactionConnection = null;
			}
		} catch (Exception e) {
			// non fare niente
		}
	}

	/**
	 * TODO: commenta
	 * 
	 * @param key
	 */
	public static void rollbackTransaction(int key) {
		try {
			if (isTransactionActive && key == transactionConnectionKey && transactionConnection != null)
				transactionConnection.rollback(key);
		} catch (Exception e) {
			// non fare niente
		}
	}

	private static CustomConnection getTransactionConnection() throws SQLException {
		if (transactionConnection == null) {
			Connection connection = createConnection();
			transactionConnection = new CustomConnection(connection, transactionConnectionKey);
			transactionConnection.setAutoCommit(false);
		}

		return transactionConnection;
	}

	private static Connection createConnection() throws SQLException {
		return DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
	}

	private static int createKey() {
		return new Random().nextInt();
	}

}