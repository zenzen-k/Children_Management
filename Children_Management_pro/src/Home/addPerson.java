package Home;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import DB_table.ClassroomBean;
import DB_table.ClassroomDao;
import DB_table.FamilyBean;
import DB_table.FamilyDao;
import DB_table.PersonBean;
import DB_table.PersonDao;
import Home.Home.MouseHandler;

public class addPerson extends JFrame implements ActionListener, ItemListener{

	// 학번은 자동입력
	private JTextField txtPerNo = new JTextField("학번은 자동 입력됩니다.");
	// 이름 성별 생년월일 입학일 주소 특이사항 - txt
	private JTextField[] txtInfo = new JTextField[5];
	// 이름 성별 생년월일 입학일 주소 특이사항 - label
	private JLabel[] lbInfo = new JLabel[6];
	private JRadioButton rbGender1 = new JRadioButton("남");
	private JRadioButton rbGender2 = new JRadioButton("여");
	private ButtonGroup btnGroup = new ButtonGroup();

	// 반 - Choice 이용
	private Choice classroom = new Choice();
	// 반 - Choice 이용 - label
	private JLabel lbclassroom = null;

	// 가족 - 관계 성명 생년월일 연락처
	private JTextField[] txtFInfo1 = new JTextField[3];
	private JTextField[] txtFInfo2 = new JTextField[3];
	// 가족 - 성명 생년월일 연락처 - label
	private JLabel[] lbFInfo = new JLabel[4];
	// 가족관계 초이스
	private Choice chFamily1 = new Choice();
	private Choice chFamily2 = new Choice();

	// 등록버튼
	private JButton btnInsert;
	private JButton btnCancle;


	//객체생성
	ClassroomDao cdao = new ClassroomDao();
	FamilyDao fdao = new FamilyDao();
	PersonDao pdao = new PersonDao();

	//이벤트관련
	int indexC = 0; // 초이스에서 선택한 인덱스를 받아옴 (classroom)
	int c_noChoice = 0; // 선택한 항목의 c_no를 저장하는 변수
	int indexF1 = 0; // 초이스에서 선택한 인덱스를 받아옴 (family1)
	String fam1Choice = null; // 선택한 항목의 문자열을 저장하는 변수
	int indexF2 = 0; // 초이스에서 선택한 인덱스를 받아옴 (family1)
	String fam2Choice = null; // 선택한 항목의 문자열을 저장하는 변수
	String gen = null;
	
	addPerson(String title) {
		super(title);
		
		compose();
		setevent();
		font();

		setSize(700, 800);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
		setVisible(true);
		setResizable(false);
	}

	/* 이벤트 관리 */
	private void setevent() {
		for(int i = 0; i<txtInfo.length; i++) {
			txtInfo[i].addKeyListener(new KeyHandler());
		}
		for(int i=0; i<txtFInfo1.length; i++) {
			txtFInfo1[i].addKeyListener(new KeyHandler());
			txtFInfo2[i].addKeyListener(new KeyHandler());
		}
	}

