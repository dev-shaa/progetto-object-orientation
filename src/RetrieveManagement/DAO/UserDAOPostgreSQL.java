package RetrieveManagement.DAO;

import java.sql.*;

import Entities.User;
import Exceptions.Database.UserDatabaseException;
import RetrieveManagement.Connections.ConnectionController;
import RetrieveManagement.Connections.CustomConnection;

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

		CustomConnection connection = null;
		PreparedStatement statement = null;
		String command = "insert into user_app(name, password) values(?, ?)";

		try {
			connection = ConnectionController.getInstance().getConnection();
			statement = connection.prepareStatement(command);

			statement.setString(1, user.getName());
			statement.setString(2, user.getPassword());

			statement.executeUpdate();
		} catch (SQLException e) {
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

		CustomConnection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "select * from user_app where name = ? and password = ?";

		try {
			connection = ConnectionController.getInstance().getConnection();
			statement = connection.prepareStatement(query);

			statement.setString(1, user.getName());
			statement.setString(2, user.getPassword());

			resultSet = statement.executeQuery();

			return resultSet.next();
		} catch (SQLException e) {
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
