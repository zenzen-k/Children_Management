package Home;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import DB_table.AttendManageBean;
import DB_table.AttendManageDao;
import DB_table.JoinBean;
import DB_table.JoinDao;
import DB_table.PersonBean;
import DB_table.PersonDao;
import Login.LoginMain;

public class AttendManage extends JFrame implements ActionListener, ItemListener {

	// 기본 화면구성 관련 변수
	private JScrollPane scrollPane = null;
	private ArrayList<JoinBean> jlists = null;
	private String[] columnName = { "NO", "유아번호", "이름", "생년월일", "출석", "조퇴" };
	private Object[][] rowData;
	private JTable table = null;
	Date now = new Date();
	private JTextField txtYear = new JTextField();
	private Choice chMonth = new Choice();
	private Choice chDate = new Choice();
	JCheckBox check = new JCheckBox();
	JCheckBox check2 = new JCheckBox();
	
	private JTextField txtAtNum = new JTextField();
	private JTextField txtAbNum = new JTextField();

	private JButton btnLogout, btnExitPro, btnSave, btnSelect;
	JLabel lbHome, lbCheck;
	String[] teacher = new String[3];
	private String loginInfo;
	private JoinDao jdao = null;
	private AttendManageDao adao = null;

	private Font font = new Font("나눔스퀘어 네오 Regular", Font.PLAIN, 13);
	private Font fontBold = new Font("나눔스퀘어 네오 Regular", Font.BOLD, 13);
	private int row; // 선택 행번호

	/* 생성자 */
	public AttendManage(String title, String loginInfo) {
		super(title);
		this.loginInfo = loginInfo;

		JOptionPane.showMessageDialog(this, "출결관리는 담당 교실만 가능합니다","",JOptionPane.INFORMATION_MESSAGE);

		SimpleDateFormat date = new SimpleDateFormat("YYYY-MM-dd");
		String nowDate = date.format(now);

		jdao = new JoinDao();
		teacher = getLbTeacher();
		adao = new AttendManageDao();

		//날짜가 있는지 검색함. 없으면 전체학생 날짜데이터만 추가
		int result = adao.selectAttend(nowDate);
		if(result == 0) {
			ArrayList<AttendManageBean> lists = new ArrayList<AttendManageBean>();
			PersonDao pdao = new PersonDao();
			
			ArrayList<PersonBean> plist = pdao.getPersonNum(teacher[1]);
			
			for(int i = 0; i<plist.size(); i++) {
				AttendManageBean ab = new AttendManageBean();
				ab.setP_no(plist.get(i).getP_no());
				ab.setAdate(nowDate);
				lists.add(ab);
			}
			adao.insertAdate(lists);
		}
		

		jlists = jdao.getAllAttend(nowDate, teacher[1]);
		rowData = new Object[jlists.size()][columnName.length]; // 테이블크기설정

		// 가져온 데이터 넣기
		dataInput();
		table = new JTable(rowData, columnName); // 테이블에 값넣기
		scrollPane = new JScrollPane(table);

		setColumn();

		compose();

		// 창설정. 창크기 고정
		setSize(1280, 800);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	} // 생성자

	private void setColumn() {
		// 출석 컬럼에 셀렌디러를 설정해준다! 
		table.getColumn("출석").setCellRenderer(dcr);
		table.getColumn("조퇴").setCellRenderer(dcr2);

		// 체크박스클릭을 위한 에디터를 설정해주어야한당.
		check.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn("출석").setCellEditor(new DefaultCellEditor(check));
		check2.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn("조퇴").setCellEditor(new DefaultCellEditor(check2));
		check.addActionListener(this);
		check2.addActionListener(this);

	}

