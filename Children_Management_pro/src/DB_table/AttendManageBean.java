package DB_table;

public class AttendManageBean {
	private int p_no;
	private int attend;
	private int absence;
	private int earlier;
	private int classday;
	private String adate;
	
	public AttendManageBean() {
		
	}

	public AttendManageBean(int p_no, int attend, int absence, int earlier, int classday, String adate) {
		super();
		this.p_no = p_no;
		this.attend = attend;
		this.absence = absence;
		this.earlier = earlier;
		this.classday = classday;
		this.adate = adate;
	}

	public int getP_no() {
		return p_no;
	}

	public void setP_no(int p_no) {
		this.p_no = p_no;
	}

	public int getAttend() {
		return attend;
	}

	public void setAttend(int attend) {
		this.attend = attend;
	}

	public int getAbsence() {
		return absence;
	}

	public void setAbsence(int absence) {
		this.absence = absence;
	}

	public int getEarlier() {
		return earlier;
	}

	public void setEarlier(int earlier) {
		this.earlier = earlier;
	}

	public int getClassday() {
		return classday;
	}

	public void setClassday(int classday) {
		this.classday = classday;
	}

	public String getAdate() {
		return adate;
	}

	public void setAdate(String adate) {
		this.adate = adate;
	}
	
}
