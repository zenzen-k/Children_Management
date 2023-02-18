package DB_table;

public class PhysicalBean {
	private int p_no;
	private String pd_age;
	private String pd_date;
	private double weight;
	private double height;
	
	public PhysicalBean() {
		super();
	}

	public PhysicalBean(int p_no, String pd_age, String pd_date, double weight, double height) {
		super();
		this.p_no = p_no;
		this.pd_age = pd_age;
		this.pd_date = pd_date;
		this.weight = weight;
		this.height = height;
	}

	public int getP_no() {
		return p_no;
	}

	public void setP_no(int p_no) {
		this.p_no = p_no;
	}

	public String getPd_age() {
		return pd_age;
	}

	public void setPd_age(String pd_age) {
		this.pd_age = pd_age;
	}

	public String getPd_date() {
		return pd_date;
	}

	public void setPd_date(String pd_date) {
		this.pd_date = pd_date;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	
}
