package DEM;

import java.awt.*;
import java.sql.SQLException;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class loginFrame extends MyFrame {
	
	UserFrame f=new UserFrame();
	
	public loginFrame(){
		this.setSize(600,300);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setShape(new RoundRectangle2D.Double(0,0,600,300, 15, 15));
		this.add(new loginPanel());
	}
	
	class loginPanel extends JPanel {
		
		JLabel label=new JLabel();
		private MyButton signIn;
		private MyButton signUp;
		private MyButton back;
		private MyButton exit;
		private MyTextField name= new MyTextField("Name");
		private MyTextField username= new MyTextField("Username");
		private MyPasswordField password= new MyPasswordField("Password");
		private MyPasswordField confirmPassword= new MyPasswordField("Confirm Password");
		private MyFrame popFrame;
		private int countClick=1;
		private final SQL sql= new SQL();

		Image backgroundImage;
		
		public loginPanel(){
			try{
				backgroundImage= new ImageIcon(ImageIO.read(getClass().getResource("/images/banknote.jpg"))).getImage();
				signIn= new MyButton(ImageIO.read(getClass().getResource("/images/SignIn.png")),ImageIO.read(getClass().getResourceAsStream("/images/SignInHover.png")));
				signUp= new MyButton(ImageIO.read(getClass().getResourceAsStream("/images/SignUp.png")),ImageIO.read(getClass().getResourceAsStream("/images/SignUpHover.png")));
				back= new MyButton(ImageIO.read(getClass().getResourceAsStream("/images/Back.png")),ImageIO.read(getClass().getResourceAsStream("/images/BackHover.png")));
				exit=new MyButton(ImageIO.read(getClass().getResourceAsStream("/images/Exit.png")),ImageIO.read(getClass().getResourceAsStream("/images/ExitHover.png")));
			}catch(IOException e){}
			this.setLayout(null);
			this.setOpaque(false);
			this.setSize(600,300);
			this.label.setOpaque(false);
			this.label.setHorizontalAlignment(SwingConstants.CENTER);
			this.label.setForeground(Color.WHITE);
			this.label.setFont(new Font(Font.MONOSPACED,1,14));
			this.label.setText("");
			this.add(label).setBounds(0, getHeight()-70, getWidth(), 50);
			this.add(signIn).setBounds(140,135, 100, 32);;
			this.add(signUp).setBounds(getWidth()-250,135, 100, 32);;
			this.add(exit).setBounds(getWidth()-50, getHeight()-16, 50, 16);
			
			signIn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(countClick==1){
						try{
							if(popFrame.isVisible()){
								popFrame.setVisible(false);
							}
						}catch(NullPointerException e1){}
						removeAll();
						add(label);
						add(exit).setBounds(getWidth()-50, getHeight()-16, 50, 16);
						add(signIn).setBounds(140,135, 100, 32);
						add(new signIn());
						add(back).setBounds(150,170,75,25);
						revalidate();
						repaint();
						
						countClick++;
					}
					else{
						try {
							if(sql.isFound(username.getText().trim())){
								if(sql.isFound(username.getText().trim(),password.getPassword())==false)
									label.setText("Wrong Username/Password!");
								else{
									SwingUtilities.getWindowAncestor(signIn).setVisible(false);
									
//									final UserFrame f=new UserFrame(username.getText());
									f.userName(username.getText());
									
									EventQueue.invokeLater(new Runnable(){
										public void run(){
											f.setVisible(true);
										}
									});
								}
							}
							else{
								label.setText("User Not Found !");
							}
						} catch (SQLException e1) {
							label.setText("Database Error!");
						}
					}
				}
			});
			
			signUp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(countClick==1){
						remove(signIn);
						add(new signUp());
						add(back).setBounds(getWidth()-230,170,75,25);
						revalidate();
						repaint();
						countClick++;
					}
					else{
						try {
							if(hasWhitespace(username.getText().trim())){
								label.setText("Username can't contain any whitespaces!");
							}
							else if(username.getText().trim().length()<4 || username.getText().trim().length()>10 ){
								label.setText("Username length should be between 4 to 10");
							}
							else if(password.getPassword().length<6 || password.getPassword().length>20){
								label.setText("Password length should be between 6 to 20");
							}
							else if(new String(password.getPassword()).equals(new String(confirmPassword.getPassword()))){
								if(sql.isFound(username.getText().trim())==true){
									countClick=1;
									popFrame=new MyFrame();
									popFrame.setLayout(null);
									popFrame.setSize(300, 150);
									popFrame.add(new popPanel());
									popFrame.setShape(new RoundRectangle2D.Double(0,0,popFrame.getWidth(),popFrame.getHeight(),15,15));
									popFrame.setVisible(true);
									popFrame.setLocationRelativeTo(null);
								}
								else{
									sql.addUser(username.getText().trim(),password.getPassword(),name.getText());
								}
							}
							else{
								label.setText("Confirm Password field should match Password field!");
							}
						}catch(SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			
			back.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					revalidate();
					setContentPane(new loginPanel());
					repaint();
				}
			});
			
			exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.exit(0);
				}
			});
		}
		
		protected void paintComponent(Graphics paramGraphics){
			super.paintComponent(paramGraphics);
			Graphics2D g= (Graphics2D)paramGraphics;
			g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Tahoma",1,30));
			FontMetrics title=(FontMetrics)g.getFontMetrics();
			int titleWidth=title.stringWidth("Welcome");
			int xCoordinate=getWidth()/2-titleWidth/2;
			int yCoordinate=40;
			g.drawImage(backgroundImage,0, 0, getWidth(), getHeight(),this);
			g.setColor(new Color(0,0,0,100));
			g.fillRect(0,0,getWidth(),getHeight());
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setColor(Color.WHITE);
			g.drawString("Welcome", xCoordinate,yCoordinate);
			g.drawLine(getWidth()/2,70,getWidth()/2,getHeight()-70);
		}
		
		boolean hasWhitespace(String s){
			for(int i=0;i<s.length();i++){
				if(Character.isWhitespace(s.charAt(i)))
					return true;
			}
				return false;
		}
		
		//-------------------------------------------------------------------signIn-------------------------------------------------------------------

		class signIn extends JPanel {
			
			signIn(){
				this.setLayout(null);
				this.setOpaque(false);
				setSize(600,300);
				
				username.setFont(new Font(Font.MONOSPACED,1,14));
				
				this.add(username).setBounds(getWidth()/2+60, 110, 150, 30);
				this.add(password).setBounds(getWidth()/2+60, 160, 150, 30);
			}
		}
		
		//---------------------------------------------------------------------signUp--------------------------------------------------------------------
		
		class signUp extends JPanel{
			
			signUp(){
				setLayout(null);
				this.setOpaque(false);
				this.setSize(600,300);
				
				name.setBounds(getWidth()/2-240,50,180,30);
				username.setBounds(getWidth()/2-240,95,180,30);
				password.setBounds(getWidth()/2-240,145,180,30);
				confirmPassword.setBounds(getWidth()/2-240,195,180,30);
				
				name.setToolTipText("Enter Your Name Here!");
				username.setToolTipText("Username: Between 4 to 10 letters");
				password.setToolTipText("Password: Between 6 to 20");
				confirmPassword.setToolTipText("Should be As same as Password!");
				
				add(name);
				add(username);
				add(password);
				add(confirmPassword);
			}
		}
		
		//--------------------------------------------------------popPanel--------------------------------------------------------------------------
		
		class popPanel extends JPanel{
			popPanel(){
				setLayout(null);
				setSize(300, 150);
				add(signIn).setBounds(40,80,90,30);
				add(exit).setBounds(170,80,90,30);
			}
			
			Image image;
			
			protected void paintComponent(Graphics paramGraphics){
				super.paintComponent(paramGraphics);
				Graphics2D g= (Graphics2D)paramGraphics;
				try{
					image= new ImageIcon(ImageIO.read(getClass().getResource("/images/popFrameBackground.jpg"))).getImage();
				}catch(IOException e){
					e.printStackTrace();
				}
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g.setColor(Color.white);
				g.setFont(new Font(Font.SANS_SERIF,1,25));
				FontMetrics fm=g.getFontMetrics();
				int fmWidth=fm.stringWidth("User Exist!");
				int xCoordinate=getWidth()/2-fmWidth/2;
				int yCoordinate=50;
				g.drawImage(image,0,0,getWidth(),getHeight(),this);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g.drawString("User Exist!", xCoordinate, yCoordinate);
			}
		}
	}
}
