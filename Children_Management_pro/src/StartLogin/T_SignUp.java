package StartLogin;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DB_table.*;

public class T_SignUp extends JFrame implements ActionListener, ItemListener{

	private ClassroomDao cDao = new ClassroomDao();
	private EmpDao eDao = new EmpDao();
	private TeacherDao tdao = new TeacherDao();
	private int classCh2 = 0;
	private int empCh2 = 0;
	private int indexC, indexE;

	private JTextField txtId = new JTextField();
	private JPasswordField txtPw = new JPasswordField();
	private JTextField txtName = new JTextField();
	private JTextField txtPhone = new JTextField();
	private Choice cClass = new Choice();
	private Choice cEmp = new Choice();
	private JButton btn = new JButton();

	private JLabel lbId = new JLabel("아이디 :");
	private JLabel lbPw = new JLabel("패스워드 :");
	private JLabel lbName = new JLabel("이름 :");
	private JLabel lbPhone = new JLabel("전화번호 :");
	private JLabel lbClass = new JLabel("담당 반 :");
	private JLabel lbEmp = new JLabel("직급 :");
	
	public T_SignUp(String title) {
		super(title);
		
		// 화면구성하는 메서드 호출함
		tcompose();
		
		// 이벤트처리
		setevent();
		
		// 창설정. 창크기 고정
		setSize(500, 600);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - this.getSize().width)/2, (screenSize.height - this.getSize().height)/2);
		setVisible(true);
		setResizable(false);

	} // T_SignUp생성자

	private void setevent() {
		txtId.addKeyListener(new KeyHandler());
		txtPw.addKeyListener(new KeyHandler());
		txtName.addKeyListener(new KeyHandler());
		txtPhone.addKeyListener(new KeyHandler());
	}

	/*화면구성*/
	private void tcompose() {
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		// 폰트 메서드 호출
		font();
		
		// 텍스트 입력패널 (이름, 비밀번호, 아이디, 핸드폰번호)
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

		// 선택패널 (반, 직급)
		JPanel pnCho = new JPanel();
		pnCho.setLayout(null);
		pnCho.setBounds(100, 290, 300, 120);
		contentPane.add(pnCho);

		lbClass.setBounds(0, 10, 70, 40);
		lbEmp.setBounds(0, 60, 70, 40);
		pnCho.add(lbClass);
		pnCho.add(lbEmp);
		
		// choice에 데이터 추가
		setChoice();

		cClass.setBounds(90, 20, 210, 40);
		cEmp.setBounds(90, 70, 210, 40);
		pnCho.add(cClass);
		pnCho.add(cEmp);
		
		// 버튼
		btn = new JButton("가입");
		btn.addActionListener(this);
		btn.setBounds(180, 430, 140, 50);
		contentPane.add(btn);
	} // tcompose()

	private void setChoice() {
		cClass.add("담당 클래스를 선택하세요");
		cEmp.add("직급을 선택하세요");
		
		// 담당 클래스 데이터 추가
		ArrayList<ClassroomBean> lists = cDao.setClassChoice();
		for(ClassroomBean cb : lists) {
			cClass.add(cb.getName());
		}
		// 직급 데이터 추가 
		ArrayList<EmpBean> lists2 = eDao.setClassChoice();
		for(EmpBean eb : lists2) {
			cEmp.add(eb.getName());
		}
		// 이벤트 리스너 부착
		cClass.addItemListener(this);
		cEmp.addItemListener(this);
	} // setChoice()
	
	// 텍스트필드 초기화
	private void clearTextField() {
		txtId.setText("");
		txtPw.setText("");
		txtName.setText("");
		txtPhone.setText("");
	}//clearTextField
	
	
	// 입력데이터 유효성검사. 글자누락, 최소최대글자, 미선택 
	private boolean checkData() {
		// 아이디
		if(txtId.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "ID를 입력하세요", "입력오류", JOptionPane.INFORMATION_MESSAGE);
			txtId.requestFocus();
			return false;
		} else if(txtId.getText().length() > 20 || txtId.getText().length() < 6) {
			JOptionPane.showMessageDialog(this, "ID는 6~20자 가능합니다", "입력오류", JOptionPane.PLAIN_MESSAGE);
			txtId.requestFocus();
			return false;
		}
		
		// 패스워드
		if(txtPw.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "PW를 입력하세요", "입력오류", JOptionPane.INFORMATION_MESSAGE);
			txtPw.requestFocus();
			return false;
		} else if(txtPw.getText().length() > 16 || txtPw.getText().length() < 8) {
			JOptionPane.showMessageDialog(this, "PW는 8~16자 가능합니다", "입력오류", JOptionPane.PLAIN_MESSAGE);
			txtPw.requestFocus();
			return false;
		}
		
		// 이름
		if(txtName.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "이름을 입력하세요", "입력오류", JOptionPane.INFORMATION_MESSAGE);
			txtName.requestFocus();
			return false;
		} else if(txtName.getText().length() > 10) {
			JOptionPane.showMessageDialog(this, "이름은 10글자까지 가능합니다", "입력오류", JOptionPane.PLAIN_MESSAGE);
			txtName.requestFocus();
			return false;
		}
		
		// 전화번호
		if(txtPhone.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "번호를 입력하세요", "입력오류", JOptionPane.INFORMATION_MESSAGE);
			txtPhone.requestFocus();
			return false;
		} 
		
		// choice 미선택 검사
		if(indexC == 0) {
			JOptionPane.showMessageDialog(this, "클래스를 선택하세요", "선택오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if(indexE == 0) {
			JOptionPane.showMessageDialog(this, "직급을 선택하세요", "선택오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		String empCh = cEmp.getItem(indexE);
		empCh2 = eDao.empNo(empCh);
		// 이미 교사가 존재하는 반 클릭시 -> 담임/주임은 교실 당 딱 한명만 가능, 부담임은 추가 가능
		if(tdao.checkTeacher(indexC) > 0 && empCh2 != 203) {
			JOptionPane.showMessageDialog(this, "이미 해당 클래스에 담당교사가 존재합니다. \n부담임을 선택하거나 다른 교실을 선택하십시오.", "선택오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		return true;
	} // 
	
	// 폰트설정
	private void font() {
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

	@Override
	// 액션이벤트 처리 - 회원가입 등록 버튼
	// 회원가입 정보는 교사테이블에 insert한다.
	public void actionPerformed(ActionEvent e) {
		// 입력값 검사 통과 대상 데이터만 회원가입 등록가능
		boolean result = checkData();
		if(result == true) {
			// 회원을 교사 테이블로 저장해야 하므로 교사테이블 객체 생성함.
			TeacherBean tb = new TeacherBean();
			tb.setT_id(txtId.getText());
			tb.setT_pw(txtPw.getText());
			tb.setC_no(classCh2);
			tb.setEmp_no(empCh2);
			tb.setT_name(txtName.getText());
			tb.setT_phone(txtPhone.getText());
			
			// 교사Dao클래스에서 insert를 위한 메서드 호출.
			int cnt = new TeacherDao().insertTeacher(tb);
	
			if(cnt > 0) {
				JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.", "Login", JOptionPane.INFORMATION_MESSAGE);
				clearTextField();
				close();
				dispose(); // 가입완료시 창을 닫음
			} else {
				JOptionPane.showMessageDialog(this, "ERROR. 다시 가입해주세요.", "ERROR", JOptionPane.ERROR_MESSAGE);
				clearTextField();
			}
		} // if
	
	} // actionPerformed

	@Override
	// 아이템 이벤트 처리 - 초이스 클릭
	public void itemStateChanged(ItemEvent e) {
		indexC = cClass.getSelectedIndex();
		indexE = cEmp.getSelectedIndex();
		
		// 각 선택한 항목에 대한 class, emp 테이블로부터 no을 받아와야 한다. 
		String classCh = cClass.getItem(indexC);
		String empCh = cEmp.getItem(indexE);
		// no으로 받아옴.
		// ex) 새싹반 -> 1 , 원장 -> 501
		classCh2 = cDao.classNo(classCh);
		empCh2 = eDao.empNo(empCh);
	}//itemStateChanged
	
	
	// 텍스트 필드에 입력되는 데이터를 실시간으로 확인하기 한 이벤트 처리하는 내부클래스 작성함.
	class KeyHandler extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			Object obj = e.getSource();
			if(obj == txtId) {
				// Pattern.matches 는 특정 규칙을 가진 문자열 집합을 위해 사용되는 형식언어로 boolean을 리턴함.
				// 소문자 : a-z / 대문자 : A-Z / 숫자 : 0-9 / 한글 : ㄱ-ㅎㅏ-ㅣ가-힣
				boolean eng = Pattern.matches("^[a-zA-Z0-9]*$", txtId.getText());
				if(!eng) {
					JOptionPane.showMessageDialog(txtId, "영문자/숫자만 입력하세요", "입력오류", JOptionPane.PLAIN_MESSAGE);
					txtId.setText("");
				}
			}
			if(obj == txtPw) {
				boolean eng = Pattern.matches("^[a-zA-Z0-9]*$", txtPw.getText());
				if(!eng) {
					JOptionPane.showMessageDialog(txtPw, "영문자/숫자만 입력하세요", "입력오류", JOptionPane.PLAIN_MESSAGE);
					txtPw.setText("");
				}
			}
			if(obj == txtName) {
				boolean koreng = Pattern.matches("^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]*$", txtName.getText());
				if(!koreng) {
					JOptionPane.showMessageDialog(txtName, "한글/영문자만 입력하세요", "입력오류", JOptionPane.PLAIN_MESSAGE);
					txtName.setText("");
				}
			}
			if(obj == txtPhone) {
				try {
					Integer.parseInt(txtPhone.getText());
				} catch (NumberFormatException nfe) { 
					JOptionPane.showMessageDialog(txtPhone, "-없이 숫자만 입력하세요\n", "입력오류", JOptionPane.PLAIN_MESSAGE);
					txtPhone.setText(""); 
					txtPhone.requestFocus(); 
				}
			}
		}
	} // KeyAdapter
	
	public void close() {
		cDao.exit();
		eDao.exit();
	}
}
