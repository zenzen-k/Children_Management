package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeacherDao {
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String id = "jspid";
	private String pw = "jsppw";
	
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

	
	// 회원가입
	public int insertTeacher(TeacherBean tb) {
		PreparedStatement ps = null;
		int cnt = -1;
		String sql = "insert into teacher values(?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, tb.getT_id());
			ps.setString(2, tb.getT_pw());
			ps.setInt(3, tb.getC_no());
			ps.setInt(4, tb.getEmp_no());
			ps.setString(5, tb.getT_name());
			ps.setString(6, tb.getT_phone());
			cnt = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cnt;
	} // insertTeacher
	
	// 회원가입시 교사검사
	public int checkTeacher(int indexC) {
		int cnt = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select count(t_id) from teacher where c_no = ? and emp_no in(201, 202)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, indexC);
			rs = ps.executeQuery();
			if(rs.next())
				cnt = rs.getInt("count(t_id)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	public int loginId(String txtId) {
		int cnt = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select count(t_id) from teacher where t_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, txtId);
			rs = ps.executeQuery();
			if(rs.next())
				cnt = rs.getInt("count(t_id)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cnt;
	}
	
} // TeacherDao 클래스
