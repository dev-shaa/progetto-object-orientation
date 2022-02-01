package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.DatabaseController;
import Entities.User;

public class UserDAO {
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	public void SaveUser(User user) {

		try {
			System.out.println("Sto provando a salvare l'utente " + user.getName() + " " + user.getPassword());
			String query = "INSERT INTO public.\"UtenteApp\" (\"Nome\", \"Password\") VALUES('" + user.getName() + "','"
					+ user.getPassword() + "')";

			con = DatabaseController.getConnection();
			if (con == null) {
				System.out.println("Non c'� connesione al db");
				return;
			}
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			System.out.println("Utente aggiunto con successo");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean ExistUser(User user) {

		String query = "select count(*) from \"UtenteApp\" ua \r\n" + "where  \"Nome\" = '" + user.getName() + "'";
		int risultato = 1;

		try {
			con = DatabaseController.getConnection();
			if (con == null) {
				System.out.println("Non c'� connesione al db");
				return true;
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				risultato = rs.getInt("count");
				System.out.printf("Ci sono: " + risultato);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (risultato == 0) {

			return false;

		}

		else {
			return true;
		}
	}

	public User GetUserLogin(User user) {

		if (!ExistUser(user)) {
			return null;
		}

		String query = "select * from \"UtenteApp\" ua where \"Nome\" = '" + user.getName() + "'";
		try {
			con = DatabaseController.getConnection();
			if (con == null) {
				System.out.println("Non c'� connesione al db");
				return null;
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			String username = rs.getString("Nome");
			String password = rs.getString("Password");

			User userDB = new User(username, password);
			return userDB;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
