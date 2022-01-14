//import java.sql.*;
//
//public class Connesione {
//
//	private final String url = "jdbc:postgresql://localhost/Esame";
//	private final String user = "postgres";
//	private final String password = "Riccardo11";
//	Statement stmt = null;
//	Connection connection = null;
//	ResultSet rs = null;
//
//	void connect(){
//		try {
//			Connection connection = DriverManager.getConnection(url, user, password);
//			if (connection != null) {
//				System.out.println("Connesione riuscita");
//			} else {
//				System.out.println("Connesione Fallita");
//			}
//		}
//	
//
//		catch (SQLException e) {
//			e.printStackTrace();
//		}
////
////	try {
////		stmt = connection.createStatement(); 
////		String sql = "SELECT * FROM ....";
////		rs = stmt.executeQuery(sql);
////		int i = 0;
////		while (rs.next())
////		{
////			System.out.println(i+ "record");
////			System.out.println(rs.getString(i));
////			System.out.println();
////
////		}
////		
////		stmt.close();
////		rs.close();
////		connection.close();
////	}
////	catch (SQLException throwables) {
////		throwables.printStackTrace();
////	}
//	
//	
//	// public static void main(String[] args) {
//	// Connesione sqlConnect = new Connesione();
//	// sqlConnect.connect();
//	// }
//
//}
//}