package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Controller.DatabaseController;
import Entities.Author;
import Entities.References.BibliographicReference;

public class AuthorDAO {

	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	public void SaveAuthor(Author author) {

		try {
			System.out.println("Salvataggio autore " + author.getName() + " " + author.getORCID());
			String query = "insert into \"AutoriApp\" (\"Nome\",\"ORCID\") values ('" + author.getName() + "','" + author.getORCID() + "')";

			con = DatabaseController.getConnection();
			if (con == null) {
				System.out.println("Non c'� connesione al db");
				return;
			}
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			System.out.println("Autore aggiunto con successo");

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

	// @R1ccardo ho commentato per debug

	public ArrayList<Author> FindAllAuthor() {
		return null;
		// try {
		// String query = "select * from \"AutoriApp\" aa";
		// con = DatabaseController.getConnection();
		// if (con == null) {
		// System.out.println("Non c'� connesione al db");
		// return null;
		// }
		// stmt = con.createStatement();
		// rs = stmt.executeQuery(query);
		// Author authorDB = null;

		// ArrayList<Author> authorList = new ArrayList<Author>();

		// while (rs.next()) {
		// String firstName = rs.getString("Nome");
		// String ORCID = rs.getString("ORCID");
		// authorDB = new Author(firstName, ORCID);
		// authorList.add(authorDB);
		// }

		// // for ( int i = 0; i < authorList.size(); i++) {
		// // System.out.println(authorList.get(i));
		// // }
		// return authorList;
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return null;
		// }
	}

	public ArrayList<Author> getAuthorsOf(BibliographicReference reference) {
		// TODO:

		return null;
	}

}