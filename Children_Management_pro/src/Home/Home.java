package Home;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.LineBorder;

import DB_table.*;
import Login.LoginMain;

public class Home extends JFrame implements ActionListener{

	//컬럼명 rownum - 인덱스번호+1 , p_no, p_name, p_birth, p_entran, c_name, c_age, t_name 
	// order by p_name;
	private String[] columnName = {"NO","유아번호","이름","생년월일","입학일","교실","연령(만)","담임교사"};
	private Object[][] rowData; // 테이블내용
	private JTable table = null;
	private JScrollPane scrollPane = null;

	JButton btnLogout, btnExitPro, btnSelect, btnInsert, btnSort, btnFilter, btnUpdate, btnUpImg, btnDelete;
	JLabel imgLabel = new JLabel(""); // 이미지
	JTextField txtSelcNo = new JTextField();
	JTextField txtSelcName = new JTextField();
	JTextField[] txtInfo = new JTextField[6];
	JTextField txtAddr = new JTextField();
	JTextField txtNote = new JTextField();
	JTextField[] txtFR = new JTextField[2];
	JTextField[] txtFName = new JTextField[2];
	JTextField[] txtFbirth = new JTextField[2];
	JTextField[] txtPhone = new JTextField[2];

	JLabel lbHome;
	JLabel lbCheck;

