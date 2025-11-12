package com.dongyang.example1;

import java.util.ArrayList;
import java.sql.*;

public class MemberDAO {
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	final String SQL_ALL="select * from membertbl;";
	final String SQL_LOGIN="select * from membertbl where memberid=? and password=?";
	
	public ArrayList<MemberDTO> selectmemberList(){
		con=JdbcConnectUtil.getConnetion();
		ArrayList<MemberDTO> aList=new ArrayList<MemberDTO>();
		try {
			pstmt = con.prepareStatement(SQL_ALL);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				 MemberDTO dto=new MemberDTO();
				 dto.setMemberid(rs.getString("memberid"));
				 dto.setPassword(rs.getString("password"));
				 dto.setName(rs.getString("name"));
				 dto.setEmail(rs.getString("email"));
				 aList.add(dto);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcConnectUtil.close(con, pstmt, rs);
		}
		return aList;
	}
	
	public boolean loginCheck(MemberDTO mdto) {

		boolean loginCheck=false;
		try {

			con=JdbcConnectUtil.getConnetion();
			
//			3단계 : SQL로 데이터 조작
			pstmt=con.prepareStatement(SQL_LOGIN);
			pstmt.setString(1, mdto.getMemberid());
			pstmt.setString(2, mdto.getPassword());
			rs=pstmt.executeQuery();
			//pstmt.executeUpdate();
			loginCheck=rs.next();		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcConnectUtil.close(con, pstmt, rs );
		}
		
		return loginCheck;

	}
}
