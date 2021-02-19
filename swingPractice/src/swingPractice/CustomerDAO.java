package swingPractice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CustomerDAO {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	private static CustomerDAO cd = new CustomerDAO();
	
	private CustomerDAO() {
		
	}
	public static CustomerDAO getInstance() {
		return cd;
	}
	
	public void connectDb() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/java6";
		String user = "root";
		String password = "1234";
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,password);
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로드실패");
		} catch (SQLException e) {
			System.out.println("DB 연결 실패");
		}
		
		
		
	}
	public void close() {
		if(rs!=null) try{rs.close();} catch(Exception e) {}
		if(pstmt!=null) try{pstmt.close();} catch(Exception e) {}
		if(con!=null) try{con.close();} catch(Exception e) {}
	}
	
	public Vector<CustomerDTO> select() {
		connectDb();
		Vector<CustomerDTO> customers = new Vector<CustomerDTO>();
		try {
			String sql = "SELECT * FROM customer";
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int idx = rs.getInt(1);
				String name = rs.getString(2);
				int age = rs.getInt(3);
				String email = rs.getString(4);
				String jumin = rs.getString(5);
				customers.add(new CustomerDTO(idx, name, age, email, jumin));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		
		return customers;
	}
	public boolean insert(CustomerDTO customer) {
		connectDb();
		boolean isIneserted = false;
		try {
			
			String sql = "INSERT INTO customer(name,age,email,jumin) VALUES(?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,customer.getName());
			pstmt.setInt(2,customer.getAge());
			pstmt.setString(3,customer.getEmail());
			pstmt.setString(4,customer.getJumin());
			if(pstmt.executeUpdate()==1) {
				isIneserted = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		
		return isIneserted;
	}
	public boolean delete(int idx) {
		
		connectDb();
		boolean isDeleted = false;
		
		try {
			String sql = "DELETE FROM customer WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			if(pstmt.executeUpdate()==1) {
				isDeleted = true;
			};
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		
		
		return isDeleted;
	}
}
