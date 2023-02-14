import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;

import DB_table.TeacherDao;
import Main.ChildMain;

public class LoginMain extends JFrame implements ActionListener{
	
	JTextField txtId = new JTextField();
	// 비밀번호를 안보이게 입력받기 위해서 정의함. 일반 텍스트필드로 하면 우측 코드 사용불가 -> txtPw.setEchoChar('*');
	JPasswordField txtPw = new JPasswordField();
	JTable table = null;
	JButton btnSignUp, btnSignIn;
	
	TeacherDao tdao = new TeacherDao();
	
	public LoginMain(String title){
		super(title);
		
		// 화면불러오기
		Lcompose();
		
		// 창설정. 창크기 고정
		setSize(500, 600);
		setLocation(400, 250);
		setVisible(true);
		setResizable(false);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new MyWindow());
	}
	
	/* 화면 구성 메서드 */
	private void Lcompose() {
		// 작업영역설정
		Container contentPane = getContentPane();
		contentPane.setLayout(null); // 기본배치관리자 사용X
		
		//폰트 설정
		Font fontBold = new Font("나눔스퀘어 네오 Regular", Font.BOLD, 15);
		Font font = new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 15);
		
		//환영인사
		JPanel pnNorth = new JPanel();
		pnNorth.setLayout(null); // 기본배치관리자 사용X
		pnNorth.setBounds(125, 100, 400, 60);
		contentPane.add(pnNorth); 
		JLabel lbText = new JLabel("환영합니다. 로그인을 해주세요.");
		lbText.setBounds(10,10,300,50);
		lbText.setFont(fontBold);
		pnNorth.add(lbText);
		
		// ID, PW
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(null);
		pnCenter.setBounds(125, 200, 250, 120);
		contentPane.add(pnCenter);
		
		JLabel lb1 = new JLabel("ID");
		JLabel lb2 = new JLabel("PW");
		lb1.setBounds(20,20,50,30);
		lb2.setBounds(20,60,50,30);
		lb1.setFont(font);
		lb2.setFont(font);
		
		txtId.setBounds(70,20,150,30);
		txtPw.setBounds(70,60,150,30);
		txtPw.setEchoChar('*');
		
		pnCenter.add(lb1);
		pnCenter.add(lb2);
		pnCenter.add(txtId);
		pnCenter.add(txtPw);
		
		//버튼
		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(null);
		pnBtn.setBounds(125,320,250,60);
		contentPane.add(pnBtn);
		
		btnSignUp = new JButton("sign up");
		btnSignIn = new JButton("login");
		
		btnSignUp.setBounds(30, 15, 80, 30);
		btnSignIn.setBounds(130, 15, 80, 30);
		
		pnBtn.add(btnSignUp);
		pnBtn.add(btnSignIn);
		
		btnSignUp.addActionListener(this);
		btnSignIn.addActionListener(this);
	} // Lcompose()
	
	//프레임 호출
	public static void main(String[] args) {
		new LoginMain("유아 관리 프로그램 - Login");
	}
	
	/* 버튼 이벤트 처리 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		// 불러온 객체가 어떤 버튼인지 확인해서 맞는 이벤트 처리하기
		if(obj == btnSignUp) {
			// 회원가입 버튼을 클릭하면 회원가입 객체를 생성한다. -> 생성자불러옴
			new T_SignUp("회원가입"); 
		}
		
		// 로그인 버튼
		else if(obj == btnSignIn) {
			String messege, tit;
			String logid = txtId.getText();
			String logpw = txtPw.getText();
			// 교사 객체로 텍스트필드로부터 id와 pw 정보를 읽어와서 데이터를 보냄
			int cnt = tdao.getLogin(logid, logpw);
			if(cnt == 0) {
				messege = "아이디와 패스워드를 확인해주세요";
				tit = "login 실패";
				JOptionPane.showMessageDialog(this, messege, tit, JOptionPane.INFORMATION_MESSAGE);
				// 실패시 아이디와 비밀번호 초기화
				txtId.setText("");
				txtPw.setText("");
			} else {
				messege = "로그인에 성공하였습니다.";
				tit = "login 성공";
				JOptionPane.showMessageDialog(this, messege, tit, JOptionPane.INFORMATION_MESSAGE);
				new ChildMain("유아 관리 프로그램");
				// 로그인 성공 시 로그인 창을 숨긴다.
				setVisible(false);
			}
		}
	} // actionPerforme()
	
	// MyWindow
	class MyWindow extends WindowAdapter { 
		public void windowClosing(WindowEvent e) {
			tdao.exit();
			System.exit(0); // 프로그램 종료
		}
	}
}
