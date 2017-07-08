package DEM;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import javax.swing.*;

public class StartScreen {
	
	static JFrame f;
	StartScreen(){
		f= new JFrame();
		f.setLayout(null);
		f.setSize(500,500);
		f.setUndecorated(true);
		f.setLocationRelativeTo(null);
		f.add(new random()).setBounds(0,0,f.getWidth(),f.getHeight());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.setVisible(true);
		random.start();
		try {
			random.t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		random.t.interrupt();
		f.dispose();
	}
}

class random extends JPanel implements Runnable{
	
	Random r= new Random();
	String Daily="Daily";
	String Expense="Expense";
	String Manager="Manager";
	JLabel dailyLabel=new JLabel();
	JLabel expenseLabel= new JLabel();
	JLabel managerLabel= new JLabel();
	
	static Thread t;
	
	random(){
		this.setLayout(null);
		this.setOpaque(true);
		dailyLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,40));
		expenseLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,40));
		managerLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,40));
		
		dailyLabel.setOpaque(false);
		expenseLabel.setOpaque(false);
		managerLabel.setOpaque(false);
		
		this.add(dailyLabel).setBounds(187, 170, 130, 45);
		this.add(expenseLabel).setBounds(165, 200, 200, 45);
		this.add(managerLabel).setBounds(165, 230, 200, 45);
	
		t=new Thread(this);
	}

	public void run(){
		for(int i=1;i<=25;i++){
			dailyLabel.setForeground(randomColor());
			dailyLabel.setText(randomString("Daily"));
	
			random.this.setBackground(BWcolor());
	
			expenseLabel.setForeground(randomColor());
			expenseLabel.setText(randomString("Expense"));
	
			managerLabel.setForeground(randomColor());
			managerLabel.setText(randomString("Manager"));
	
			repaint();
			try{
				Thread.sleep(10*i);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		random.this.setBackground(Color.WHITE);
		dailyLabel.setForeground(Color.BLACK);
		expenseLabel.setForeground(Color.BLACK);
		managerLabel.setForeground(Color.BLACK);
		dailyLabel.setText("Daily");
		expenseLabel.setText("Expense");
		managerLabel.setText("Manager");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	private Color randomColor(){
		switch(r.nextInt(9)){
		case 0:
			return Color.BLACK;
		case 1:
			return Color.BLUE;
		case 2:
			return Color.CYAN;
		case 3:
			return Color.ORANGE;
		case 4:
			return Color.GRAY;
		case 5:
			return Color.GREEN;
		case 6:
			return Color.MAGENTA;
		case 7:
			return Color.RED;
		case 8:
			return Color.WHITE;
		}
		return Color.BLACK;
	}
	
	private Color BWcolor(){
		switch(r.nextInt(3)){
		case 0:
			return new Color(123,110,101);
		case 1:
			return new Color(143,138,132);
		case 2:
			return new Color(171,167,158);
		}
		
		return Color.BLACK;
	}
	
	private String randomString(String s){
		char[] c=new char[s.length()];
		String random="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%&*";
		for(int i=0;i<c.length;i++){
			c[i]=random.charAt(r.nextInt(random.length()));
		}
		return new String(c);
	}
	
	static public void start(){
		t.start();
	}
}
