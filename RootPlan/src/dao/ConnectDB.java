package dao;

import java.util.*;

import dto.Address;
import dto.CustomerInfo;
import dto.DBRouteData;

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
	public void CheckID(CustomerInfo info) {
		try {
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
	private void CreateDB(CustomerInfo info) {
		System.out.println("DB를 생성합니다");
		int result = 0;
		try {
			int Query = st.executeUpdate("INSERT INTO customer " +
					"VALUES('"+info.getId()+"','"+info.getEmail()
					+"',"+info.getGender()+","+info.getAge()+");");			
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
	}
	
	private String makeRID(LinkedList<Address> ad) {
		String result = "";
		int size = ad.size();
		for(int i = 0;i<size;i++) {
			result+= Double.toString(ad.get(i).getLat()) + Double.toString(ad.get(i).getLng());
		}
		System.out.println("rID:" + result);
		return result;
	}
	
	public String CheakSameData(LinkedList<Address> ad,String cID) { //저장 -> 중복되는 애가 있는지 검사
		//rID를 만든다
		String rID =  makeRID(ad);
		try {
			rs = st.executeQuery("SELECT * FROM route where rid='"+rID+"' AND cid='"+cID+"'");
			if(rs.next()) { //이미 있는 아이디
				System.out.println("이미 있는 경로");
				return "0";
			}else {
				System.out.println("없는 저장 정보 입니다."); 
				DBRouteData data = DataIntoDBRouteData(ad, rID ,cID); //리스트에 있는 데이터를 dto에 넣어준다
				SaveData(data); //중복되는 데이터가 없으면 저장 한다.
				rs = st.executeQuery("SELECT * FROM Route where cid='"+cID+"'"); //회원의 전체 리스트를 봐본다
			}
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
		}
		return "1";
	}
	
	//리트스에 있는 데이터를 DTO에 넣어준다. CheakSameData - >DataIntoDBRouteData -> SaveData 순서로
	private DBRouteData DataIntoDBRouteData(LinkedList<Address> ad,String rID,String cID) {
		int size = ad.size();
		DBRouteData tmpData = new DBRouteData(rID, cID);
		tmpData.setDataSize(size);
		for(int i =0;i<size;i++) {
			tmpData.setAddress(i,ad.get(i).getAddress());
			tmpData.setLat(i, ad.get(i).getLat());
			tmpData.setLng(i, ad.get(i).getLng());
		}		
		return tmpData;
	}
	
	public void SaveData(DBRouteData data) { //데이터 저장		
		System.out.println("Route DB에 데이터를 삽입합니다.");
		int result = 0;
		try {
			int Query = st.executeUpdate("INSERT INTO route VALUES('"
					        +data.getRid()+"',"+data.getDatasize()+",'"+data.getCid()+"','"
					        +data.getAddress(0)+"',"+ data.getLat(0) +","+data.getLng(0)+",'"
					        +data.getAddress(1)+"',"+ data.getLat(1) +","+data.getLng(1)+",'"
					        +data.getAddress(2)+"',"+ data.getLat(2) +","+data.getLng(2)+",'"
					        +data.getAddress(3)+"',"+ data.getLat(3) +","+data.getLng(3)+",'"
					        +data.getAddress(4)+"',"+ data.getLat(4) +","+data.getLng(4)+",'"
					        +data.getAddress(5)+"',"+ data.getLat(5) +","+data.getLng(5)+",'"
					        +data.getAddress(6)+"',"+ data.getLat(6) +","+data.getLng(6)+")");
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}	
	}
	
	public String GetAllData(String cID) { //모든 데이터 넘겨주기
		String result = "<SaveData>";
		try {				
			rs = st.executeQuery("SELECT * FROM route WHERE cid='"+cID+"'");
			
			while(rs.next()) {
				result += "<Data>";
				result += "<rID>" + rs.getString(1) + "</rID>";
				int size = rs.getInt(2); //사이즈
				result += "<size>" + Integer.toString(size) + "</size>";
				int addressCnt = 4;
				int latCnt = 5, lngCnt = 6;
				for(int i = 0;i<size;i++) {
					result += "<address"+Integer.toString(i)+">"+ rs.getString(addressCnt) +"</address"+Integer.toString(i)+">";
					result +=  "<lat"+Integer.toString(i)+">"+ rs.getString(latCnt) +"</lat"+Integer.toString(i)+">";
					result +=  "<lng"+Integer.toString(i)+">"+ rs.getString(lngCnt) +"</lng"+Integer.toString(i)+">";
					addressCnt += 3;
					latCnt += 3;
					lngCnt += 3;
				}
				result += "</Data>";
			}			
			result = "</SaveData>";
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
		return result;
	}
	
	public void DeleteData(String rID,String cID) { //삭제
		System.out.println("DB삭제");
		int result = 0;
		try {				
			int Query = st.executeUpdate("DELETE FROM route WHERE rid='"+rID+"' AND cid='"+cID+"'");	
			rs = st.executeQuery("SELECT * FROM Route where cid='"+cID+"'"); //회원의 전체 리스트를 봐본다 삭제 됬는지 검사
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
	}
	 //rid로 찾아서 넘길때 - > AddressDataManager에 list에 데이터를 삽입해주기 위해 DBRouteData에 데이터를 넣어 return;
	public DBRouteData CallDBData_INDEX(String rID,String cID) {		
		DBRouteData tmpIndex = new DBRouteData(rID, cID);
		try {			
			rs = st.executeQuery("SELECT * FROM route WHERE cid='"+cID+"' AND rid='"+rID+"'");		
			int size = rs.getInt(2);
			tmpIndex.setDataSize(size);
			int addressCnt = 4;
			int latCnt = 5, lngCnt = 6;
			for(int i =0;i<size;i++) {
				tmpIndex.setAddress(i, rs.getString(addressCnt));
				tmpIndex.setLat(i, Double.parseDouble(rs.getString(latCnt)));
				tmpIndex.setLng(i, Double.parseDouble(rs.getString(lngCnt)));
				addressCnt += 3;
				latCnt += 3;
				lngCnt += 3;			
			}			
		}catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
		}					
		return tmpIndex;
	}
}
