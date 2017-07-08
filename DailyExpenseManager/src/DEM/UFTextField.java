package DEM;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

public class UFTextField  extends JTextField{
		private Color color;
	
		UFTextField(final String str,Color c){
			super(str);
			this.getText();
			this.color=c;
			this.setOpaque(false);
			this.setFont(new Font("Ariel",Font.BOLD,18));
			this.setForeground(Color.WHITE);
			this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
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
			Graphics2D g= (Graphics2D)paramGraphics;
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setColor(color);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke(new BasicStroke(2.0f));
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
			super.paintComponent(paramGraphics);
		}
}
