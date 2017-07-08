package DEM;

import javax.swing.*;
import java.awt.event.*;

public class MyFrame extends JFrame {
	private int x;
	private int y;
	MyFrame(){
		this.x=getX();
		this.y=getY();
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
//		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				x=e.getX();
				y=e.getY();
			}
		});
		
		this.addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen()-x,e.getYOnScreen()-y);
			}
		});
	}
}