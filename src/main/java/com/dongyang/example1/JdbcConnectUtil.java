package com.dongyang.example1;

import java.sql.*;

public class JdbcConnectUtil {
	public static Connection getConnetion() {
		Connection con=null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			con= DriverManager.getConnection("jdbc:mysql://localhost:3306/servletdb", "root", "dongyang");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return con;
	}
	
	public static void close(Connection con, PreparedStatement pstmt) {
		try {
			con.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
		try {
			con.close();
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}	

}





