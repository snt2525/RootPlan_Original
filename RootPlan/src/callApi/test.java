package callApi;
import java.util.*;

import dto.CustomerInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class test {
	static CustomerInfo info = new CustomerInfo("1231","test@mail.com","M","20-29");
	static PreparedStatement ps;
	static Connection connection;
	static Statement st;
	static ResultSet rs;
	public void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rootplan","root", "rootplan");
			st = connection.createStatement();
			rs = st.executeQuery("SHOW TABLES;");
			CheckID();
			while (rs.next()) {
				String str = rs.getNString(1);
				System.out.println(str);
			}
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void CheckID() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rootplan","root", "rootplan");	
			st = connection.createStatement();
			rs = st.executeQuery("SELECT * FROM customer where id='"+ info.getId()+"'");
			if(rs.next()) { //이미 있는 아이디
				System.out.println("이미 있는 아이디입니다.");
				return;
			}else {
				System.out.println("없는 아이디입니다.");
				CreateDB(); //없는 아이디
				rs = st.executeQuery("SELECT * FROM customer");
			}
		} catch (SQLException SQLex) {
			
		}
	}
	public void CreateDB() {
		System.out.println("DB를 생성합니다");
		int result = 0;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rootplan","root", "rootplan");				
			st = connection.createStatement();
			int r = st.executeUpdate("INSERT INTO customer " +
					"VALUES('"+info.getId()+"','"+info.getEmail()
					+"',"+info.getGender()+","+info.getAge()+");");
			
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
	}
	public void SaveData() { //데이터 저장
		
	}
	public void GetAllData() { //모든 데이터 넘겨주기
		
	}
	
	
}