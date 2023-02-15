package DB_table;

public class PersonBean {
	private int p_no;
	private String p_name ;
	private String p_birth ; 
	private String gender ;
	private String p_entran ; // 입학일
	private String addr ;
	private int img_id ;
	private int c_no ;
	private String note ;
	
	
	public PersonBean() {
		super();
	}

	public PersonBean(int p_no, String p_name, String p_birth, String gender, String p_entran, String addr, int img_id,
			int c_no, String note) {
		super();
		this.p_no = p_no;
		this.p_name = p_name;
		this.p_birth = p_birth;
		this.gender = gender;
		this.p_entran = p_entran;
		this.addr = addr;
		this.img_id = img_id;
		this.c_no = c_no;
		this.note = note;
	}

	public int getP_no() {
		return p_no;
	}

	public void setP_no(int p_no) {
		this.p_no = p_no;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public String getP_birth() {
		return p_birth;
	}

	public void setP_birth(String p_birth) {
		this.p_birth = p_birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getP_entran() {
		return p_entran;
	}

	public void setP_entran(String p_entran) {
		this.p_entran = p_entran;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getImg_id() {
		return img_id;
	}

	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}

	public int getC_no() {
		return c_no;
	}

	public void setC_no(int c_no) {
		this.c_no = c_no;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
