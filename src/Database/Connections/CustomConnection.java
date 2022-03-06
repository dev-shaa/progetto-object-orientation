package Database.Connections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Un wrapper di {@code Connection} che permette di specificare una chiave per poterla chiudere, eseguire commit o rollback
 */
public class CustomConnection {

    private Connection connection;
    private boolean needsKey;
    private int key;

    /**
     * Crea una nuova connessione che non ha bisogno di chiavi per chiuderla.
     * 
     * @param connection
     *            connessione vera da usare
     * @throws IllegalArgumentException
     *             se {@code connection == null}
     */
    public CustomConnection(Connection connection) {
        if (connection == null)
            throw new IllegalArgumentException("connection can't be null");

        this.connection = connection;
        this.needsKey = false;
    }

    /**
     * Crea una nuova connessione per cui è necessario specificare una chiave per chiuderla.
     * 
     * @param connection
     *            connessione vera da usare
     * @param key
     *            chiave della connessione
     * @throws IllegalArgumentException
     *             se {@code connection == null}
     */
    public CustomConnection(Connection connection, int key) {
        this(connection);

        needsKey = true;
        this.key = key;
    }

    /**
     * Crea uno {@code Statement}.
     * 
     * @return un nuovo {@code Statement}
     * @throws SQLException
     *             se si verifica un errore di accesso al database
     */
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    /**
     * Crea un {@code PreparedStatement}.
     * 
     * @param sql
     *            query da preparare
     * @return un nuovo {@code PreparedStatement}
     * @throws SQLException
     *             se si verifica un errore di accesso al database
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * Crea un {@code PreparedStatement}.
     * 
     * @param sql
     *            query da preparare
     * @param arg1
     *            parametri opzionali
     * @return un nuovo {@code PreparedStatement}
     * @throws SQLException
     *             se si verifica un errore di accesso al database
     */
    public PreparedStatement prepareStatement(String sql, int arg1) throws SQLException {
        return connection.prepareStatement(sql, arg1);
    }

    /**
     * Imposta la modalità di commmit.
     * 
     * @param autoCommit
     *            se dovrebbe eseguire il commit automaticamente
     * @throws SQLException
     *             se si verifica un errore di accesso al database
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    /**
     * Chiude la connessione, se non è necessaria una chiave.
     * 
     * @throws SQLException
     *             se si verifica un errore di database
     */
    public void close() throws SQLException {
        if (!needsKey)
            connection.close();
    }

    /**
     * Chiude la connessione, se non è necessaria una chiave o se la chiave di input è esatta.
     * 
     * @param key
     *            chiave da controllare
     * @throws SQLException
     *             se si verifica un errore di database
     */
    public void close(int key) throws SQLException {
        if (!needsKey || this.key == key)
            connection.close();
    }

    /**
     * Annulla tutte le modifiche eseguite nella transazione corrente.
     * <p>
     * Se questa connessione ha bisogno di una chiave, non effettua nulla.
     * 
     * @throws SQLException
     *             se si verifica un errore di accesso al database
     */
    public void rollback() throws SQLException {
        if (!needsKey)
            connection.rollback();
    }

    /**
     * Annulla tutte le modifiche eseguite nella transazione corrente, se non è necessaria una chiave o se la chiave di input è esatta.
     * 
     * @param key
     *            chiave da controllare
     * @throws SQLException
     *             se si verifica un errore di accesso al database
     */
    public void rollback(int key) throws SQLException {
        if (!needsKey || this.key == key)
            connection.rollback();
    }

    /**
     * Effettua tutti i cambiamenti dal precedente commit o rollback.
     * <p>
     * Se questa connessione ha bisogno di una chiave, non effettua nulla.
     * 
     * @throws SQLException
     *             se si verifica un errore di accesso al database
     */
    public void commit() throws SQLException {
        if (!needsKey)
            connection.commit();
    }

    /**
     * Effettua tutti i cambiamenti della transazione dal precedente commit o rollback, se non è necessaria o se la chiave di input è esatta.
     * 
     * @param key
     *            chiave della transazione
     * @throws SQLException
     *             se si verifica un errore di accesso al database
     */
    public void commit(int key) throws SQLException {
        if (!needsKey || this.key == key)
            connection.commit();
    }

}