package Main;

import javax.swing.JFrame;
import javax.swing.JTextField;

import DB_table.*;

public class ChildMain extends JFrame{
	


	public ChildMain(String title) {
		super(title);
		
		compose();
		
		// 창설정. 창크기 고정
		setSize(1280, 800);
		setLocation(400, 250);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	private void compose() {
		
	}


	//test
	public static void main(String[] args) {
		new ChildMain("유아 관리 프로그램");
	}
}
