package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.DatabaseController;
import Entities.User;
import Exceptions.DatabaseConnectionException;
import Exceptions.UserDatabaseException;

/**
 * Implementazione dell'interfaccia {@code UserDAO} per database relazionali PostgreSQL.
 * 
 * @see UserDAO
 */
public class UserDAOPostgreSQL implements UserDAO {

	@Override
	public void register(User user) throws UserDatabaseException {
		Connection connection = null;
		Statement statement = null;
		String command = "insert into user_app(name, password) values('" + user.getName() + "','" + user.getPassword() + "')";

		try {
			connection = DatabaseController.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(command);
		} catch (SQLException | DatabaseConnectionException e) {
			e.printStackTrace();
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

	@Override
	public boolean doesUserExist(User user) throws UserDatabaseException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String query = "select * from user_app where name = '" + user.getName() + "' and password = '" + user.getPassword() + "'";

		try {
			connection = DatabaseController.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			return resultSet.next();
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
