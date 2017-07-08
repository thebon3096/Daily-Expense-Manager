package DEM;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.geom.*;
public class loadingAnimation {
	
	static MyFrame f;
	
	loadingAnimation() {
		f=new MyFrame();
		f.setSize(250,125);
		f.setUndecorated(true);
		f.add(new loadingAnimationPanel());
		f.setShape(new RoundRectangle2D.Double(0,0,250,125,20,20));
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		loadingAnimationPanel.t.setPriority(Thread.NORM_PRIORITY+1);
		loadingAnimationPanel.t.start();
		try{
			loadingAnimationPanel.t.join();
		}catch(InterruptedException e){}
		loadingAnimationPanel.t.interrupt();
		f.dispose();
	}
}

class loadingAnimationPanel extends JPanel implements Runnable{

	static Thread t;
	String path;
	loadingAnimationPanel(){
		this.setSize(300,150);
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		t= new Thread(this);
	}
	
	public void run(){
		try{
			for(int i=0;i<=25;i++){
				path=imagePaths(i%6);
				Thread.sleep(200);
				repaint();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	static public void start(){
		t.start();
	}
	
	private String imagePaths(int i){
		switch(i){
		case 0: return "/images/loggingin1.png";
		case 1:	return "/images/loggingin2.png";
		case 2: return "/images/loggingin3.png";
		case 3: return "/images/loggingin4.png";
		case 4: return "/images/loggingin5.png";
		case 5: return "/images/loggingin6.png";
		}
		 return "/images/BlurGround.jpg";
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		Graphics2D g2d= (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		try{
			g2d.drawImage(ImageIO.read(getClass().getResource(path)), 0, 0, getWidth(),getHeight(),this);
		}catch(IOException e){
			e.printStackTrace();
		}
	
	}
}