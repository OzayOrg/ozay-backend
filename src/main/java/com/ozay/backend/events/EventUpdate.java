import java.sql.*;

public class EventUpdate {
	static String account = "pymqxayzqcupig";
    static String password = "bbfQfAnPae6Fk4jrJDChq4qOpN";
	static String server = "ec2-54-83-10-210.compute-1.amazonaws.com:5432";
	static String database = "d7iegu76smkaqb";

	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection( "jdbc:postgresql://" + server + "/" + database + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", account, password);
			Statement stmt = con.createStatement();
			stmt.executeQuery("UPDATE events SET(name, date) = ('Fun Day', '2016-02-15') WHERE id = 9");
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
