import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DB_table.*;

public class T_SignUp extends JFrame implements ActionListener, ItemListener{
	
	ClassroomDao cDao = new ClassroomDao();
	EmpDao eDao = new EmpDao();
	
	JTextField txtId = new JTextField();
	JTextField txtPw = new JTextField();
	JTextField txtName = new JTextField();
	JTextField txtPhone = new JTextField();
	Choice cClass = new Choice(), cEmp = new Choice();
	JButton btn = new JButton();
	
	JLabel lbId = new JLabel("아이디 :");
	JLabel lbPw = new JLabel("패스워드 :");
	JLabel lbName = new JLabel("이름 :");
	JLabel lbPhone = new JLabel("전화번호 :");
	JLabel lbClass = new JLabel("담당 반 :");
	JLabel lbEmp = new JLabel("직급 :");
	
	public T_SignUp(String title) {
		super(title);
		
		tcompose();
		
		// 창설정. 창크기 고정
		setSize(500, 600);
		setLocation(400, 250);
		setVisible(true);
		setResizable(false);
		
		//test
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	} // T_SignUp생성자
	
	/*화면구성*/
	private void tcompose() {
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		font();
		// 입력패널
		JPanel pnTxt = new JPanel();
		pnTxt.setLayout(null);
		pnTxt.setBounds(100, 100, 300, 200);
		contentPane.add(pnTxt);

		lbId.setBounds(0, 0, 70, 40);
		lbPw.setBounds(0, 50, 70, 40);
		lbName.setBounds(0, 100, 70, 40);
		lbPhone.setBounds(0, 150, 70, 40);
		
		pnTxt.add(lbId);
		pnTxt.add(lbPw);
		pnTxt.add(lbName);
		pnTxt.add(lbPhone);
		
		txtId.setBounds(90, 6, 210, 30);
		txtPw.setBounds(90, 56, 210, 30);
		txtName.setBounds(90, 106, 210, 30);
		txtPhone.setBounds(90, 156, 210, 30);
		
		pnTxt.add(txtId);
		pnTxt.add(txtPw);
		pnTxt.add(txtName);
		pnTxt.add(txtPhone);
		
		// 선택패널
		JPanel pnCho = new JPanel();
		pnCho.setLayout(null);
		pnCho.setBounds(100, 290, 300, 120);
		contentPane.add(pnCho);
		
		lbClass.setBounds(0, 10, 70, 40);
		lbEmp.setBounds(0, 60, 70, 40);
		pnCho.add(lbClass);
		pnCho.add(lbEmp);
		
		setChoice();
		
		cClass.setBounds(90, 20, 210, 40);
		cEmp.setBounds(90, 70, 210, 40);
		pnCho.add(cClass);
		pnCho.add(cEmp);
		
		cClass.addItemListener(this);
		cEmp.addItemListener(this);
		
		// 버튼
		btn = new JButton("가입");
		btn.addActionListener(this);
		btn.setBounds(180, 430, 140, 50);
		contentPane.add(btn);
	} // tcompose()

	private void setChoice() {
		cClass.add("담당 클래스를 선택하세요");
		cEmp.add("직급을 선택하세요");
		
		ArrayList<ClassroomBean> lists = cDao.setClassChoice();
		for(ClassroomBean cb : lists) {
			cClass.add(cb.getName());
		}
		
		ArrayList<EmpBean> lists2 = eDao.setClassChoice();
		for(EmpBean eb : lists2) {
			cEmp.add(eb.getName());
		}
	} // setChoice()

	public void font() {
		Font fontBold = new Font("나눔스퀘어 네오 Regular", Font.BOLD, 15);
		Font font = new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 15);
		lbId.setFont(fontBold);
		lbPw.setFont(fontBold);
		lbName.setFont(fontBold);
		lbPhone.setFont(fontBold);
		lbClass.setFont(fontBold);
		lbEmp.setFont(fontBold);
		txtId.setFont(font);
		txtPw.setFont(font);
		txtName.setFont(font);
		txtPhone.setFont(font);
		btn.setFont(fontBold);
		cClass.setFont(font);
		cEmp.setFont(font);
	}
	//test
	public static void main(String[] args) {
		new T_SignUp("회원가입");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("가입");
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		String item1 = cClass.getItem(cClass.getSelectedIndex());
		String item2 = cEmp.getItem(cEmp.getSelectedIndex());
		System.out.println(item1 + item2);
	}
	
	
}
