package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseController {
	private static String connectionURL = "jdbc:postgresql://localhost:5432/Esame";
	private static String connectionUser = "postgres";
	private static String connectionPassword = "Riccardo11";

//    private static DatabaseController Instance = null;
//
//    public static DatabaseController getInstance() throws Exception {
//        if (Instance == null)
//            Instance = new DatabaseController();
//
//        return Instance;
//    }
//
//    public DatabaseController() throws Exception {
//        Class.forName("org.postgresql.Driver");
//    }
//
//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
//    }
//
//}

	private static Connection con;
	private static String urlstring;

	public static Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			try {
				con = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
			} catch (SQLException ex) {
				
				System.out.println("Errore nella connesione al database");
			}
		} catch (ClassNotFoundException ex) {
			
			System.out.println("Driver non trovato.");
		}
		return con;
	}
}