package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 */
public class CustomConnection {

    private Connection connection;
    private boolean needsKey;
    private int key;

    /**
     * 
     * @param connection
     * @throws IllegalArgumentException
     */
    public CustomConnection(Connection connection) {
        if (connection == null)
            throw new IllegalArgumentException("connection can't be null");

        this.connection = connection;
        this.needsKey = false;
    }

    /**
     * 
     * @param connection
     * @param key
     */
    public CustomConnection(Connection connection, int key) {
        this(connection);

        needsKey = true;
        this.key = key;
    }

    /** */
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    /**
     * 
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int arg1) throws SQLException {
        return connection.prepareStatement(sql, arg1);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    public void close() throws SQLException {
        if (!needsKey)
            connection.close();
    }

    public void close(int key) throws SQLException {
        if (!needsKey || this.key == key)
            connection.close();
    }

    /** */
    public void rollback() throws SQLException {
        if (!needsKey)
            connection.rollback();
    }

    /**
     * 
     * @param key
     * @throws SQLException
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
     * 
     * @param key
     * @throws SQLException
     */
    public void commit(int key) throws SQLException {
        if (!needsKey || this.key == key)
            connection.commit();
    }

}