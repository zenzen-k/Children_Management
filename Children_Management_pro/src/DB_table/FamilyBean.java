package DB_table;

public class FamilyBean {
	private int p_no;
	private String f_relations;
	private String f_name;
	private String f_birth;
	private String f_phone;
	
	public FamilyBean() {
		super();
	}

	public FamilyBean(int p_no, String f_relations, String f_name, String f_birth, String f_phon) {
		super();
		this.p_no = p_no;
		this.f_relations = f_relations;
		this.f_name = f_name;
		this.f_birth = f_birth;
		this.f_phone = f_phon;
	}

	public int getP_no() {
		return p_no;
	}

	public void setP_no(int p_no) {
		this.p_no = p_no;
	}

	public String getF_relations() {
		return f_relations;
	}

	public void setF_relations(String f_relations) {
		this.f_relations = f_relations;
	}

	public String getF_name() {
		return f_name;
	}

	public void setF_name(String f_name) {
		this.f_name = f_name;
	}

	public String getF_birth() {
		return f_birth;
	}

	public void setF_birth(String f_birth) {
		this.f_birth = f_birth;
	}

	public String getF_phone() {
		return f_phone;
	}

	public void setF_phone(String f_phon) {
		this.f_phone = f_phon;
	}
	
	
}
