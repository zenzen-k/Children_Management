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
import javax.swing.JTable;
import javax.swing.JTextField;

import DB_table.TeacherDao;
import Main.ChildMain;

public class LoginMain extends JFrame implements ActionListener{
	
	JTextField txtId = new JTextField();
	JTextField txtPw = new JTextField();
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
		contentPane.setLayout(null);
		Font fontBold = new Font("나눔스퀘어 네오 Regular", Font.BOLD, 15);
		Font font = new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 15);
		
		JPanel pnNorth = new JPanel();
		pnNorth.setLayout(null);
		pnNorth.setBounds(125, 100, 400, 60);
		contentPane.add(pnNorth);
//		Pnorth.setBackground(Color.blue);
		JLabel lbText = new JLabel("환영합니다. 로그인을 해주세요.");
		lbText.setBounds(10,10,300,50);
		lbText.setFont(fontBold);
		pnNorth.add(lbText);
		
		// ID, PW
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(null);
		pnCenter.setBounds(125, 200, 250, 120);
		contentPane.add(pnCenter);
//		centerP.setBackground(Color.pink);
		
		JLabel lb1 = new JLabel("ID");
		JLabel lb2 = new JLabel("PW");
		lb1.setBounds(20,20,50,30);
		lb2.setBounds(20,60,50,30);
		lb1.setFont(font);
		lb2.setFont(font);
		
		txtId.setBounds(70,20,150,30);
		txtPw.setBounds(70,60,150,30);
		
		pnCenter.add(lb1);
		pnCenter.add(lb2);
		pnCenter.add(txtId);
		pnCenter.add(txtPw);
		
		//버튼
		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(null);
		pnBtn.setBounds(125,320,250,60);
		contentPane.add(pnBtn);
//		btnP.setBackground(Color.pink);
		
		btnSignUp = new JButton("sign up");
		btnSignIn = new JButton("login");
		
		btnSignUp.setBounds(30, 15, 80, 30);
		btnSignIn.setBounds(130, 15, 80, 30);
		
		pnBtn.add(btnSignUp);
		pnBtn.add(btnSignIn);
		
		btnSignUp.addActionListener(this);
		btnSignIn.addActionListener(this);
	} // Lcompose()
	
	public static void main(String[] args) {
		new LoginMain("유아 관리 프로그램 - Login");
	}
	
	/* 버튼 이벤트 처리 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == btnSignUp) {
			new T_SignUp("회원가입");
		}
		else if(obj == btnSignIn) {
			String messege, tit;
			String logid = txtId.getText();
			String logpw = txtPw.getText();
			int cnt = tdao.getLogin(logid, logpw);
			if(cnt == 0) {
				messege = "아이디와 패스워드를 확인해주세요";
				tit = "login 실패";
				JOptionPane.showMessageDialog(this, messege, tit, JOptionPane.INFORMATION_MESSAGE);
			} else {
				messege = "로그인에 성공하였습니다.";
				tit = "login 성공";
				JOptionPane.showMessageDialog(this, messege, tit, JOptionPane.INFORMATION_MESSAGE);
				new ChildMain("유아 관리 프로그램");
				setVisible(false);
			}
		}
	} // actionPerforme()
	
	class MyWindow extends WindowAdapter { 
		public void windowClosing(WindowEvent e) {
			tdao.exit();
			System.exit(0); // 프로그램 종료
		}
	}
}
