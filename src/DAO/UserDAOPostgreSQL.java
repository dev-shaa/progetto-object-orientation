package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Controller.ConnectionController;
import Entities.User;
import Exceptions.Database.DatabaseConnectionException;
import Exceptions.Database.UserDatabaseException;

/**
 * Implementazione dell'interfaccia {@code UserDAO} per database relazionali PostgreSQL.
 * 
 * @see UserDAO
 */
public class UserDAOPostgreSQL implements UserDAO {

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException
	 *             se {@code user == null}
	 */
	@Override
	public void register(User user) throws UserDatabaseException {
		if (user == null)
			throw new IllegalArgumentException("user can't be null");

		Connection connection = null;
		PreparedStatement statement = null;
		String command = "insert into user_app(name, password) values(?, ?)";

		try {
			connection = ConnectionController.getConnection();
			statement = connection.prepareStatement(command);

			statement.setString(1, user.getName());
			statement.setString(2, user.getPassword());

			statement.executeUpdate();
		} catch (SQLException | DatabaseConnectionException e) {
			throw new UserDatabaseException("Impossibile registrare l'utente.");
		} finally {
			try {
				if (statement != null)
					statement.close();

				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// non fare niente
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException
	 *             se {@code user == null}
	 */
	@Override
	public boolean doesUserExist(User user) throws UserDatabaseException {
		if (user == null)
			throw new IllegalArgumentException("user can't be null");

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "select * from user_app where name = ? and password = ?";

		try {
			connection = ConnectionController.getConnection();
			statement = connection.prepareStatement(query);

			statement.setString(1, user.getName());
			statement.setString(2, user.getPassword());

			resultSet = statement.executeQuery();

			boolean doesUserExist = resultSet.next();

			return doesUserExist;
		} catch (SQLException | DatabaseConnectionException e) {
			e.printStackTrace();

			throw new UserDatabaseException("Impossibile controllare se l'utente esiste.");
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();

				if (statement != null)
					statement.close();

				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// non fare niente
			}

		}
	}

}
