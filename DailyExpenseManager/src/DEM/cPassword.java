package DEM;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class cPassword {
	
	static MyFrame f;
	cPassword() {
		f= new MyFrame();
		f.setLayout(null);
		f.setSize(400,250);
		f.add(new popPassword());
		f.setLocationRelativeTo(null);
		f.setShape(new RoundRectangle2D.Double(0,0,400,250, 15, 15));
		f.setVisible(true);
	}

}

class popPassword extends JPanel{
	static SQL sql= new SQL();
	MyButton change;
	MyButton cancel;
	MyPasswordField oldPass;
	MyPasswordField newPass;
	MyPasswordField cNewPass;
	JLabel oldLabel;
	JLabel newLabel;
	JLabel cNewLabel;
	
	popPassword(){
		setLayout(null);
		setSize(400, 250);
		oldPass=new MyPasswordField("Old Password");
		newPass= new MyPasswordField("New Password");
		cNewPass= new MyPasswordField("Confirm New Password");
		oldLabel= new JLabel("Old Password :");
		newLabel= new JLabel("New Password :");
		cNewLabel= new JLabel("Confirm Password :");
		
		oldLabel.setFont(new Font(Font.DIALOG,Font.BOLD,18));
		oldLabel.setForeground(Color.WHITE);
		add(oldLabel).setBounds(20, 20, 150, 30);
		
		newLabel.setFont(new Font(Font.DIALOG,Font.BOLD,18));
		newLabel.setForeground(Color.WHITE);
		add(newLabel).setBounds(20, 70, 150, 30);
		
		cNewLabel.setFont(new Font(Font.DIALOG,Font.BOLD,16));
		cNewLabel.setForeground(Color.WHITE);
		add(cNewLabel).setBounds(20, 120, 160, 30);
		
		add(oldPass).setBounds(180, 20, 210, 30);
		add(newPass).setBounds(180, 70, 210, 30);
		add(cNewPass).setBounds(180, 120, 210, 30);
		try{
			change=new MyButton(ImageIO.read(getClass().getResource("/images/change [].png")),ImageIO.read(getClass().getResource("/images/changeHover [].png")));
			cancel=new MyButton(ImageIO.read(getClass().getResource("/images/cancel [].png")),ImageIO.read(getClass().getResource("/images/cancelHover [].png")));
		}catch(IOException e){}
		add(change).setBounds(30,getHeight()-40-10-10-10,getWidth()/2-10-10-10,40);
		add(cancel).setBounds(30+getWidth()/2,getHeight()-40-10-10-10,getWidth()/2-10-10-10-10-10-10,40);
		
		change.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(sql.oldPassword(UserFrame.username,oldPass.getPassword())){
					if(newPass.getPassword().length>=6){
						if(new String(newPass.getPassword()).equals(new String(cNewPass.getPassword())))
							sql.changePassword(UserFrame.username,newPass.getPassword());
						else
							JOptionPane.showMessageDialog(null, "Password & Confirm Password should Match!");
					}
					else 
						JOptionPane.showMessageDialog(null, "Password length should be greater than or equal to 6");
				}else
					JOptionPane.showMessageDialog(null, "Wrong Old Password!");
			}
		});
		
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				cPassword.f.dispose();
			}
		});
	}
	
	Image image;
	
	protected void paintComponent(Graphics paramGraphics){
		super.paintComponent(paramGraphics);
		Graphics2D g= (Graphics2D)paramGraphics;
		try{
			image= new ImageIcon(ImageIO.read(getClass().getResource("/images/BlurGround.jpg"))).getImage();
		}catch(IOException e){
			e.printStackTrace();
		}
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(image,0,0,getWidth(),getHeight(),this);
	}
}