	/* 기본 화면 구상 */
	private void compose() {
		// 작업영역설정
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		scrollPane.setBounds(50, 200, 560, 510); // 스크롤바 크기
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

		btnLogout.setBounds(1060, (pnMenuBar.getHeight() - 20) / 2, 90, 25);
		btnExitPro.setBounds(1160, (pnMenuBar.getHeight() - 20) / 2, 90, 25);
		pnMenuBar.add(btnLogout);

		pnMenuBar.add(btnExitPro);

		// 라벨에 사용자 정보를 담음
		teacher = getLbTeacher();
		JLabel lbUser = new JLabel("Login " + teacher[0] + "님 반갑습니다! - " + teacher[1] + " / " + teacher[2]);
		lbUser.setBounds((pnMenuBar.getWidth() - 300) / 2, (pnMenuBar.getHeight() - 20) / 2, 300, 20);
		pnMenuBar.add(lbUser);

		// 상단관리라벨
		lbHome = new JLabel("HOME");
		lbCheck = new JLabel("출결관리");
		lbHome.setBounds(30, (pnMenuBar.getHeight() - 20) / 2, 80, 25);
		lbCheck.setBounds(120, (pnMenuBar.getHeight() - 20) / 2, 80, 25);
		lbHome.setForeground(Color.GRAY); // 글자색변경
		pnMenuBar.add(lbHome);
		pnMenuBar.add(lbCheck);


		// 날짜선택
		JButton btnDate = new JButton("날짜선택");
		btnDate.setBackground(Color.darkGray);
		btnDate.setForeground(Color.white);
		btnDate.setBounds(50, 160, 100, 25);
		contentPane.add(btnDate);
		btnDate.setEnabled(false); 

		JPanel pnDate = new JPanel();
		pnDate.setBounds(170, 160, 300, 25);
		pnDate.setLayout(new GridLayout());
		contentPane.add(pnDate);

		JLabel lbYear = new JLabel(" 년");
		JLabel lbMonth = new JLabel(" 월");
		JLabel lbDate = new JLabel(" 일");

		txtYear.setText(String.valueOf(now.getYear()+1900));
		chMonth.addItemListener(this);
		chDate.addItemListener(this);
		setChoice();

		pnDate.add(txtYear);
		pnDate.add(lbYear);
		pnDate.add(chMonth);
		pnDate.add(lbMonth);
		pnDate.add(chDate);
		pnDate.add(lbDate);

		//btnSave
		btnSave = new JButton("저장");
		btnSave.setBounds(550, 160, 60, 25);
		contentPane.add(btnSave);
		btnSave.addActionListener(this);

		//btnSelect
		btnSelect = new JButton("조회");
		btnSelect.setBounds(480, 160, 60, 25);
		contentPane.add(btnSelect);
		btnSelect.addActionListener(this);

		// 당일 전체 출석일자 조회
		LineBorder lB = new LineBorder(Color.gray, 1, true);
		JPanel pnAllAttend = new JPanel();
		pnAllAttend.setBounds(50, 70, 250, 70);
		pnAllAttend.setLayout(new GridLayout(2,2));
		contentPane.add(pnAllAttend);
//		pnAllAttend.setBackground(Color.pink);
		pnAllAttend.setBorder(lB);
		
		JLabel lballAt = new JLabel("  총 출석 인원 : ");
		JLabel lballAb = new JLabel("  총 결석 인원 : ");
		
		getAttendNum();
		txtAtNum.setEditable(false);
		txtAbNum.setEditable(false);
		
		pnAllAttend.add(lballAt);
		pnAllAttend.add(txtAtNum);
		pnAllAttend.add(lballAb);
		pnAllAttend.add(txtAbNum);
		
		// 이벤트관리
		lbHome.addMouseListener(new MouseHandler());
		btnLogout.addActionListener(this);
		btnExitPro.addActionListener(this);
		btnSelect.addActionListener(this);

		// 폰트관리
		lbUser.setFont(fontBold);
		lbYear.setFont(font);
		lbMonth.setFont(font);
		lbDate.setFont(font);
		lbHome.setFont(fontBold);
		lbCheck.setFont(fontBold);
		btnLogout.setFont(font);
		btnExitPro.setFont(font);
		btnSelect.setFont(font);
		btnSave.setFont(font);
		txtYear.setFont(font);
		chMonth.setFont(font);
		chDate.setFont(font);
		btnDate.setFont(font);
		lballAt.setFont(font);
		lballAb.setFont(font);
		txtAtNum.setFont(font);
		txtAbNum.setFont(font);

	} // compose

