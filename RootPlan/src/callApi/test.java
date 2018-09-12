package callApi;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class test {
	static Connection connection;
	static Statement st;
	static ResultSet rs;
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rootplan","root", "rootplan");
			st = connection.createStatement();
			rs = st.executeQuery("show tables;");
			
			while (rs.next()) {
				String str = rs.getNString(1);
				System.out.println(str);
			}
			
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}