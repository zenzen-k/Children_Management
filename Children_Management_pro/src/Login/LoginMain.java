package Login;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
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
import Home.Home;

public class LoginMain extends JFrame implements ActionListener{
	
	private JTextField txtId = new JTextField();
	// 비밀번호를 안보이게 입력받기 위해서 정의함. 일반 텍스트필드로 하면 우측 코드 사용불가 -> txtPw.setEchoChar('*');
	private JPasswordField txtPw = new JPasswordField();
	private JTable table = null;
	private JButton btnSignUp, btnSignIn;
	
	private TeacherDao tdao = new TeacherDao();
	
	public LoginMain(String title){
		super(title);
		
		// 화면불러오기
		Lcompose();
		
		// 창설정. 창크기 고정
		setSize(500, 600);
		
		// Toolkit -> 사용자 인터페이스 작성, 그래픽스, 등의 클래스이다. 여기서 사용자의 화면 크기를 알아낼 수 있다.
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 화면의 크기를 알아내는 메서드
		// this.getSize() 현재 프레임창의 사이즈를 가져온다.
		// 가운데 위치하기 위해 아래의 코드를 사용하였다.
		// (전체 화면 가로길이 - 프레임의 가로길이)/2 를 하면 프레임을 가운데 띄웠을 때 x의 위치가 어디서 시작될지 구할 수 있다. 
		// y값도 동일하게 구해준다.
		setLocation((screenSize.width - this.getSize().width)/2, (screenSize.height - this.getSize().height)/2);
		//setLocation(400, 250);
		
		setVisible(true);
		setResizable(false); // 창크기고정
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowExit());
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
		JLabel lbText = new JLabel("환영합니다. 로그인을 해주세요.");
		lbText.setBounds(135,110,300,50);
		lbText.setFont(fontBold);
		contentPane.add(lbText);
		
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
				new Home("유아 관리 프로그램", logid);
//				// 로그인 성공 시 로그인 창을 숨긴다.
//				setVisible(false);
				// 프로그램이 종료되는게 아니라 하나의 프레임만 닫히는 메서드!!!!
				dispose();
			}
		}
	} // actionPerforme()
	
	// WindowExit
	class WindowExit extends WindowAdapter { 
		public void windowClosing(WindowEvent e) {
			tdao.exit();
			System.exit(0); // 프로그램 종료
		}
	}

	//프레임 호출
	public static void main(String[] args) {
		new LoginMain("유아 관리 프로그램 - Login");
	}
}
