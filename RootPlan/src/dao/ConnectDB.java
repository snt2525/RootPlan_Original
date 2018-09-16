package dao;

import java.util.*;

import dto.CustomerInfo;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
	static Connection connection;
	static Statement st;	
	static ResultSet rs;
	static PreparedStatement ps;
	public ConnectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rootplan","root", "rootplan");
			st = connection.createStatement();
			rs = st.executeQuery("SHOW TABLES;");
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
	public static void CheckID(CustomerInfo info) {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rootplan","root", "rootplan");	
			st = connection.createStatement();
			rs = st.executeQuery("SELECT * FROM customer where id='"+ info.getId()+"'");
			if(rs.next()) { //이미 있는 아이디
				System.out.println("이미 있는 아이디입니다.");
				return;
			}else {
				System.out.println("없는 아이디입니다.");
				CreateDB(info); //없는 아이디
				rs = st.executeQuery("SELECT * FROM customer");
			}
		} catch (SQLException SQLex) {
			
		}
	}
	private static void CreateDB(CustomerInfo info) {
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
	public static void callDB() {
		
	}
}
