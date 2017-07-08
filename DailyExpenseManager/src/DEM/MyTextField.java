package DEM;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MyTextField extends JTextField {
	
	MyTextField(final String str){
		super(str);
//		this.getText();
		this.setOpaque(false);
		this.setFont(new Font(Font.MONOSPACED,Font.BOLD,14));
		this.setForeground(Color.WHITE);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				if(getText().equals(str)){
					setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if(getText().equals("")){
					setText(str);
				}
			}
			
		});
	}
	
	protected void paintComponent(Graphics paramGraphics){
		super.paintComponent(paramGraphics);
		Graphics2D g= (Graphics2D)paramGraphics;
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setColor(Color.WHITE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(1.0f));
		g.drawLine(0, getHeight()-5, getWidth(), getHeight()-5);
	}
}
