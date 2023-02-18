package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.proxy.annotation.Pre;

public class PhysicalDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "jspid";
	String pw = "jsppw";

	Connection conn = null;

	public PhysicalDao() {
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
	} // PhysicalDao()

	public void exit() {
		if(conn!=null)
			try {
				conn.close();
				System.out.println("접속 종료");
			} catch (SQLException e) {
				e.printStackTrace();
			}
	} // exit()

	// 전체정보조회
	public ArrayList<PhysicalBean> getAllInfo(int no) {
		ArrayList<PhysicalBean> list = new ArrayList<PhysicalBean>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select pd_date, weight, height, pd_age from physicalDev where p_no = "+no+"order by pd_age";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				PhysicalBean pb = new PhysicalBean();
				pb.setPd_date(rs.getString("pd_date"));
				pb.setWeight(rs.getDouble("weight"));
				pb.setHeight(rs.getDouble("height"));
				pb.setPd_age(rs.getString("pd_age"));
				list.add(pb);
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
		return list;
	} //getAllInfo
	
	//학기자동추가
	public void phyAgeInsert(int p_no) {
		PreparedStatement ps = null;
		for(int i=3; i<6; i++) {
			try {
				conn.setAutoCommit(false);
				String sql = "insert into physicalDev(p_no, pd_age, pd_date) values( ?, '만 "+i+"세 "+1+"학기', ' ')";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, p_no);
				ps.executeUpdate();
				conn.commit();

				sql = "insert into physicalDev(p_no, pd_age, pd_date) values( ?, '만 "+i+"세 "+2+"학기', ' ')";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, p_no);
				ps.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("실패");
			} 
		}// for
		try {
			if(ps != null)
				ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//phyAgeInsert
	
	//정보추가
	public int physicalUpdate(ArrayList<PhysicalBean> lists) {
		System.out.println(lists.get(1).getPd_date());
		PreparedStatement ps = null;
		int cnt = -1;
		for(int i=0; i<lists.size(); i++) {
			String sql = "update physicalDev set pd_date = ?, weight = ?, height = ? where p_no = ? and pd_age = ?";
			try {
				ps = conn.prepareStatement(sql);
				String n = lists.get(i).getPd_date();
				ps.setString(1, lists.get(i).getPd_date());
				ps.setDouble(2, lists.get(i).getWeight());
				ps.setDouble(3, lists.get(i).getHeight());
				ps.setInt(4, lists.get(i).getP_no());
				ps.setString(5, lists.get(i).getPd_age());
				
				ps.executeUpdate();
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
		} //for
		return cnt;
	} //physicalUpdate


}
