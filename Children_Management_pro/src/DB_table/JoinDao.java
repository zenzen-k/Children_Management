package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JoinDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "jspid";
	String pw = "jsppw";
	
	Connection conn = null;
	
	public JoinDao() {
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
	} // JoinDao()
	
	public void exit() {
		if(conn!=null)
		try {
			conn.close();
			System.out.println("접속 종료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // exit()
	
	//테이블리스트 가져오기
	public ArrayList<JoinBean> getJoinTable() {
		ArrayList<JoinBean> lists = new ArrayList<JoinBean>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select p_no, p_name, p_birth, p_entran, c_name, c_age, t_name "
				+ "from (select * from person natural join classroom order by p_name asc, p_birth asc) "
				+ "natural join teacher order by p_name asc, p_birth asc";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				JoinBean jb = new JoinBean();
				jb.setP_no(rs.getInt("p_no"));
				jb.setP_name(rs.getString("p_name"));
				jb.setP_birth(String.valueOf(rs.getDate("p_birth")));
				jb.setP_entran(String.valueOf(rs.getDate("p_entran")));
				jb.setC_name(rs.getString("c_name"));
				jb.setC_age(rs.getInt("c_age"));
				jb.setT_name(rs.getString("t_name"));
				lists.add(jb);
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
	} // getAllJoin
	
	// 교사정보
	public String[] getLbTeacher(String loginInfo) {
		String[] lists = new String[3];
		String sql = "select t_name, c_name, e_name from (select t_name, c_name, emp_no  "
				+ "from teacher, classroom where teacher.c_no = classroom.c_no and t_id = ?) natural join emp";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, loginInfo);
			rs = ps.executeQuery();
			if(rs.next()) {
				JoinBean jb = new JoinBean();
				int i = 0;
				lists[i++] = rs.getString("t_name");
				lists[i++] = rs.getString("c_name");
				lists[i++] = rs.getString("e_name");
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
	}//getTeacher

}