package Home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import DB_table.*;

public class ChildMain extends JFrame{
	
	//컬럼명 rownum - 인덱스번호+1 , p_no, p_name, p_birth, p_entran, c_name, c_age, t_name 
	// order by p_name;
	private String[] columnName = {"NO","유아번호","이름","생년월일","입학일","교실","연령","담임교사"};
	private Object[][] rowData; // 테이블내용
	private JTable table = null;
	private JScrollPane scrollPane = null;
	
	JButton btnLogout, btnExitPro, btnSelect, btnInsert, btnSort, btnFilter;
	JLabel imgLabel = new JLabel(""); // 이미지
	JTextField txtSelcName = new JTextField();
//	JTextField txtName = new JTextField(), txtGender = new JTextField(), txtClass = new JTextField(), 
//			txtBirth = new JTextField(), txtEntran = new JTextField(), txtTeacher = new JTextField();
	JTextField[] txtInfo = new JTextField[6];
	JTextField txtAddr = new JTextField();
	JTextField txtFR1 = new JTextField();
	JTextField txtFR2 = new JTextField();
	JTextField txtFName1 = new JTextField();
	JTextField txtFName2 = new JTextField();
	JTextField txtFbirth1 = new JTextField();
	JTextField txtFbirth2 = new JTextField();
	JTextField txtPhone1 = new JTextField();
	JTextField txtPhone2 = new JTextField();
	JTextField txtNote = new JTextField();
	
	private JLabel lbHome;
	private JLabel lbCheck;
	
	Font font = new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 13);
	Font fontBold = new Font("나눔스퀘어 네오 Regular", Font.BOLD, 13);
	
	
	// 정의
	PersonDao pdao = null;
	JoinDao jdao = null;
	ArrayList<JoinBean> jlists = null;
	String loginInfo;
	
	public ChildMain(String title, String id) {
		super(title);
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
		
		Lcompose();
		Rcompose();
		
		//이벤트리스너
		
		
		// 창설정. 창크기 고정
		setSize(1280, 800);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - this.getSize().width)/2, (screenSize.height - this.getSize().height)/2);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	} // ChildMain생성자
	

	/* 메인화면 구상 - 왼쪽*/
	private void Lcompose() {
		
		// 작업영ㅇ역설정
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		scrollPane.setBounds(20, 110, 560, 630); // 스크롤바 크기
		contentPane.add(scrollPane);
		// 테이블 컬럼별 길이 설정하기! 70
		// 해보니까 가로길이를 설정해놨기 때문에 필요한것만 조정하면 나머지는 남은 너비에서 알아서 자동조정되는듯!
		table.getColumn("NO").setPreferredWidth(30);
		table.getColumn("연령").setPreferredWidth(30);
		table.getColumn("생년월일").setPreferredWidth(100);
		table.getColumn("입학일").setPreferredWidth(100);
		
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
		
		// 좌측 상단 버튼 - 이름검색, 추가, 필터, 정렬
		JPanel pnLeft = new JPanel();
		pnLeft.setBounds(20, 50, 560, 90);
		pnLeft.setLayout(null);
		contentPane.add(pnLeft);
		
		btnSelect = new JButton("검색");
		btnInsert = new JButton("추가");
		btnFilter = new JButton("필터");
		btnSort = new JButton("정렬");
		
		txtSelcName.setBounds(60, 20, 100, 25);
		btnSelect.setBounds(0, 20, 60, 25);
		btnInsert.setBounds(340, 20, 60, 25);
		btnFilter.setBounds(420, 20, 60, 25);
		btnSort.setBounds(500, 20, 60, 25);
		
		pnLeft.add(txtSelcName); // like 로 조회하깅!
		pnLeft.add(btnSelect);
		pnLeft.add(btnInsert);
		pnLeft.add(btnFilter);
		pnLeft.add(btnSort);
		
		
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
		
	} // Lcompose();

	
	/* 메인화면 구상 - 오른쪽*/
	private void Rcompose() {
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		
		// 우측 패널 설정
		JPanel pnRight = new JPanel();
		pnRight.setLayout(null);
		pnRight.setBounds(600, 50, 650, 690);
		contentPane.add(pnRight);
		//pnRight.setBackground(Color.pink);
		
		//인적사항
		JButton btnInfo = new JButton("인적사항");
		btnInfo.setBackground(Color.darkGray);
		btnInfo.setForeground(Color.white);
		btnInfo.setBounds(0,20,120,25);
		pnRight.add(btnInfo);
		btnInfo.setEnabled(false); // 라벨대신써봤움 나중에 기능넣고싶으면 변경하기
		
		//인적사항 - 이미지칸
		ImageIcon icon = imgsize();
		imgLabel.setIcon(icon);
		imgLabel.setBounds(0,60,150,200);
		pnRight.add(imgLabel);
		
		//인적사항 - 내용
		JPanel jpnInfo = new JPanel();
		jpnInfo.setBounds(150, 60, 490, 50);
		jpnInfo.setLayout(new GridLayout(2,6));
//		jpnInfo.setBackground(Color.pink);
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
		}
		
		// 인적사항 주소
		JLabel lbAddr = new JLabel("주소");
		lbAddr.setBounds(152, 110, 81, 25);
		pnRight.add(lbAddr);
		lbAddr.setBorder(lB);
		txtAddr.setBounds(233, 110, 405, 25);
		pnRight.add(txtAddr);
		
		// 인적사라-가족관계
		JLabel lbFamily = new JLabel("가족상황");
		lbFamily.setBounds(152, 135, 81, 100);
		pnRight.add(lbFamily);
		lbFamily.setBorder(lB);
		
		JPanel jpnInfo2 = new JPanel();
		jpnInfo2.setBounds(233, 135, 405, 100);
		jpnInfo2.setLayout(new GridLayout(4,3));
