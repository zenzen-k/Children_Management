package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AttendManageDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String id = "jspid";
	private String pw = "jsppw";
	
	Connection conn = null;
	
	public AttendManageDao(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로드 실패");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("계정접속 실패");
			e.printStackTrace();
		}
	}//AttendManageDao생성자
	
	public void exit() {
		if(conn!=null)
		try {
			conn.close();
			System.out.println("접속 종료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // exit()
	
	public int selectAttend(String date) {
		int result = 0;
		String sql = "select * from ATTENDMANAGE where ADATE = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			rs = ps.executeQuery();
			if(rs.next()) {
				result++;
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
		return result;
	} //selectAttend
	
	public int updateAttend(ArrayList<AttendManageBean> lists) {
		PreparedStatement ps = null;
		String sql = "update ATTENDMANAGE set attend = ?, ABSENCE = ?, EARLIER = ? where p_no = ? and adate = ? ";
		int cnt = -1;
		try {
			for(int i =0 ; i<lists.size(); i++) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, lists.get(i).getAttend());
				ps.setInt(2, lists.get(i).getAbsence());
				ps.setInt(3, lists.get(i).getEarlier());
				ps.setInt(4, lists.get(i).getP_no());
				ps.setString(5, lists.get(i).getAdate());
				
				cnt = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		return cnt;
	} //updateAttend
	

	public int insertAttend(ArrayList<AttendManageBean> lists) {
		PreparedStatement ps = null;
		int cnt = -1;
		String sql = "insert into attendmanage(p_no, attend, absence, earlier, adate) "
				+ "values(?, ?, ?, ?, ?)";
		try {
			for(int i =0 ; i<lists.size(); i++) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, lists.get(i).getP_no());
				ps.setInt(2, lists.get(i).getAttend());
				ps.setInt(3, lists.get(i).getAbsence());
				ps.setInt(4, lists.get(i).getEarlier());
				ps.setString(5, lists.get(i).getAdate());
				
				cnt = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		return cnt;
	} //insertAttend
	
	// 테이블 새로고침을 위한 insert. 없는 날짜 선택 시 데이터 추가됨
	public int insertAdate(ArrayList<AttendManageBean> lists) {
		ArrayList<JoinBean> jlist = new ArrayList<JoinBean>();
		PreparedStatement ps = null;
		int cnt = -1;
		String sql = "insert into attendManage(p_no, adate) values(?, ?)";
		try {
			for(AttendManageBean x : lists) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, x.getP_no());
				ps.setString(2, x.getAdate());
				cnt = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		
		return cnt;
	}

	public int[] getAttendNum(String classroom, String adate) {
		int[] num = new int[2];
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select count(p_no) "
				+ "from (select p_no from person natural join classroom where c_name = ?) "
				+ "natural join attendmanage "
				+ "where adate = ? and attend = ?";
		try {
			for(int i=0; i<num.length; i++) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, classroom);
				ps.setString(2, adate);
				ps.setInt(3, i);
				rs = ps.executeQuery();
				if(rs.next()) {
					num[i] = rs.getInt("count(p_no)");
				}
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
		return num;
	}
} //AttendManageDao클래스
