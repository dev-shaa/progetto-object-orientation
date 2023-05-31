package Database.Connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

/**
 * Controller per stabilire una connessione col database.
 */
public class ConnectionController {

	private final String connectionURL = "jdbc:postgresql://localhost:5432/progetto";
	private final String connectionUser = "postgres";
	private final String connectionPassword = "tarallo";

	private boolean isTransactionActive = false;
	private int transactionConnectionKey;
	private CustomConnection currentTransactionConnection;

	private static ConnectionController instance;

	private ConnectionController() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Restituisce l'istanza di questa classe.
	 * 
	 * @return istanza di questa classe
	 */
	public static ConnectionController getInstance() {
		if (instance == null)
			instance = new ConnectionController();

		return instance;
	}

	/**
	 * Ottiene una connessione al database.
	 * <p>
	 * Se è attiva una transazione, restituisce la connessione attiva.
	 * 
	 * @return connessione al database
	 * @throws SQLException
	 *             se si verifica un errore di accesso al database
	 */
	public CustomConnection getConnection() throws SQLException {
		CustomConnection connection;

		if (isTransactionActive)
			connection = getTransactionConnection();
		else
			connection = new CustomConnection(createConnection());

		return connection;
	}

	/**
	 * Inizia una transazione.
	 * <p>
	 * Se viene chiamata dopo l'inizio di un'altra transazione, restituisce la chiave della precedente.
	 * 
	 * @return chiave della transazione
	 */
	public int beginTransaction() {
		if (!isTransactionActive) {
			transactionConnectionKey = createKey();
			isTransactionActive = true;
		}

		return transactionConnectionKey;
	}

	/**
	 * Chiude una transazione.
	 * 
	 * @param key
	 *            chiave della transazione
	 */
	public void closeTransaction(int key) {
		try {
			if (isTransactionActive && key == transactionConnectionKey && currentTransactionConnection != null) {
				currentTransactionConnection.close(key);
				isTransactionActive = false;
				currentTransactionConnection = null;
			}
		} catch (Exception e) {
			// non fare niente
		}
	}

	/**
	 * Esegue il commit della transazione.
	 * 
	 * @param key
	 *            chiave della transazione
	 */
	public void commitTransaction(int key) {
		try {
			if (isTransactionActive && key == transactionConnectionKey && currentTransactionConnection != null)
				currentTransactionConnection.commit(key);
		} catch (SQLException e) {
			// non fare niente
		}
	}

	/**
	 * Esegue il rollback della transazione.
	 * 
	 * @param key
	 *            chiave della transazione
	 */
	public void rollbackTransaction(int key) {
		try {
			if (isTransactionActive && key == transactionConnectionKey && currentTransactionConnection != null)
				currentTransactionConnection.rollback(key);
		} catch (SQLException e) {
			// non fare niente
		}
	}

	private CustomConnection getTransactionConnection() throws SQLException {
		if (currentTransactionConnection == null) {
			Connection connection = createConnection();
			currentTransactionConnection = new CustomConnection(connection, transactionConnectionKey);
			currentTransactionConnection.setAutoCommit(false);
		}

		return currentTransactionConnection;
	}

	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
	}

	private int createKey() {
		return new Random().nextInt();
	}

}