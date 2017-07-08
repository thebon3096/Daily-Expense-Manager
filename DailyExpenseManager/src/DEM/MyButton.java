package DEM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MyButton extends JButton {
	private Image image;
	private ImageIcon icon;
	MyButton(){
		super();
		setContentAreaFilled(true);
		setOpaque(false);
		repaint();
	}
	
	MyButton(String str){
		super(str);
		setContentAreaFilled(true);
		setOpaque(false);
		repaint();
	}
	
	MyButton(final BufferedImage path,final BufferedImage hoverPath){
		icon= new ImageIcon(path);
		image= icon.getImage();
		setContentAreaFilled(false);
		setOpaque(false);
		this.setBorder(BorderFactory.createEmptyBorder());
		
		this.addMouseListener(new MouseAdapter(){

			public void mouseEntered(MouseEvent e) {
				icon= new ImageIcon(hoverPath);
				image= icon.getImage();
				repaint();
			}

			public void mouseExited(MouseEvent e) {
				icon= new ImageIcon(path);
				image= icon.getImage();
				repaint();
			}
		});
		repaint();
	}
	
	public String toString(){
		return this.getName();
	}
	
	protected void paintComponent(Graphics paramGraphics){	
		super.paintComponent(paramGraphics);
		Graphics2D g=(Graphics2D)paramGraphics;
		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(image, 0, 0, getWidth(),getHeight(),this);
	}
	
	protected void paintBorder(Graphics paramGraphics){
		super.paintBorder(paramGraphics);
		Graphics2D g=(Graphics2D)paramGraphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(255,255,255,0));
		g.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
	}
}
