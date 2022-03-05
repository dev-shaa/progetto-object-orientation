package RetrieveManagement.Connections;

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
	private CustomConnection transactionConnection;
	private int transactionConnectionKey;

	private static ConnectionController instance;

	private ConnectionController() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static ConnectionController getInstance() {
		if (instance == null)
			instance = new ConnectionController();

		return instance;
	}

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
	public void rollbackTransaction(int key) {
		try {
			if (isTransactionActive && key == transactionConnectionKey && transactionConnection != null)
				transactionConnection.rollback(key);
		} catch (Exception e) {
			// non fare niente
		}
	}

	private CustomConnection getTransactionConnection() throws SQLException {
		if (transactionConnection == null) {
			Connection connection = createConnection();
			transactionConnection = new CustomConnection(connection, transactionConnectionKey);
			transactionConnection.setAutoCommit(false);
		}

		return transactionConnection;
	}

	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
	}

	private int createKey() {
		return new Random().nextInt();
	}

}