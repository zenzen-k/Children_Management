package DB_table;

public class TeacherBean {
	private String t_id;
	private String t_pw;
	private int c_no; 
	private int emp_no;
	private String t_name;
	private String t_phone;
	
	public TeacherBean() {
		super();
	}
	
	public TeacherBean(String t_id, String t_pw) {
		super();
		this.t_id = t_id;
		this.t_pw = t_pw;
	}

	public TeacherBean(String t_id, String t_pw, int c_no, int emp_no, String t_name, String t_phone) {
		super();
		this.t_id = t_id;
		this.t_pw = t_pw;
		this.c_no = c_no;
		this.emp_no = emp_no;
		this.t_name = t_name;
		this.t_phone = t_phone;
	}

	public String getT_id() {
		return t_id;
	}

	public void setT_id(String t_id) {
		this.t_id = t_id;
	}

	public String getT_pw() {
		return t_pw;
	}

	public void setT_pw(String t_pw) {
		this.t_pw = t_pw;
	}

	public int getC_no() {
		return c_no;
	}

	public void setC_no(int c_no) {
		this.c_no = c_no;
	}

	public int getEmp_no() {
		return emp_no;
	}

	public void setEmp_no(int emp_no) {
		this.emp_no = emp_no;
	}

	public String getT_name() {
		return t_name;
	}

	public void setT_name(String t_name) {
		this.t_name = t_name;
	}

	public String getT_phone() {
		return t_phone;
	}

	public void setT_phone(String t_phone) {
		this.t_phone = t_phone;
	}
	
}
