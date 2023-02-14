package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmpDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "jspid";
	String pw = "jsppw";
	
	Connection conn = null;
	
	public EmpDao() {
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
	} // EmpDao()

	public ArrayList<EmpBean> setClassChoice() {
		ArrayList<EmpBean> lists = new ArrayList<EmpBean>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select e_name from emp";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				EmpBean eb = new EmpBean();
				eb.setName(rs.getString("e_name"));
				lists.add(eb);
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
	} //setClassChoice()
	
	public void exit() {
		if(conn!=null)
		try {
			conn.close();
			System.out.println("접속 종료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // exit()
}
