package DEM;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class mainProgram implements mainInterface {
	
	public static void main(String[] args) {
		
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		new StartScreen();
		
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				mainFrame.setVisible(true);
			}
		});
	}
}

interface mainInterface{
	static final loginFrame mainFrame=new loginFrame();
}