	// 테이블에 데이터 불러오기
	private void dataInput() {
		Object[] arr = jlists.toArray();
		boolean attend;
		int j = 0, x = 1; // x 순번
		for (int i = 0; i < arr.length; i++) {
			JoinBean jb = (JoinBean) arr[i];
			rowData[i][j++] = x++;
			rowData[i][j++] = jb.getP_no();
			rowData[i][j++] = jb.getP_name();
			rowData[i][j++] = jb.getP_birth();
			if(jb.getAttend() == 0) {
				attend = false;
			} else {
				attend = true;
			}
			rowData[i][j++] = attend;
			if(jb.getEarlier() == 0) {
				attend = false;
			} else {
				attend = true;
			}
			rowData[i][j++] = attend;
			j = 0;
		}
	}

	// 초이스박스에 데이터저장하기
	private void setChoice() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);

		int i = 0;
		for (i = 1; i <= 12; i++) {
			chMonth.add(String.valueOf(i));
		}
		chMonth.select(month - 1);
		if (txtYear.getText().length() != 0) {

			Date d = new Date(year, month, 0);

			for (i = 1; i <= d.getDate(); i++) {
				chDate.add(String.valueOf(i));
			}
		}
		chDate.select(day - 1);
	} //setChoice

	/* 사용자(로그인) 정보 가져오는 메서드 */
	private String[] getLbTeacher() {
		String[] lists = jdao.getLbTeacher(loginInfo); // 교사명, 클래스명, 직급명
		return lists;
	} // getLbTeacher
	
	// 인원수 가져오는 SQL문
	private void getAttendNum() {
		int[] atNum = new int[2];
		String adate = txtYear.getText() +"-" + chMonth.getSelectedItem()+"-" + chDate.getSelectedItem();
		atNum = adao.getAttendNum(teacher[1], adate);
		
		txtAtNum.setText(String.valueOf(atNum[1]) + " 명");
		txtAbNum.setText(String.valueOf(atNum[0]) + " 명");
	}


	//수정
	private void updateAttend() {
		ArrayList<AttendManageBean> lists = new ArrayList<AttendManageBean>();
		String adate = txtYear.getText() +"-" + chMonth.getSelectedItem()+"-" + chDate.getSelectedItem();

		// 해당 날짜 데이터가 있으면 update, 없으면 insert 해야함.
		adao = new AttendManageDao();
		int result = adao.selectAttend(adate);

		for(int i = 0; i<table.getRowCount(); i++) {
			AttendManageBean ab = new AttendManageBean();
			ab.setP_no((int)table.getValueAt(i, 1));
			if((boolean)table.getValueAt(i, 4)) {
				ab.setAttend(1);
				ab.setAbsence(0);
			}
			else if(!(boolean)table.getValueAt(i, 4)) {
				ab.setAttend(0);
				ab.setAbsence(1);
			}
			if((boolean)table.getValueAt(i, 5)) {
				ab.setEarlier(1);
			}
			else if(!(boolean)table.getValueAt(i, 5)) {
				ab.setEarlier(0);
			}
			ab.setAdate(adate);

			lists.add(ab);
		}

		if(result > 0) {
			int cnt = adao.updateAttend(lists);
			System.out.println("update : " + cnt);
		}
		else if(result == 0) {
			int cnt = adao.insertAttend(lists);
			System.out.println("insert : " + cnt);
		}

	} //updateAttend

	/* 액션 이벤트 처리 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj == btnLogout) { // 로그아웃
			System.out.println("로그아웃");
			int result = JOptionPane.showConfirmDialog // 확인하는 메세지. 확인 - 0 , 아니오 - 1, 취소 - 2 닫기 - -1 리턴
					(this, "로그아웃 하시겠습니까?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			System.out.println(result);
			if (result == 0) {
				dispose(); // 프레임창종료
				new LoginMain("유아 관리 프로그램 - Login");
			}
		} 
		else if (obj == btnExitPro) {
			System.out.println("종료");
			int result = JOptionPane.showConfirmDialog(this, "프로그램을 종료 하시겠습니까?", "Exit", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (result == 0) {
				System.exit(0);
			}
		} 
		//저장
		else if(obj == btnSave) {
			updateAttend();
		}
		//조호ㅚ
		else if(obj == btnSelect) {
			getAllAttend();
		}
	} // actionPerformed

	/* 마우스 이벤트 처리 */
	class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			System.out.println("mouse");
			Object obj = e.getSource();
			if (obj == lbHome) {
				new Home("유아 관리 프로그램 - Home", loginInfo);
				dispose();
			}
		}
	} // MouseHandler


	//선택
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();

		if(obj == chMonth) {
			chDate.removeAll();

			if (txtYear.getText().length() != 0) {
				int year = Integer.parseInt(txtYear.getText());
				int month = Integer.parseInt(chMonth.getItem(chMonth.getSelectedIndex()));
				Date d = new Date(year, month, 0);

				int i = 0;
				for (i = 1; i <= d.getDate(); i++) {
					chDate.add(String.valueOf(i));
				}
			}
		}
		if(obj == chDate) {
			System.out.println("chDate");
		}
	}//itemStateChanged



	//새로고침
	private void getAllAttend() {
		String adate = txtYear.getText() +"-" + chMonth.getSelectedItem()+"-" + chDate.getSelectedItem();
		adao = new AttendManageDao();

		//날짜가 있는지 검색함. 없으면 날짜데이터만 추가
		int result = adao.selectAttend(adate);
		if(result == 0) {
			System.out.println("Res " + result);
			ArrayList<AttendManageBean> lists = new ArrayList<AttendManageBean>();
			for(int i = 0; i<table.getRowCount(); i++) {
				AttendManageBean ab = new AttendManageBean();
				ab.setP_no((int)table.getValueAt(i, 1));
				ab.setAdate(adate);
				lists.add(ab);
			}
			adao.insertAdate(lists);
		}

		//그리고 값을 가져옴 기본값 - 0
		jlists = jdao.getAllAttend(adate, teacher[1]);
		rowData = new Object[jlists.size()][columnName.length]; // 테이블크기설정

		// 가져온 데이터 넣기
		dataInput();
		System.out.println(rowData[0][4]);
		table = new JTable(rowData, columnName); // 테이블에 값넣기
		scrollPane.setViewportView(table);
		setColumn();
		
		getAttendNum();
	}


	// 테이블 내의 체크박스 사용하기 위한 객체생성 
	// DefaultTableCellRenderer 를 상속받아 메서드를 오버라이딩해주어야 한당.
	// Component -> GUI 구축에 필요한 여러개의 하위 클래스를 가짐.
	DefaultTableCellRenderer dcr = new DefaultTableCellRenderer() {
		public Component getTableCellRendererComponent // 셀렌더러
		(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			check.setSelected(((Boolean)value).booleanValue());
			check.setHorizontalAlignment(JLabel.CENTER);
			return check;
		}
	};

	DefaultTableCellRenderer dcr2 = new DefaultTableCellRenderer() {
		public Component getTableCellRendererComponent // 셀렌더러
		(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			check2.setSelected(((Boolean)value).booleanValue());
			check2.setHorizontalAlignment(JLabel.CENTER);
			return check2;
		}
	};


	// test
	public static void main(String[] args) {
		new AttendManage("유아 관리 프로그램 - 출결관리", "jisang");
	}


}
