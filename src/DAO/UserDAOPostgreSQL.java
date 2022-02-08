package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.DatabaseController;
import Entities.User;
import Exceptions.DatabaseConnectionException;
import Exceptions.UserDatabaseException;

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

	public boolean ExistUser(User user) {

		return true;
		// String query = "select count(*) from \"UtenteApp\" ua \r\n" + "where \"Nome\" = '" + user.getName() + "'";
		// int risultato = 1;

		// try {
		// con = DatabaseController.getConnection();
		// if (con == null) {
		// System.out.println("Non c'� connesione al db");
		// return true;
		// }
		// stmt = con.createStatement();
		// rs = stmt.executeQuery(query);

		// while (rs.next()) {
		// risultato = rs.getInt("count");
		// System.out.printf("Ci sono: " + risultato);

		// }
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// if (risultato == 0) {

		// return false;

		// }

		// else {
		// return true;
		// }
	}

	public User GetUserLogin(User user) {

		return null;

		// if (!ExistUser(user)) {
		// return null;
		// }

		// String query = "select * from \"UtenteApp\" ua where \"Nome\" = '" + user.getName() + "'";
		// try {
		// con = DatabaseController.getConnection();
		// if (con == null) {
		// System.out.println("Non c'� connesione al db");
		// return null;
		// }
		// stmt = con.createStatement();
		// rs = stmt.executeQuery(query);
		// rs.next();
		// String username = rs.getString("Nome");
		// String password = rs.getString("Password");

		// User userDB = new User(username, password);
		// return userDB;

		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return null;
		// }

	}

}
