package DB_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String id = "jspid";
	private String pw = "jsppw";

	Connection conn = null;

	public PersonDao() {
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
	} // PersonDao 생성자
	
	public void exit() {
		if(conn!=null)
		try {
			conn.close();
			System.out.println("접속 종료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // exit()

	// 한 학생의 모든 정보를 리턴함
	public ArrayList<JoinBean> getAllInfo(int no) {
		ArrayList<JoinBean> lists = new ArrayList<JoinBean>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select p_no, p_name, gender, cnotoname(p_no), to_char(p_birth,'yy-mm-dd') bdate, "
				+ "to_char(p_entran,'yy-mm-dd') edate, tnotoname(c_no), addr, note "
				+ "from person where p_no = " + no;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				JoinBean jb = new JoinBean();
				jb.setP_no(rs.getInt("p_no"));
				jb.setP_name(rs.getString("p_name"));
				jb.setGender(rs.getString("gender"));
				jb.setC_name(rs.getString("cnotoname(p_no)"));
				jb.setP_birth(rs.getString("bdate"));
				jb.setP_entran(rs.getString("edate"));
				jb.setT_name(rs.getString("tnotoname(c_no)"));
				jb.setAddr(rs.getString("addr"));
				jb.setP_note(rs.getString("note"));

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
	} // getAllInfo
	
	
	// 정보수정
	public int InfoUpdate(PersonBean pb) {
		int cnt = -1;
		PreparedStatement ps = null;
		String sql = "update person set addr = ? , note = ? where p_no = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, pb.getAddr());
			ps.setString(2, pb.getNote());
			ps.setInt(3, pb.getP_no());
			cnt = ps.executeUpdate();
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
	} // InfoUpdate
	
	// 학생추가
	public int infoInsert(PersonBean pb) {
		int cnt = -1;
		PreparedStatement ps = null;
		String sql = "insert into person values(?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pb.getP_no());
			ps.setString(2, pb.getP_name());
			ps.setString(3, pb.getP_birth());
			ps.setString(4, pb.getGender());
			ps.setString(5, pb.getP_entran());
			ps.setString(6, pb.getAddr());
			ps.setInt(7, pb.getImg_id());
			ps.setInt(8, pb.getC_no());
			ps.setString(9, pb.getNote());
			
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
	} // infoInsert
	
	// 학생번호생성
	public int personNum(String p_entran) {
		int cnt = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select stunum(?, p_seq.nextval) as num from person";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, p_entran);
			rs = ps.executeQuery();
			if(rs.next()) {
				cnt = rs.getInt("num");
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
		return cnt;
	} // personNum
	
	// 학생삭제
	public int personDelete(int p_no) {
		int cnt = -1;
		PreparedStatement ps = null;
		String sql = "delete person where p_no = " + p_no;
		try {
			ps = conn.prepareStatement(sql);
			cnt = ps.executeUpdate();
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
	} // personDelete
	
	//인원수와 학생벊
	public ArrayList<PersonBean> getPersonNum(String c_name) {
		ArrayList<PersonBean> lists = new ArrayList<PersonBean>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select p_no from person natural join classroom where c_name = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, c_name);
			rs = ps.executeQuery();
			while(rs.next()) {
				PersonBean pb = new PersonBean();
				pb.setP_no(rs.getInt("p_no"));
				lists.add(pb);
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
		return lists;
	}
	
}
