package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherDao {
	
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "jspid";
	String pw = "jsppw";
	
	Connection conn = null;
	
	public TeacherDao() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로드 실패");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("계정 접속 실패");
			e.printStackTrace();
		}
	} // TeacherDao()

	public int getLogin(String logid, String logpw) {
		String sql = "select login('" + logid + "','" + logpw + "') as login from dual";
		ResultSet rs = null;
		PreparedStatement ps = null;
		int cnt = 0;
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			rs = ps.executeQuery();
			
			if(rs.next())
				System.out.println(rs.getInt("login"));
				cnt = rs.getInt("login");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null)
					ps.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cnt;
	} // getLogin
	
	public void exit() {
		if(conn!=null)
		try {
			conn.close();
			System.out.println("접속 종료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // exit()
	
} // TeacherDao 클래스
