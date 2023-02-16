package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FamilyDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "jspid";
	String pw = "jsppw";

	Connection conn = null;

	public FamilyDao() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("드라이버 로드 실패");
		} catch (SQLException e) {
			System.out.println("계정 접속 실패");
			e.printStackTrace();
		}
	} // FamilyDao 생성자
	
	public void exit() {
		if(conn!=null)
		try {
			conn.close();
			System.out.println("접속 종료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // exit()

	
	// 조건 - 가족조회
	public ArrayList<FamilyBean> getAllFam(int p_no) {
		ArrayList<FamilyBean> lists = new ArrayList<FamilyBean>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from family where p_no = " + p_no;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				FamilyBean fb = new FamilyBean();
				fb.setF_relations(rs.getString("f_relations"));
				fb.setF_name(rs.getString("f_name"));
				fb.setF_birth(String.valueOf(rs.getDate("f_birth")));
				fb.setF_phone(rs.getString("f_phone"));
				lists.add(fb);
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
	}
	
	// 정보수정
	public int InfoUpdate(FamilyBean[] list) {
		int cnt = -1;
		PreparedStatement ps = null;
		String sql = "update family set f_name = ? , f_birth = ?, f_phone = ? "
				+ "where p_no = ? and f_relations = ?";
		for(int i = 0 ; i<list.length; i++) {
			try {
				cnt = -1;
				ps = conn.prepareStatement(sql);
				ps.setString(1, list[i].getF_name());
				ps.setString(2, list[i].getF_birth());
				ps.setString(3, list[i].getF_phone());
				ps.setInt(4, list[i].getP_no());
				ps.setString(5, list[i].getF_relations());
				cnt = ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			if(ps != null)
				ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	} // InfoUpdate

	public int finfoInsert(FamilyBean fb) {
		int cnt = -1;
		PreparedStatement ps = null;
		String sql = "insert into family values(?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, fb.getP_no());
			ps.setString(2, fb.getF_relations());
			ps.setString(3, fb.getF_name());
			ps.setString(4, fb.getF_birth());
			ps.setString(5, fb.getF_phone());
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
	}
	
	
}