	//신체
	JLabel[] lbAge = new JLabel[6];
	JLabel[] lbTitle = new JLabel[4]; 
	JTextField[][] txtPhysical = new JTextField[6][3];
	JButton btnUpdatePhy = new JButton();
	
	
	private Font font = new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 13);
	private Font fontBold = new Font("나눔스퀘어 네오 Regular", Font.BOLD, 13);
	private boolean flag = false;
	private boolean pflag = false;
	private int row; //선택 행번호

	// 정의
	private PersonDao pdao = null;
	private JoinDao jdao = null;
	private FamilyDao fdao = null;
	private ClassroomDao cdao = null;
	private PhysicalDao phdao = null;
	private ArrayList<JoinBean> jlists = null;
	private String loginInfo;


	/* 생성자 */
	public Home(String title, String id) {
		super(title);
		System.out.println("생성자");
		//로그인 정보 - 아이디
		loginInfo = id;

		// 테이블 값 가져오기
		jdao = new JoinDao();
		jlists = jdao.getJoinTable();
		rowData = new Object[jlists.size()][columnName.length];

		// 가져온 데이터 넣기
		dataInput();
		table = new JTable(rowData, columnName); // rowData의 값을 table에 올린다.
		scrollPane = new JScrollPane(table);

		// 화면구성
		tableSet();
		Lcompose();
		Rcompose();
		Pcompose();

		//이벤트
		setevent();

		// 창설정. 창크기 고정
		setSize(1280, 800);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - this.getSize().width)/2, (screenSize.height - this.getSize().height)/2);
		setVisible(true);
		setResizable(false);
	} // ChildMain생성자

	/* 이벤트 관리 */
	private void setevent() {
		System.out.println("setevent");

		// 완전종료!
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 테이블을 클릭하면 MouseHandler로 가기!
		table.addMouseListener(new MouseHandler());

	}

	/* 메인화면 구상 - 왼쪽*/
	private void Lcompose() {
		System.out.println("Lcompose");

		// 작업영ㅇ역설정
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		scrollPane.setBounds(20, 110, 560, 630); // 스크롤바 크기
		contentPane.add(scrollPane);


		// 상단패널 설정 - 로그아웃종료 / 사용자라벨 / 홈화면과 출결상황화면 관리 버튼 -> 라벨로 클릭이벤트 사용하깅.
		JPanel pnMenuBar = new JPanel();
		pnMenuBar.setBackground(Color.LIGHT_GRAY);
		pnMenuBar.setLayout(null);
		pnMenuBar.setBounds(0, 0, 1280, 50);
		contentPane.add(pnMenuBar);

		// 로그아웃과 종료버튼
		btnLogout = new JButton("로그아웃");
		btnExitPro = new JButton("종료");

		btnLogout.setBounds(1060, (pnMenuBar.getHeight() - 20)/2, 90, 25);
		btnExitPro.setBounds(1160, (pnMenuBar.getHeight() - 20)/2, 90, 25);
		pnMenuBar.add(btnLogout); 

		pnMenuBar.add(btnExitPro);

		//라벨에 사용자 정보를 담음
		String[] list = getLbTeacher();
		JLabel lbUser = new JLabel("Login "+list[0]+"님 반갑습니다! - "+list[1]+" / "+list[2]);
		lbUser.setBounds((pnMenuBar.getWidth() - 300)/2, (pnMenuBar.getHeight() - 20)/2, 300, 20);
		pnMenuBar.add(lbUser);

		// 프레임관리라벨
		lbHome = new JLabel("HOME");
		lbCheck = new JLabel("출결관리");
		lbHome.setBounds(30, (pnMenuBar.getHeight() - 20)/2, 80, 25);
		lbCheck.setBounds(120, (pnMenuBar.getHeight() - 20)/2, 80, 25);
		lbCheck.setForeground(Color.GRAY); // 글자색변경
		pnMenuBar.add(lbHome);
		pnMenuBar.add(lbCheck);
		lbCheck.addMouseListener(new MouseHandler());

		// 좌측 상단 버튼 - 이름검색, 추가, 필터, 정렬, 삭제
		JPanel pnLeft = new JPanel();
		pnLeft.setBounds(20, 50, 560, 90);
		pnLeft.setLayout(null);
		contentPane.add(pnLeft);

		btnSelect = new JButton("검색");
		btnInsert = new JButton("추가");
		btnFilter = new JButton("필터");
		btnSort = new JButton("정렬");
		btnDelete = new JButton("삭제");

		int n = 300;
		txtSelcName.setBounds(60, 20, 100, 25);
		btnSelect.setBounds(0, 20, 60, 25);
		btnDelete.setBounds(305, 20, 60, 25);
		btnInsert.setBounds(370, 20, 60, 25);
		btnFilter.setBounds(435, 20, 60, 25);
		btnSort.setBounds(500, 20, 60, 25);

		pnLeft.add(txtSelcName); // like 로 조회하깅!
		pnLeft.add(btnSelect);
		pnLeft.add(btnInsert);
		pnLeft.add(btnFilter);
		pnLeft.add(btnSort);
		pnLeft.add(btnDelete);


		//폰트
		btnLogout.setFont(font);
		btnExitPro.setFont(font);
		lbUser.setFont(fontBold);
		lbHome.setFont(fontBold);
		lbCheck.setFont(fontBold);
		btnSelect.setFont(font);
		btnInsert.setFont(font);
		btnFilter.setFont(font);
		btnSort.setFont(font);
		txtSelcName.setFont(font);
		btnDelete.setFont(font);

	} // Lcompose();

	/* 메인화면 구상 - 오른쪽*/
	private void Rcompose() {
		System.out.println("Rcompose");

		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		// 우측 패널 설정
		JPanel pnRight = new JPanel();
		pnRight.setLayout(null);
		pnRight.setBounds(600, 50, 650, 280);
		contentPane.add(pnRight);
		//pnRight.setBackground(Color.pink);

		//인적사항
		JButton btnInfo = new JButton("인적사항");
		btnInfo.setBackground(Color.darkGray);
		btnInfo.setForeground(Color.white);
		btnInfo.setBounds(0,20,120,25);
		pnRight.add(btnInfo);
		btnInfo.setEnabled(false); // 라벨대신써봤움 나중에 기능넣고싶으면 변경하기

		// 학번 수정불간ㅇ
		txtSelcNo.setBounds(150,20,120,25);
		txtSelcNo.setEditable(false);
		pnRight.add(txtSelcNo);

		//인적사항 - 이미지칸
		ImageIcon icon = imgsize();
		imgLabel.setIcon(icon);
		imgLabel.setBounds(0,60,150,200);
		pnRight.add(imgLabel);

		//인적사항 - 내용
		JPanel jpnInfo = new JPanel();
		jpnInfo.setBounds(150, 60, 500, 50);
		jpnInfo.setLayout(new GridLayout(2,6));
		//jpnInfo.setBackground(Color.pink);
		pnRight.add(jpnInfo);

		//라벨에 박스그어주깅
		LineBorder lB = new LineBorder(Color.gray, 1, true);
		// 인적사항 위에2줄
		JLabel[] lbarr = new JLabel[6];
		for(int i=0; i<lbarr.length; i++) {
			switch(i) {
			case 0: lbarr[i] = new JLabel("이름"); break;
			case 1: lbarr[i] = new JLabel("성별"); break;
			case 2: lbarr[i] = new JLabel("반"); break;
			case 3: lbarr[i] = new JLabel("생년월일"); break;
			case 4: lbarr[i] = new JLabel("입학일"); break;
			case 5: lbarr[i] = new JLabel("교사"); break;
			}
			lbarr[i].setBorder(lB);
			lbarr[i].setFont(font);
			jpnInfo.add(lbarr[i]);
			txtInfo[i] = new JTextField();
			txtInfo[i].setFont(font);
			jpnInfo.add(txtInfo[i]);
			// 입력불가능
			txtInfo[i].setEditable(false);
		}

		// 인적사항 주소
		JLabel lbAddr = new JLabel("주소");
		lbAddr.setBounds(152, 110, 81, 25);
		pnRight.add(lbAddr);
		lbAddr.setBorder(lB);
		txtAddr.setBounds(233, 110, 415, 25);
		pnRight.add(txtAddr);

		// 인적사라-가족관계
		JLabel lbFamily = new JLabel("가족관계");
		lbFamily.setBounds(152, 135, 81, 100);
		pnRight.add(lbFamily);
		lbFamily.setBorder(lB);

		JPanel jpnInfo2 = new JPanel();
		jpnInfo2.setBounds(233, 135, 416, 100);
		jpnInfo2.setLayout(new GridLayout(4,3));
		//jpnInfo.setBackground(Color.pink);
		pnRight.add(jpnInfo2);

		JLabel lbFR = new JLabel("관계");
		JLabel lbFName = new JLabel("성명");
		JLabel lbFbirth = new JLabel("생년월일");
		JLabel lbPhone = new JLabel("연락처");

		// 라벨선그어주기
		lbFR.setBorder(lB);
		lbFName.setBorder(lB);
		lbFbirth.setBorder(lB);
		lbPhone.setBorder(lB);

		for(int i=0; i < txtFR.length; i++) {
			txtFR[i] = new JTextField();
			txtFName[i] = new JTextField();
			txtFbirth[i] = new JTextField();
			txtPhone[i] = new JTextField();
		}

		jpnInfo2.add(lbFR);
		jpnInfo2.add(txtFR[0]);
		jpnInfo2.add(txtFR[1]);
		jpnInfo2.add(lbFName);
		jpnInfo2.add(txtFName[0]);
		jpnInfo2.add(txtFName[1]);
		jpnInfo2.add(lbFbirth);
		jpnInfo2.add(txtFbirth[0]);
		jpnInfo2.add(txtFbirth[1]);
		jpnInfo2.add(lbPhone);
		jpnInfo2.add(txtPhone[0]);
		jpnInfo2.add(txtPhone[1]);

		//특이사항
		JLabel lbNote = new JLabel("특이사항");
		lbNote.setBounds(152, 235, 81, 25);
		pnRight.add(lbNote);
		lbNote.setBorder(lB);
		txtNote.setBounds(233, 236, 415, 25);
		pnRight.add(txtNote);

		// 우측상단 버튼 - 추가/수정, 사진업로드
		btnUpdate = new JButton("수정");
		btnUpImg = new JButton("사진 업로드");
		btnUpdate.setBounds(585, 20, 60, 25);
		btnUpImg.setBounds(460, 20, 110, 25);
		pnRight.add(btnUpdate);
		pnRight.add(btnUpImg);

		// 폰트 및 텍스트 - 수정금지 설정
		btnInfo.setFont(fontBold);
		lbAddr.setFont(font);
		txtAddr.setFont(font);
		lbFamily.setFont(font);
		lbFR.setFont(font);
		lbFName.setFont(font);
		lbFbirth.setFont(font);
		lbPhone.setFont(font);
		for(int i=0; i < txtFR.length; i++) {
			txtFR[i].setFont(font);
			txtFName[i].setFont(font);
			txtFbirth[i].setFont(font);
			txtPhone[i].setFont(font);

			txtFName[i].setEditable(false);
			txtFbirth[i].setEditable(false);
			txtFR[i].setEditable(false);
			txtPhone[i].setEditable(false);
		}
		lbNote.setFont(font);
		txtNote.setFont(font);
		btnUpdate.setFont(font);
		btnUpImg.setFont(font);
		txtSelcNo.setFont(font);

		txtAddr.setEditable(false);
		txtNote.setEditable(false);

		// 이벤트리스너
		btnLogout.addActionListener(this);
		btnExitPro.addActionListener(this);
		btnSelect.addActionListener(this);
		btnInsert.addActionListener(this);
		btnSort.addActionListener(this);
		btnFilter.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnUpImg.addActionListener(this);
		btnDelete.addActionListener(this);
		
	} //Rcompose()
	
	/* 메인화면 구상 - 신체정보 */
	private void Pcompose() {
		System.out.println("Pcompose");
		LineBorder lB = new LineBorder(Color.gray, 1, true);
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		
		JPanel pnPhysical = new JPanel();
		pnPhysical.setLayout(null);
		pnPhysical.setBounds(600, 340, 650, 220);
		contentPane.add(pnPhysical);

		//신체발달
		JButton btnPhysical = new JButton("신체발달");
		btnPhysical.setBackground(Color.darkGray);
		btnPhysical.setForeground(Color.white);
		btnPhysical.setBounds(0,0,120,25);
		pnPhysical.add(btnPhysical);
		btnPhysical.setEnabled(false);
		btnPhysical.setFont(font);
		
		btnUpdatePhy = new JButton("수정");
		btnUpdatePhy.setBounds(585, 0, 60, 25);
		pnPhysical.add(btnUpdatePhy);
		btnUpdatePhy.setFont(font);
		
		//내용
		JPanel pbPhyTitle = new JPanel();
		pbPhyTitle.setBounds(0, 40, 650, 25);
		pbPhyTitle.setLayout(new GridLayout());
		pnPhysical.add(pbPhyTitle);
//		pbPhyTitle.setBackground(Color.pink);
		
		lbTitle[0] = new JLabel("연령");
		lbTitle[1] = new JLabel("검사일자");
		lbTitle[2] = new JLabel("몸무게");
		lbTitle[3] = new JLabel("키");
		

		int i=0;
		for(i=0; i<lbTitle.length; i++) {
			lbTitle[i].setFont(font);
			lbTitle[i].setBorder(lB);
			pbPhyTitle.add(lbTitle[i]);
		}
		
		JPanel pbPhyData = new JPanel();
		pbPhyData.setBounds(0, 65, 650, 150);
		pbPhyData.setLayout(new GridLayout(6,4));
		pnPhysical.add(pbPhyData);
//		pbPhyData.setBackground(Color.pink);
		
		lbAge[0] = new JLabel("만 3세 1학기");
		lbAge[1] = new JLabel("만 3세 2학기");
		lbAge[2] = new JLabel("만 4세 1학기");
		lbAge[3] = new JLabel("만 4세 2학기");
		lbAge[4] = new JLabel("만 5세 1학기");
		lbAge[5] = new JLabel("만 5세 2학기");
		
		
		for(i=0; i<lbAge.length; i++) {
			lbAge[i].setFont(font);
			lbAge[i].setBorder(lB);
			pbPhyData.add(lbAge[i]);
			for(int j=0; j<txtPhysical[i].length; j++) {
				txtPhysical[i][j] = new JTextField();
				txtPhysical[i][j].setFont(font);
				pbPhyData.add(txtPhysical[i][j]);
				System.out.println(i + " " + j);
			}
		}
		//수정불가
		for (i = 0; i < txtPhysical.length; i++) {
			for(int j=0; j<txtPhysical[i].length; j++) {
				txtPhysical[i][j].setEditable(false);
			}
		}
		
		// 이벤트 리스너
		btnUpdatePhy.addActionListener(this);
		
	} //Pcompose()
	
	
	
	
	/* 가져온 데이터 rowData에넣기 */
	private void dataInput() {
		System.out.println("dataInput");

		Object[] arr = jlists.toArray(); // 배열로변환
		System.out.println("arr : " + arr.length);
		int j=0, x=1; // x 순번
		for(int i=0; i< arr.length; i++) {
			JoinBean jb = (JoinBean)arr[i];
			rowData[i][j++] = x++;
			rowData[i][j++] = jb.getP_no();
			rowData[i][j++] = jb.getP_name();
			rowData[i][j++] = jb.getP_birth();
			rowData[i][j++] = jb.getP_entran();
			rowData[i][j++] = jb.getC_name();
			rowData[i][j++] = jb.getC_age();
			rowData[i][j++] = jb.getT_name();
			j = 0;      
		}
	} // dataInput()

	// 이미지크기조정해쥬는메서드!!!!!!!!
	// 이미지아이콘을 이미지로 바꿔주고 사이즈조정한뒤 다시 아이콘으로 변경해야한다.
	private ImageIcon imgsize() {
		System.out.println("imgsize");

		ImageIcon icon = new ImageIcon("image/0.png");
		Image img = icon.getImage();
		Image imgsize = img.getScaledInstance(150, 200, Image.SCALE_SMOOTH); // 사이즈조정해주기!
		// getScaledInstance -> 품질유지한 채 시이즈 변경하기
		icon = new ImageIcon(imgsize);

		return icon;
	} //imgsize

	/* 테이블데이터 불러오는 메서드 */
	private void getJoinTable() {
		System.out.println("getJoinTable");
		
		// 생성자에서 했던 것과 동일한
		jlists = jdao.getJoinTable();
		rowData = new Object[jlists.size()][columnName.length];
		dataInput();
		table = new JTable(rowData, columnName); // rowData의 값을 table에 올린다.
		scrollPane.setViewportView(table);
		setevent();
	} // getJoinTable

	/* 사용자 정보 가져오는 메서드 */
	private String[] getLbTeacher() {
		System.out.println("getLbTeacher");
		String[] lists = jdao.getLbTeacher(loginInfo);
		return lists;
	} // getTeacher

	/* 검색 데이터 불러오는 메서드 */
	private void getSearch(String name) {
		System.out.println("getSearch");
		jlists = jdao.getSearch(name);

		if(jlists.size() < 1) {
			JOptionPane.showMessageDialog
			(this, "찾는 이름이 없습니다. 다시 입력하세요", "NO DATA", JOptionPane.INFORMATION_MESSAGE);
			txtSelcName.requestFocus();
			txtSelcName.setText("");
		}
		else {
			System.out.println(jlists.size());

			rowData = new Object[jlists.size()][columnName.length];
			dataInput();
			table = new JTable(rowData, columnName); // rowData의 값을 table에 올린다.
			scrollPane.setViewportView(table);
			tableSet();

			table.addMouseListener(new MouseHandler());
		}
	} // getJoinTable

	/* 전체 텍스트필드 초기화 */
	private void clearTxtInfo() {
		for(int i =0; i< txtInfo.length; i++) {
			txtInfo[i].setText("");
		}
		txtAddr.setText("");
		txtNote.setText("");
		for(int i=0; i< txtFR.length; i++) {
			txtFR[i].setText("");
			txtFName[i].setText("");
			txtFbirth[i].setText("");
			txtPhone[i].setText("");
		}
		for (int i = 0; i < txtPhysical.length; i++) {
			for(int j=0; j<txtPhysical[i].length; j++) {
				txtPhysical[i][j].setText("");
			}
		}
	} // clearTxtInfo

	// 연락처 폼 -> 이렇게도 할 수 있고, Pattern.matches 를 사용 가능하다! addPerson클래스 참고
	private String phoneFormat(String phone) {
		StringBuffer buf = new StringBuffer(phone);
		if(phone.contains("-")) {
			buf.insert(3, "-");
			buf.insert(8, "-");
			String rePhone = buf.toString();
			return rePhone;
		}
		return phone;
	} // phoneFormat

	/* 마우스 이벤트 처리*/
	class MouseHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			System.out.println("mouse");
			Object obj = e.getSource();
			if (obj == lbCheck) {
				new AttendManage("유아 관리 프로그램 - 출결관리", loginInfo);
				dispose();
			}
			else {
				clearTxtInfo();
				row = table.getSelectedRow();
				// 선택한 행의 학생번호를 가지고 mouseInfo를 호출한다.
				int no = (int)table.getValueAt(row, 1);
				mouseInfo(no);
				mousePhysical(no);
			}
		} // mouseClicked

		/* 클릭한 정보를 토대로 우측 신체발달 텍스트 필드에 추가 */
		private void mousePhysical(int no) {
			System.out.println("mousePhysical");
			
			phdao = new PhysicalDao();
			ArrayList<PhysicalBean> lists = phdao.getAllInfo(no);
			
			for(int i=0; i<lists.size(); i++) {
					txtPhysical[i][0].setText(lists.get(i).getPd_date()); // i -> 0
					txtPhysical[i][1].setText(String.valueOf(lists.get(i).getWeight()));
					txtPhysical[i][2].setText(String.valueOf(lists.get(i).getHeight()));
			}
		} //mousePhysical
		
		/* 클릭한 정보를 토대로 우측 인적사항 텍스트 필드에 추가 */
		public void mouseInfo(int p_no) {
			System.out.println("mouseInfo");

			pdao = new PersonDao();
			ArrayList<JoinBean> lists = pdao.getAllInfo(p_no);

			int i =0;
			txtInfo[i++].setText(lists.get(0).getP_name());
			txtInfo[i++].setText(lists.get(0).getGender());
			txtInfo[i++].setText(lists.get(0).getC_name());
			txtInfo[i++].setText(lists.get(0).getP_birth());
			txtInfo[i++].setText(lists.get(0).getP_entran());
			txtInfo[i++].setText(lists.get(0).getT_name());
			txtAddr.setText(lists.get(0).getAddr());
			txtNote.setText(lists.get(0).getP_note());
			txtSelcNo.setText(String.valueOf(lists.get(0).getP_no()));

			fdao = new FamilyDao();
			ArrayList<FamilyBean> flists = fdao.getAllFam(p_no);

			i = 0;
			for(FamilyBean x : flists) {
				txtFR[i].setText(x.getF_relations());
				txtFName[i].setText(x.getF_name());
				txtFbirth[i].setText(x.getF_birth());
				txtPhone[i].setText(phoneFormat(x.getF_phone()));
				i++;
			}
		} // mouseInfo
	} // MouseHandler

	/* 액션 이벤트 처리 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed");
		Object obj = e.getSource();
		//수정확인용  0 -> 수정불가 , 1 -> 수정가능

		if(obj == btnLogout) { // 로그아웃
			System.out.println("로그아웃");
			int result = JOptionPane.showConfirmDialog // 확인하는 메세지. 확인 - 0 , 아니오 - 1, 취소 - 2 닫기 - -1 리턴
					(this, "로그아웃 하시겠습니까?","Logout",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
			System.out.println(result);
			if(result == 0) {
				dispose(); // 프레임창종료
				new LoginMain("유아 관리 프로그램 - Login");
			}
		}
		else if(obj == btnExitPro) {
			System.out.println("종료");
			int result = JOptionPane.showConfirmDialog
					(this, "프로그램을 종료 하시겠습니까?","Exit",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
			if(result == 0) {
				close();
				System.exit(0);
			}
		}
		else if(obj == btnSelect) { // 검색
			System.out.println("검색");
			String selcName = txtSelcName.getText();
			if(!selcName.equals("")) {
				getSearch(selcName);
			}
			else {
				getJoinTable(); // 아무것도 없으면 전체조회하기
				//JOptionPane.showMessageDialog(this, "검색할 이름을 입력하세요", "ERROR", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if(obj == btnDelete) { // 삭제
			System.out.println("삭제");
			personDelete();
			getJoinTable();
		}
		else if(obj == btnInsert) { // 추가
			System.out.println("추가");
			new addPerson("유아 등록");
			getJoinTable();
		}
		else if(obj == btnSort) { // 정렬
			System.out.println("정렬");
			sortPerson();
		}
		else if(obj == btnFilter) {
			System.out.println("필터");
			filterPerson();
		}
		else if(obj == btnUpdate) { // 인적사항 수정버튼
			System.out.println("수정");
			flag = !flag;
			personUpdate();
			getSearch(txtSelcName.getText());
		}
		else if(obj == btnUpImg) {
			System.out.println("이미지업로드");
			JOptionPane.showMessageDialog(this, "접근불가 : 준비중입니다.", "No Access", JOptionPane.ERROR_MESSAGE);
		}
		else if(obj == btnUpdatePhy) { // 신체 수정버튼
			System.out.println("수정");
			pflag = !pflag;
			physicalUpdate();
		}

		tableSet();
	} //actionPerformed
	
	/* 필터 기능 */
	private void filterPerson() {
		//반이름
		cdao = new ClassroomDao();
		ArrayList<ClassroomBean> lists = cdao.setClassChoice();
		String[] a = new String[lists.size()];
		int i = 0;
		for(ClassroomBean cb : lists) {
			a[i++] = cb.getName();
		}
		//조회할거
		String str = JOptionPane.showInputDialog("조회할 교실을 선택하세요\n1:"+a[0]+" 2:"+a[1] +" 3:"+a[2] +" 4:"+a[3]);
		if(str == null) {
			return;
		}
		try {
			int num = Integer.parseInt(str);
			jlists = jdao.cNameJoin(num);
			rowData = new Object[jlists.size()][columnName.length];
			dataInput();
			table = new JTable(rowData, columnName); 
			scrollPane.setViewportView(table);
			tableSet();
			table.addMouseListener(new MouseHandler());
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "잘못된 값입니다. 보기의 숫자만 입력하세요", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/* 정렬 기능 */
	private void sortPerson() {
		try {
			String str = JOptionPane.showInputDialog("정렬 기준을 선택하세요\n1:이름 2:입학일 3:교실");
			if(str == null) {
				return;
			}
			int num = Integer.parseInt(str);
			String[] arr = new String[2];
			switch(num) {
			case 1: arr[0] = "p_name"; break;
			case 2: arr[0] = "p_entran"; break;
			case 3: arr[0] = "c_name"; break;
			default : JOptionPane.showMessageDialog(this, "잘못된 값입니다. 보기의 숫자만 입력하세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
			String str2 = JOptionPane.showInputDialog("정렬 방식을 선택하세요\n1:내림차순 2:오름차순");
			if(str2 == null) {
				return;
			}
			num = Integer.parseInt(str2);
			switch(num) {
			case 1: arr[1] = "desc"; break;
			case 2: arr[1] = "asc"; break;
			default : JOptionPane.showMessageDialog(this, "잘못된 값입니다. 보기의 숫자만 입력하세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			}

			System.out.println(arr[0] + " " + arr[1]);
			jlists = jdao.sortJoin(arr);
			rowData = new Object[jlists.size()][columnName.length];
			dataInput();
			table = new JTable(rowData, columnName);
			scrollPane.setViewportView(table);
			tableSet();
			setevent();
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "잘못된 값입니다. 보기의 숫자만 입력하세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			sortPerson();
		}
	}

	/* 삭제 기능 */
	private void personDelete() {
		try {
			row = table.getSelectedRow();
			int p_no = (int)table.getValueAt(row, 1);
			int cnt = pdao.personDelete(p_no);
			System.out.println("delete cnt : " + cnt);
			if(cnt > 0)
				JOptionPane.showMessageDialog(this, "정상적으로 삭제되었습니다", "success", JOptionPane.INFORMATION_MESSAGE);
			else 
				JOptionPane.showMessageDialog(this, "삭제 중 오류", "ERROR", JOptionPane.ERROR_MESSAGE);
		}catch(ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(this, "접근불가 : 삭제할 행을 클릭하세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}

	} // personDelete
	
	/* 신체 수정 기능 */
	private void physicalUpdate() {
		// 행을 클릭 안하고 수정버튼 눌렀을 때
		try {
			System.out.println(Integer.parseInt(txtSelcNo.getText()));
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "접근불가 : 수정할 행을 클릭하세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			pflag = false;
			return;
		}
		if(pflag == true) { // 클릭을 했을 때 수정가능상태로 바뀜
			System.out.println("수정가능");
			for (int i = 0; i < txtPhysical.length; i++) {
				for(int j=0; j<txtPhysical[i].length; j++) {
					txtPhysical[i][j].setEditable(true);
				}
				if(txtPhysical[i][0].getText().equals(" ")) {
					txtPhysical[i][0].setText("");
				}
//				if(txtPhysical[i][1].getText() == "0.0") {
//					txtPhysical[i][1].setText("");
//				}
			}
		} 
		else if(pflag == false) { // 수정 불가능 상태 + 텍스트에 있는 데이터 리턴
			System.out.println("수정불가능");

			ArrayList<PhysicalBean> lists = new ArrayList<PhysicalBean>(); 

			// 수정불가능으로 변경함
			for (int i = 0; i < txtPhysical.length; i++) {
				for(int j=0; j<txtPhysical[i].length; j++) {
					txtPhysical[i][j].setEditable(false);
				}
			}
			// 텍스트필드입력된값가져옴 - 
			for(int i=0; i<txtPhysical.length; i++) {
				boolean formCheck = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", txtPhysical[i][0].getText());
				// 공백넣깅
				if(txtPhysical[i][0].getText().length() == 0) {
					txtPhysical[i][0].setText(" ");
				}
				if(txtPhysical[i][1].getText().length() == 0) {
					txtPhysical[i][1].setText("0.0");
				}
				if(txtPhysical[i][2].getText().length() == 0) {
					txtPhysical[i][2].setText("0.0");
				}
				//저장
				if(formCheck || txtPhysical[i][0].getText().equals(" ")) {
					PhysicalBean pb = new PhysicalBean();
					pb.setPd_age(lbAge[i].getText());
					pb.setPd_date(txtPhysical[i][0].getText());
					pb.setWeight(Double.parseDouble(txtPhysical[i][1].getText())); 
					pb.setHeight(Double.parseDouble(txtPhysical[i][2].getText())); 
					pb.setP_no(Integer.parseInt(txtSelcNo.getText()));
					lists.add(pb);
				}
				else {
					JOptionPane.showMessageDialog(this, "검사일자는 공백 없이 아래형식으로 입력하세요 \n yyyy-mm-dd", "ERROR",JOptionPane.INFORMATION_MESSAGE);
					pflag = true;
					physicalUpdate();
				} //else
			}
			int cnt = phdao.physicalUpdate(lists);
			System.out.println(cnt);
		} //수정중일때 -> 
	} //physicalUpdate
	
	/* 인적사항 수정 기능 */
	private void personUpdate() {
		System.out.println("personUpdate");
		System.out.println(flag);
		// 행을 클릭 안하고 수정버튼 눌렀을 때
		try {
			System.out.println(Integer.parseInt(txtSelcNo.getText()));
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "접근불가 : 수정할 행을 클릭하세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			flag = false;
			return;
		}

		if(flag == true) { // 클릭을 했을 때 수정가능상태로 바뀜
			System.out.println("수정가능");

			txtAddr.setEditable(true);
			txtNote.setEditable(true);
			for (int i = 0; i < txtFName.length; i++) {
				txtFName[i].setEditable(true);
				txtFbirth[i].setEditable(true);
				txtPhone[i].setEditable(true);
			}

			// 수정할때 번호의 - 삭제
			String phoneNum = txtPhone[0].getText();
			String phoneNum2 = txtPhone[1].getText();
			if(phoneNum.contains("-")) {
				txtPhone[0].setText(phoneNum.replace("-",""));
				txtPhone[1].setText(phoneNum2.replace("-",""));
			}

		} 
		else if(flag == false) { // 수정불가능상태로 바뀜 + 텍스트에 있는 데이터 보내깅
			System.out.println("수정불가능");

			FamilyBean[] list = new FamilyBean[2]; // 2칸
			PersonBean pb = new PersonBean();

			// 수정불가능으로 변경함
			txtAddr.setEditable(false);
			txtNote.setEditable(false);

			// 텍스트필드입력된값가져옴
			pb.setAddr(txtAddr.getText());
			pb.setNote(txtNote.getText());
			pb.setP_no(Integer.parseInt(txtSelcNo.getText())); // 학생정보
			System.out.println(pb.getP_no());
			int pcnt = pdao.InfoUpdate(pb);

			//가족관계 수정불가능 + 텍필 입력값 list에 저장함
			for (int i = 0; i < txtFName.length; i++) {
				txtFName[i].setEditable(false);
				txtFbirth[i].setEditable(false);
				txtPhone[i].setEditable(false);

				FamilyBean fb = new FamilyBean();
				fb.setF_relations((txtFR[i].getText())); ;
				fb.setF_name(txtFName[i].getText());
				fb.setF_birth(txtFbirth[i].getText());
				fb.setF_phone(txtPhone[i].getText());
				fb.setP_no(Integer.parseInt(txtSelcNo.getText())); // 학생정보

				list[i] = fb;
				// 수정완료하면 다시설정
				phoneFormat(txtPhone[i].getText());
			}
			int fcnt = fdao.InfoUpdate(list);
			System.out.println(fcnt + " " + pcnt);
		}
	} // InfoUpdate
	
	/* 테이블 컬럼별 길이 설정 */
	private void tableSet() {
		System.out.println("tableSet");

		// 해보니까 가로길이를 설정해놨기 때문에 필요한것만 조정하면 나머지는 남은 너비에서 알아서 자동조정되는듯!
		table.getColumn("NO").setPreferredWidth(30);
		table.getColumn("연령(만)").setPreferredWidth(60);
		table.getColumn("생년월일").setPreferredWidth(100);
		table.getColumn("입학일").setPreferredWidth(100);
	} // 
	
	public void close() {
		if(cdao != null)
			cdao.exit();
		if(pdao != null)
			pdao.exit();
		if(jdao != null)
			jdao.exit();
		if(fdao != null)
			fdao.exit();
		if(phdao != null)
			phdao.exit();
	}

}
