import java.sql.*;

public class Connesione {

	private final String url = "jdbc:postgresql://localhost/Esame";
	private final String user = "postgres";
	private final String password = "Riccardo11";

	private void connect() {
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			if (connection != null) {
				System.out.println("Connesione riuscita");
			} else {
				System.out.println("Connesione Fallita");
			}
		}

		catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Connesione sqlConnect = new Connesione();
		sqlConnect.connect();
	}

}
