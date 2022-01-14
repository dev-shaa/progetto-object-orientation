package DAO;

import Entities.User;
import java.sql.*; 
import DAO.DatabaseController;

public class UserDAO {	
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public void SaveUser ( User user) {
	
		try {
			String st = "INSERT INTO public.\"UtenteApp\" (\"Nome\", \"Password\") VALUES('"+user.getName()+"','sdfgh');";

			con = DatabaseController.getConnection();
			stmt = con.createStatement();
			stmt.executeUpdate(st);
//			rs = stmt.executeQuery(st);
			
//			stmt.setString(1, user.getName());
//			stmt.executeUpdate();
			System.out.println("Utente aggiunto con successo");
			}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (con != null) 
					con.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	
//	public void UpdateUser(User user) {
//
//		try {
//			String st = "UPDATE user SET Name=? WHERE Id=?";  implementare id utente
//			connection = getConnection();
//			stmt = connection.prepareStatement(st);
//			stmt.setString(1, user.getName());
//			stmt.executeUpdate();
//			System.out.println("Utente aggiornato con successo");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (stmt != null)
//					stmt.close();
//				if (conn != null)
//					conn.close();
//			}
//
//			catch (SQLException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//
//			}
//		}
//	}
//		
//	
//	public void DeleteUser(int id) {     //implementare id utente
//
//		try {
//			String st = "DELETE FROM User WHERE id";
//			con = DatabaseController.getConnection();
//			stmt = con.prepareStatement(st);
//			rs = stmt.executeQuery(st);
//			
//			stmt.setString(1, user.getName());
//			stmt.executeUpdate();
//			System.out.println("Utente eliminato con successo");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (stmt != null)
//					stmt.close();
//				if (con != null)
//					con.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	
		
		
		
	}
	
	
	

