package DEM;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

public class MyPasswordField extends JPasswordField{
		MyPasswordField(final String str){
			super(str);
			this.setOpaque(false);
			this.getPassword();
			this.setFont(new Font(Font.MONOSPACED,Font.BOLD,14));
			this.setForeground(Color.WHITE);
			this.setBorder(BorderFactory.createEmptyBorder());
			this.addFocusListener(new FocusListener(){

				public void focusGained(FocusEvent e) {
					if(new String(getPassword()).equals(str)){
						setText("");
					}
				}

				public void focusLost(FocusEvent e) {
					if(new String(getPassword()).equals("")){
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

