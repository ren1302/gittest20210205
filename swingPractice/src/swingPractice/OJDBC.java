package swingPractice;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class OJDBC {

	public static void main(String[] args) {
		Connection con = null;
		String url ="jdbc:oracle:thin:@192.168.219.199:1521:orcl";
		String user = "hr";
		String password = "hr";
		ResultSet rs = null;
		PreparedStatement psmt = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("성공");
			con = DriverManager.getConnection(url, user, password);
			System.out.println("연결성공");
			
			String sql = "SELECT * FROM employees";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			
			while(rs.next()) {
				
				for(int i=1; i<=rsmd.getColumnCount(); i++) {
					if(rsmd.getColumnTypeName(i).equals("INT")) {
							System.out.println(rs.getInt(i));
					} else if(rsmd.getColumnTypeName(i).equals("VARCHAR")) {
						System.out.println(rs.getString(i));
					}
				}
			}
			
			
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("실패");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
