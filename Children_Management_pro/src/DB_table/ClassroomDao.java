package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassroomDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String id = "jspid";
	private String pw = "jsppw";
	
	Connection conn = null;
	
	public ClassroomDao() {
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
	} // ClassroomDao()
	
	// 관리자를 제외한 모든 반 조회
	public ArrayList<ClassroomBean> setClassChoice() {
		ArrayList<ClassroomBean> lists = new ArrayList<ClassroomBean>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from classroom where c_no > 0";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ClassroomBean cb = new ClassroomBean();
				cb.setName(rs.getString("c_name"));
				lists.add(cb);
			}
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
		return lists;
	} // setClassChoice()
	
	public void exit() {
		if(conn!=null)
		try {
			conn.close();
			System.out.println("접속 종료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // exit()
	
	
	// c_name의 c_no조회
	public int classNo(String classCh) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "select c_no from classroom where c_name = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, classCh);
			rs = ps.executeQuery();
			if(rs.next())
				result = rs.getInt("c_no");
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
		return result;
	} //classNo()
	
	
}