//		jpnInfo.setBackground(Color.pink);
		pnRight.add(jpnInfo2);
		
		JLabel lbFR = new JLabel("관계");
		JLabel lbFName = new JLabel("성명");
		JLabel lbFbirth = new JLabel("생년월일");
		JLabel lbPhone = new JLabel("연락처");
		
		lbFR.setBorder(lB);
		lbFName.setBorder(lB);
		lbFbirth.setBorder(lB);
		lbPhone.setBorder(lB);
		
		jpnInfo2.add(lbFR);
		jpnInfo2.add(txtFR1);
		jpnInfo2.add(txtFR2);
		jpnInfo2.add(lbFName);
		jpnInfo2.add(txtFName1);
		jpnInfo2.add(txtFName2);
		jpnInfo2.add(lbFbirth);
		jpnInfo2.add(txtFbirth1);
		jpnInfo2.add(txtFbirth2);
		jpnInfo2.add(lbPhone);
		jpnInfo2.add(txtPhone1);
		jpnInfo2.add(txtPhone2);
		
		//특이사항
		JLabel lbNote = new JLabel("특이사항");
		lbNote.setBounds(152, 235, 81, 25);
		pnRight.add(lbNote);
		lbNote.setBorder(lB);
		txtNote.setBounds(233, 236, 405, 25);
		pnRight.add(txtNote);
		
		// 폰트설정
		btnInfo.setFont(fontBold);
		lbAddr.setFont(font);
		txtAddr.setFont(font);
		lbFamily.setFont(font);
		lbFR.setFont(font);
		lbFName.setFont(font);
		lbFbirth.setFont(font);
		lbPhone.setFont(font);
		txtFR1.setFont(font);
		txtFR2.setFont(font);
		txtFName1.setFont(font);
		txtFName2.setFont(font);
		txtFbirth1.setFont(font);
		txtFbirth2.setFont(font);
		txtPhone1.setFont(font);
		txtPhone2.setFont(font);
		lbNote.setFont(font);
		txtNote.setFont(font);
		
	} //Rcompose()
	
	// 이미지크기조정해쥬는메서드!!!!!!!!
	// 이미지아이콘을 이미지로 바꿔주고 사이즈조정한뒤 다시 아이콘으로 변경해야한다.
	private ImageIcon imgsize() {
		ImageIcon icon = new ImageIcon("image/0.png");
		Image img = icon.getImage();
		Image imgsize = img.getScaledInstance(150, 200, Image.SCALE_SMOOTH); // 사이즈조정해주기!
		// getScaledInstance -> 품질유지한 채 시이즈 변경하기
		icon = new ImageIcon(imgsize);
		
		return icon;
	}

	/* 가져온 데이터 rowData에넣기 */
	private void dataInput() {
		Object[] arr = jlists.toArray(); // 배열로변환
		int j=0, x=1;
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
			j=0;      
		}
	} // dataInput()
	
	/* 테이블데이터 불러오는 메서드 */
	private void getJoinTable() {
		// 생성자에서 했던 것과 동일한
		jlists = jdao.getJoinTable();
		rowData = new Object[jlists.size()][columnName.length];
		dataInput();
		table = new JTable(rowData, columnName); // rowData의 값을 table에 올린다.
		scrollPane.setViewportView(table);
	}
	
	/* 사용자 정보 가져오는 메서드 */
	private String[] getLbTeacher() {
		String[] lists = jdao.getLbTeacher(loginInfo);
		return lists;
	} // getTeacher
	
	//test
	public static void main(String[] args) {
		new ChildMain("유아 관리 프로그램","nnew11");
	}
	
}
