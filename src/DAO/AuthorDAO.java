package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Entities.Author;

public class AuthorDAO {

		private Connection con = null;
		private Statement stmt = null;
		private ResultSet rs = null;

		public void SaveAuthor(Author author) {

			try {
				System.out.println("Sto provando a salvare l'utente " + author.getFirstName() + " " + author.getLastName() + " " + author.getORCID());				
				String query = "insert into \"AutoriApp\" (\"Nome\", \"Cognome\",\"ORCID\") values ('"+author.getFirstName()+"','"
						+ ""+author.getLastName()+"','"+ author.getORCID()+"')";
				
				con = DatabaseController.getConnection();
				if (con == null) {
					System.out.println("Non c'è connesione al db");
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
		
		
		public void FindAllAuthor (Author author) {
			
			try {
				
				String query = "select * from \"AutoriApp\"";
				con = DatabaseController.getConnection();
				if (con == null) {
					System.out.println("Non c'è connesione al db");
					return;
				}
				stmt = con.createStatement(); 
				stmt.executeUpdate(query);
				while (rs.next()) {
					System.out.println("Nome: "+author.getFirstName() + "\nCognome: "+author.getLastName() + "\nORCID: "+author.getORCID());
				}
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
			
			
			
		}
		
	
	
	