	/* 화면구성 */
	private void compose() {
		//화면
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//인적사항 네모칸
		JButton btnInfo = new JButton("인적사항");
		btnInfo.setBackground(Color.darkGray);
		btnInfo.setForeground(Color.white);
		btnInfo.setBounds(20, 20, 120, 25);
		contentPane.add(btnInfo);
		btnInfo.setEnabled(false);
		btnInfo.setFont(new Font("나눔스퀘어 네오 Regular", Font.BOLD, 15));

		//학번
		txtPerNo.setBounds(150, 20, 180, 25);
		contentPane.add(txtPerNo);
		txtPerNo.setEditable(false);

		// 라벨 패널 - 이름 성별 생년월일 입학일 주소 특이사항 반
		JPanel pnInfo = new JPanel();
		contentPane.add(pnInfo);
		pnInfo.setBounds(20, 60, 120, 300);
		pnInfo.setLayout(new GridLayout(7,1));

		//라벨이름설정
		lbInfo[0] = new JLabel("이름");
		lbInfo[1] = new JLabel("성별");
		lbInfo[2] = new JLabel("생년월일");
		lbInfo[3] = new JLabel("입학일");
		lbInfo[4] = new JLabel("주소");
		lbInfo[5] = new JLabel("특이사항");
		lbclassroom = new JLabel("반 선택");
		//올리기
		pnInfo.add(lbclassroom);
		for(int i = 0 ; i < lbInfo.length; i++) {
			pnInfo.add(lbInfo[i]);
		}

		//가족사항 네모칸
		JButton btnFInfo = new JButton("가족관계");
		btnFInfo.setBackground(Color.darkGray);
		btnFInfo.setForeground(Color.white);
		btnFInfo.setBounds(20, 390, 120, 25);
		contentPane.add(btnFInfo);
		btnFInfo.setEnabled(false);
		btnFInfo.setFont(new Font("나눔스퀘어 네오 Regular", Font.BOLD, 15));

		// 라벨 패널 - 관계, 이름, 생년월일 번호 640
		JPanel pnJInfo = new JPanel();
		contentPane.add(pnJInfo);
		pnJInfo.setBounds(20, 420, 120, 160);
		pnJInfo.setLayout(new GridLayout(4,1));

		//가족관계 라벨이름설정
		lbFInfo[0] = new JLabel("관계");
		lbFInfo[1] = new JLabel("성명");
		lbFInfo[2] = new JLabel("생년월일");
		lbFInfo[3] = new JLabel("연락처");
		//올리기
		for(int i = 0 ; i < lbFInfo.length; i++) {
			pnJInfo.add(lbFInfo[i]);
		}

		// 성별 라디오버튼 패널
		JPanel pngender = new JPanel();
		pngender.setLayout(new GridLayout());

		rbGender1.setFont(new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 15));
		rbGender2.setFont(new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 15));
		btnGroup.add(rbGender1);
		btnGroup.add(rbGender2);


		// 정보입력할 텍스트 패널 - 이름 성별 생년월일 입학일 주소 특이사항 반
		// 300, 120
		JPanel pnTxt = new JPanel();
		contentPane.add(pnTxt);
		pnTxt.setBounds(150, 110, 500, 250);
		pnTxt.setLayout(new GridLayout(6,1));
		//올리기
		setClassChoice();
		contentPane.add(classroom);
		classroom.setBounds(150, 75, 500, 100);
		for(int i = 0 ; i < txtInfo.length; i++) {
			txtInfo[i] = new JTextField();
		}
		pnTxt.add(txtInfo[0]);
		pngender.add(rbGender1);
		pngender.add(rbGender2);
		pnTxt.add(pngender);
		rbGender1.setSelected(true);
		for(int i = 1 ; i < txtInfo.length; i++) {
			pnTxt.add(txtInfo[i]);
		}

		// 가족관계 정보입력할 텍스트 패널 - 관계, 이름, 생년월일 번호
		JPanel pnFTxt = new JPanel();
		contentPane.add(pnFTxt);
		pnFTxt.setBounds(150, 420, 500, 160);
		pnFTxt.setLayout(new GridLayout(4,1));
		//올리기
		pnFTxt.add(chFamily1);
		pnFTxt.add(chFamily2);
		for(int i = 0 ; i < txtFInfo1.length; i++) {
			txtFInfo1[i] = new JTextField();
			txtFInfo2[i] = new JTextField();
			pnFTxt.add(txtFInfo1[i]);
			pnFTxt.add(txtFInfo2[i]);
		}

		// 저장 취소 버튼 생성
		btnInsert = new JButton("저장");
		btnCancle = new JButton("취소");

		btnInsert.setBounds(240, 600, 100, 50);
		btnCancle.setBounds(360, 600, 100, 50);

		contentPane.add(btnInsert);
		contentPane.add(btnCancle);


		// 이벤트리스너
		classroom.addItemListener(this);
		chFamily1.addItemListener(this);
		chFamily2.addItemListener(this);

		btnInsert.addActionListener(this);
		btnCancle.addActionListener(this);
		rbGender1.addActionListener(this);
		rbGender2.addActionListener(this);

	}

	/* 폰트설정 */
	private void font() {
		Font font = new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 15);
		Font fontBold = new Font("나눔스퀘어 네오 Regular", Font.BOLD, 15);
		Font fontL = new Font("나눔스퀘어 네오 Regular", Font.BOLD, 16);

		txtPerNo.setFont(fontBold);
		btnInsert.setFont(font);
		btnCancle.setFont(font);
		lbclassroom.setFont(fontBold);
		classroom.setFont(fontL);
		chFamily1.setFont(fontL);
		chFamily2.setFont(fontL);

		for (int i = 0; i < txtInfo.length; i++) {
			txtInfo[i].setFont(font);
		}
		for (int i = 0; i < lbInfo.length; i++) {
			lbInfo[i].setFont(fontBold);
		}

		for( int i = 0 ; i< txtFInfo1.length; i++) {
			txtFInfo1[i].setFont(font);
			txtFInfo2[i].setFont(font);
		}
		for( int i = 0 ; i< lbFInfo.length; i++) {
			lbFInfo[i].setFont(fontBold);
		}
	} //compose

	/* choice 데이터 추가 */
	private void setClassChoice() {
		// 클래스초이스
		classroom.add("클래스를 선택하세요");

		ArrayList<ClassroomBean> lists = cdao.setClassChoice();
		for(ClassroomBean cb : lists) {
			classroom.add(cb.getName());
		}

		// 가족관계 초이스
		String[] familyR = {"가족관계를 선택하세요", "조부", "조모", "부", "모", "형제","기타"};
		for(int i = 0; i<familyR.length; i++) {
			chFamily1.add(familyR[i]);
			chFamily2.add(familyR[i]);
		}

	} // setClassChoice

	@Override
	public void itemStateChanged(ItemEvent e) {
		// 클래스 아이템이벤트
		indexC = classroom.getSelectedIndex();
		// 각 선택한 항목 인덱스번호를 가져와 해당 내용을 String에 저장한다. 
		String classCh = classroom.getItem(indexC);
		// 선택한 항목에 대한 c_no 정보를 저장함
		c_noChoice = cdao.classNo(classCh);

		// 가족관계 아이템 이벤트
		indexF1 = chFamily1.getSelectedIndex();
		indexF2 = chFamily2.getSelectedIndex();
		fam1Choice = chFamily1.getItem(indexF1);
		fam2Choice = chFamily2.getItem(indexF2);

	} // itemStateChanged

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		gen = rbGender1.isSelected() ? rbGender1.getText() : rbGender2.getText();
		if(obj == btnInsert) {
			boolean result = checkData(); // 데이터체크
			if(result == true) {
				System.out.println("저장");
				int p_no = pdao.personNum(txtInfo[2].getText());
				infoInsert(p_no);
				finfoInsert(p_no);
			}
		}
		else if (obj == btnCancle) {
			System.out.println("취소");
			clearTextField();
			close();
			dispose();
		}

	} // actionPerformed
	
	//가족테이블에 데이터 추가
	private void finfoInsert(int no) {
		FamilyBean fb1 = new FamilyBean();
		FamilyBean fb2 = new FamilyBean();
		fb1.setP_no(no);
		fb1.setF_relations(fam1Choice);
		fb1.setF_name(txtFInfo1[0].getText());
		fb1.setF_birth(txtFInfo1[1].getText());
		fb1.setF_phone(txtFInfo1[2].getText());
			
		fb2.setP_no(no);
		fb2.setF_relations(fam2Choice);
		fb2.setF_name(txtFInfo2[0].getText());
		fb2.setF_birth(txtFInfo2[1].getText());
		fb2.setF_phone(txtFInfo2[2].getText());
		
		int cnt1 = fdao.finfoInsert(fb1);
		int cnt2 = fdao.finfoInsert(fb2);
		System.out.println("cnt1 : " + cnt1);
		System.out.println("cnt2 : " + cnt2);
		if(cnt1 > 0 && cnt2 > 0) {
			System.out.println("Family insert 성공");
			int result = JOptionPane.showConfirmDialog
					(this, "등록완료. 더 등록하시겠습니까?", "success",JOptionPane.YES_NO_OPTION);
			if(result == 1) {
				close();
				dispose();
			}
			else
				clearTextField();	
		}
		else if(cnt1 == 0 || cnt2 == 0) 
			System.out.println("SQL오류");
		else {
			System.out.println("insert 실패");
			JOptionPane.showMessageDialog(this, "실패. 관리자에게 문의", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
	//학생테이블에 데이터 추가
	private void infoInsert(int no) {
		PersonBean pb = new PersonBean();
		pb.setP_no(no);
		pb.setP_name(txtInfo[0].getText());
		pb.setP_birth(txtInfo[1].getText());
		pb.setGender(gen);
		pb.setP_entran(txtInfo[2].getText());
		pb.setAddr(txtInfo[3].getText());
		pb.setImg_id(0);
		pb.setC_no(c_noChoice);
		pb.setNote(txtInfo[4].getText());

		int cnt = pdao.infoInsert(pb);
		System.out.println("cnt : " + cnt);
		if(cnt > 0)
			System.out.println("insert 성공");
		else if(cnt == 0) 
			System.out.println("SQL오류");
		else {
			System.out.println("insert 실패");
			JOptionPane.showMessageDialog(this, "실패. 관리자에게 문의", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
		
	} //infoInsert
	
	
	// 데이터 검사
	private boolean checkData() {
		int checkInfo[] = {10, 50, 50, 40, 33};
		int checkFInfo[] = {10, 50, 11};
		// 클래스선택
		if(indexC == 0) {
			JOptionPane.showMessageDialog(this, "클래스를 선택하세요", "NoData", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		//가족관계선택
		if(indexF1 == 0) {
			JOptionPane.showMessageDialog(this, "가족관계1를 선택하세요", "NoData", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if(indexF2 == 0) {
			JOptionPane.showMessageDialog(this, "가족관계2를 선택하세요", "NoData", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		// 정보입력검사
		for(int i=0; i<txtInfo.length; i++) {
			if(txtInfo[i].getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "입력하지 않은 값이 있습니다.", "NoData", JOptionPane.INFORMATION_MESSAGE);
				return false;
			} else if(txtInfo[i].getText().length() > checkInfo[i]) {
				JOptionPane.showMessageDialog(this, "입력값 초과 이름:10글자, \n 주소:40글자, 메모:33글자 입력 가능", "FullData", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		//가족입력검사
		for(int i=0; i<txtFInfo1.length; i++) {
			if(txtFInfo1[i].getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "입력하지 않은 값이 있습니다.", "NoData", JOptionPane.INFORMATION_MESSAGE);
				return false;
			} else if(txtFInfo1[i].getText().length() > checkFInfo[i]) {
				JOptionPane.showMessageDialog(this, "입력값 초과 \n 이름:10글자 입력가능", "FullData", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			if(txtFInfo2[i].getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "입력하지 않은 값이 있습니다.", "NoData", JOptionPane.INFORMATION_MESSAGE);
				return false;
			} else if(txtFInfo2[i].getText().length() > checkFInfo[i]) {
				JOptionPane.showMessageDialog(this, "입력값 초과 \n 이름:10글자 입력가능", "FullData", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		return true;
	}
	
	class KeyHandler extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			Object obj = e.getSource();
			if(obj == txtInfo[0]) { // 이름
				// 소문자 : a-z / 대문자 : A-Z / 숫자 : 0-9 / 한글 : ㄱ-ㅎㅏ-ㅣ가-힣
				boolean eng = Pattern.matches("^[a-zA-Z0-9]*$", txtInfo[0].getText());
				if(!eng) {
					JOptionPane.showMessageDialog(txtInfo[0], "영문자/숫자만 입력하세요", "입력오류", JOptionPane.INFORMATION_MESSAGE);
					txtInfo[0].setText("");
				}
			}
			if(obj == txtInfo[1]) { // 생년월일
				
			}
		}
	}

	// 텍스트필드 초기화
	private void clearTextField() {
		for(int i = 0; i<txtFInfo1.length; i++) {
			txtFInfo1[i].setText("");
			txtFInfo2[i].setText("");
		}
		for(int i = 0; i<txtInfo.length; i++) {
			txtInfo[i].setText("");
		}
		txtPerNo.setText("");
	} // clearTextField

	// 접속종료
	public void close() {
		fdao.exit();
		cdao.exit();
	}

}
