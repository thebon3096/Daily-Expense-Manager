package DEM;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Properties;

public class UserFrame extends MyFrame implements mainInterface{
	
	private MyButton history;
	private MyButton addExpense;
	private MyButton addIncome;
	private MyButton logOut;
	private MyButton dashBoard;
	private MyButton add;
	static private buttonPanel bPanel;

	private Dashboard dBoard= new Dashboard();
	private History hPanel= new History();
	private addExpense expPanel= new addExpense();
	private addIncome incPanel= new addIncome();
	
	static String username;
	
	private MyButton referenceButton;
	private JLabel totalLabel;
	private JLabel totalAmount;
	
	private String[] expenseCategories= {"Food","Travel","Bill Payment","Books/Stationary","Entertainment","Fuel","Personal Care","Health Care"};
	JComboBox<String> categoryCBox = new JComboBox<>(expenseCategories);
	
	private SQL sql= new SQL();
	
	UserFrame(){
		try{
			dashBoard= new MyButton(ImageIO.read(getClass().getResource("/images/DashBoard.png")),ImageIO.read(getClass().getResource("/images/DashBoardHover.png")));
			history=new MyButton(ImageIO.read(getClass().getResource("/images/History.png")),ImageIO.read(getClass().getResource("/images/HistoryHover.png")));
			addExpense= new MyButton(ImageIO.read(getClass().getResource("/images/AddExpense.png")),ImageIO.read(getClass().getResource("/images/AddExpenseHover.png")));
			addIncome= new MyButton(ImageIO.read(getClass().getResource("/images/AddIncome.png")),ImageIO.read(getClass().getResource("/images/AddIncomeHover.png")));
			logOut= new MyButton(ImageIO.read(getClass().getResource("/images/LogOut.png")),ImageIO.read(getClass().getResource("/images/LogOutHover.png")));
		}catch(IOException e){
			e.printStackTrace();
		}
	
		setLayout(null);
		setSize(600,400);
		setLocationRelativeTo(null);
		setResizable(false);
		bPanel= new buttonPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(bPanel).setLocation(0,0);
		loadPanel(UserFrame.this, dBoard);
		UserFrame.this.getRootPane().setBorder(BorderFactory.createEtchedBorder(new Color(200,0,0), new Color(200,0,0)));
		
		addExpense.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loadPanel(UserFrame.this, expPanel);
				UserFrame.this.getRootPane().setBorder(BorderFactory.createEtchedBorder(new Color(160,160,255), new Color(160,160,255)));
			}
		});
		
		history.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loadPanel(UserFrame.this, hPanel);
				UserFrame.this.getRootPane().setBorder(BorderFactory.createEtchedBorder(new Color(200,100,0), new Color(200,100,0)));
			}
		});
		
		addIncome.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loadPanel(UserFrame.this,incPanel);
				UserFrame.this.getRootPane().setBorder(BorderFactory.createEtchedBorder(new Color(0,200,100), new Color(0,200,100)));
			}
		});
		
		dashBoard.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loadPanel(UserFrame.this,dBoard);
				UserFrame.this.getRootPane().setBorder(BorderFactory.createEtchedBorder(new Color(200,0,0), new Color(200,0,0)));
			}
		});
		
		logOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SwingUtilities.getWindowAncestor(bPanel).setVisible(false);
//				loginFrame x=new loginFrame();
				mainFrame.setVisible(true);
			}
		});
	}
	
	void updateTotalAmount(){
		totalAmount.setText(Double.toString(sql.totalAmount));
	}
	
	void userName(String user){
		username=user;
		repaint();
	}
	
	protected void paintBorder(Graphics paramGraphics){
		super.paintComponents(paramGraphics);
		Graphics2D g=(Graphics2D)paramGraphics;
		g.setStroke(new BasicStroke(50.0f));
		g.setColor(new Color(0,0,0));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawRect(0, 0, getWidth(), getHeight());
	}
	
	//---------------------------------------------------------------DateLabelFormatter-------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------
	
	class DateLabelFormatter extends AbstractFormatter {

	    private String datePattern = "yyyy-MM-dd";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parseObject(text);
	    }

	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }

	        return "";
	    }
	}
	
	//---------------------------------------------------------------buttonPane(Panel)-------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------

	class buttonPanel extends JPanel{
		
		buttonPanel(){
			setLayout(null);
			setSize(120,400);
			add(dashBoard).setBounds(0,0,120,80);
			add(history).setBounds(0,80,120,80);
			add(addExpense).setBounds(0,160,120,80);
			add(addIncome).setBounds(0,240,120,80);
			add(logOut).setBounds(0,320,120,80);
		}
	}

	//----------------------------------------------------------------addExpense(Panel)---------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	class addExpense extends JPanel{
		private SqlDateModel model;
		private JDatePanelImpl datePanel;
		private JDatePickerImpl datePicker;
		private Properties p;
		private UFTextField itemField;
		private UFTextField eAmountField;
		private String[] expenseCategories= {"Food","Travel","Bill Payment","Books/Stationary","Entertainment","Fuel","Personal Care","Health Care"};
		private JComboBox<String> categoryCBox;
		
		addExpense(){
			setLayout(null);
			setSize(480,400);
			
			categoryCBox= new JComboBox<String>(expenseCategories);
			categoryCBox.setBackground(new Color(0,100,200));
			categoryCBox.setForeground(Color.WHITE);
			
			model = new SqlDateModel();
			
			p= new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");
			
			datePanel= new JDatePanelImpl(model, p);
			datePicker= new JDatePickerImpl(datePanel, new DateLabelFormatter());
			
			try{
				add=new MyButton(ImageIO.read(getClass().getResource("/images/AddExpense1.png")),ImageIO.read(getClass().getResource("/images/AddExpenseHover1.png")));
			}catch(IOException e){
				e.printStackTrace();
			}
			
			itemField= new UFTextField("Description",new Color(0,100,200));
			eAmountField = new UFTextField("Amount",new Color(0,100,200));
			
			this.add(itemField).setBounds(getWidth()-300,70,300,50);
			this.add(eAmountField).setBounds(getWidth()-300, 130, 300, 50);
			this.add(datePicker).setBounds(getWidth()-300, 230, 300, 30);
			this.add(add).setBounds(getWidth()-200, 270, 200, 50);
			this.add(categoryCBox).setBounds(getWidth()-300, 190, 300, 30);
			
			add.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					java.sql.Date selectedDate=(java.sql.Date)datePicker.getModel().getValue();
					try{
						
						sql.addRecord(username,itemField.getText(),"Expense",Double.parseDouble(eAmountField.getText()),categoryCBox.getSelectedItem().toString(),selectedDate);
					}catch(NumberFormatException e1){
						JOptionPane.showMessageDialog(null, "Enter Data Properly");
					}
				}
			});
			
			setVisible(true);
		}
		
		protected void paintComponent(Graphics paramGraphics){
			super.paintComponent(paramGraphics);
			Graphics2D g= (Graphics2D) paramGraphics;
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setColor(new Color(160,160,255,50));
			g.fillRect(0,0,getWidth(),getHeight());
		}
	}
	
	//--------------------------------------------------------------------History(Panel)-----------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	class History extends JPanel{
		
		private MyButton daily;
		private MyButton Monthly;
		private MyButton yearly;
		private MyButton categoryButton;
		private MyButton expense;
		private MyButton income;
		private MyButton back;
		private DMYCListener DMYCHandler= new DMYCListener(); 
		final int ADD=1;
		final int REMOVE=0;
		private EIBListener EIBHandler= new EIBListener();
		private String[] monthString= {"January","February","March","April","May","June","July","August","September","October","November","December"};
		private SpinnerListModel monthModel= new SpinnerListModel(monthString);
		private JSpinner months= new JSpinner(monthModel);
		private SpinnerDateModel yearModel= new SpinnerDateModel();
		private JSpinner years= new JSpinner(yearModel);
		private JSpinner.DateEditor de= new JSpinner.DateEditor(years,"yyyy");	
		private MyButton show;
		
		History(){
			setLayout(null);
			setSize(480,400);
			setVisible(true);
			try{
				daily= new MyButton(ImageIO.read(getClass().getResource("/images/DailyHover.png")),ImageIO.read(getClass().getResource("/images/Daily.png")));
				Monthly= new MyButton(ImageIO.read(getClass().getResource("/images/MonthlyHover.png")),ImageIO.read(getClass().getResource("/images/Monthly.png")));
				yearly= new MyButton(ImageIO.read(getClass().getResource("/images/YearlyHover.png")),ImageIO.read(getClass().getResource("/images/Yearly.png")));
				categoryButton= new MyButton(ImageIO.read(getClass().getResource("/images/CategoryHover.png")),ImageIO.read(getClass().getResource("/images/Category.png")));
				expense= new MyButton(ImageIO.read(getClass().getResource("/images/ExpenseHover.png")),ImageIO.read(getClass().getResource("/images/Expense.png")));
				income= new MyButton(ImageIO.read(getClass().getResource("/images/IncomeHover.png")),ImageIO.read(getClass().getResource("/images/Income.png")));
				back= new MyButton(ImageIO.read(getClass().getResource("/images/BackHover1.png")),ImageIO.read(getClass().getResource("/images/Back1.PNG")));
			}catch(IOException e){
				e.printStackTrace();
			}
			
			this.DMYCButtons(ADD);
			
			expense.setName("Expense");
			income.setName("Income");
			
			daily.addActionListener(DMYCHandler);
			Monthly.addActionListener(DMYCHandler);
			yearly.addActionListener(DMYCHandler);
			categoryButton.addActionListener(DMYCHandler);
			
			expense.addActionListener(EIBHandler);
			income.addActionListener(EIBHandler);
		}
		
		protected void paintComponent(Graphics paramGraphics){
			super.paintComponent(paramGraphics);
			Graphics2D g= (Graphics2D) paramGraphics;
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setColor(new Color(200,100,0,50));
			g.fillRect(0,0,getWidth(),getHeight());
		}
		
		//**********************************************************DYMCButtons(Method)**********************************************************
		
		void DMYCButtons(final int x){
			final int ADD=1;
			if(x==ADD){
				add(daily).setBounds(getWidth()-230, 60, getWidth()-250, 60);
				add(Monthly).setBounds(getWidth()-230,130,getWidth()-250,60);
				add(yearly).setBounds(getWidth()-230,200,getWidth()-250,60);
				add(categoryButton).setBounds(getWidth()-230,270,getWidth()-250,60);
			}
			else{
				this.remove(daily);
				this.remove(Monthly);
				this.remove(yearly);
				this.remove(categoryButton);
			}
		}
		
		//***********************************************************EIBButtons(Method)***********************************************************
		
		void EIBButtons(final int x){
			final int ADD=1;
			if(x==ADD){
				this.add(expense).setBounds(getWidth()-230,100,getWidth()-250,60);
				this.add(income).setBounds(getWidth()-230,170,getWidth()-250,60);
				this.add(back).setBounds(getWidth()-230,240,getWidth()-250,60);
			}
			else{
				this.remove(expense);
				this.remove(income);
				this.remove(back);
			}
		}
		
		//**********************************************************DMYCListener(ActionListener)*************************************************
		
		class DMYCListener implements ActionListener{
			
			public void actionPerformed(ActionEvent e){
				referenceButton=(MyButton) e.getSource();
				History.this.DMYCButtons(REMOVE);
				History.this.EIBButtons(ADD);
				if(referenceButton == categoryButton)
					History.this.remove(income);
				back.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						History.this.EIBButtons(REMOVE);
						History.this.DMYCButtons(ADD);
						referenceButton=null;
						revalidate();
						repaint();
					}
				});
				revalidate();
				repaint();
			}
		}	
		
		//**********************************************************EIBListener(ActionListener)*************************************************
		
		class EIBListener extends DMYCListener implements ActionListener{
			JFrame tableFrame;
			private String[] expenseCategories= {"Food","Travel","Bill Payment","Books/Stationary","Entertainment","Fuel","Personal Care","Health Care"};
			private JComboBox<String> categoryCBox=new JComboBox<String>(expenseCategories);
				
			public void actionPerformed(final ActionEvent e) {
					tableFrame= new JFrame();
					tableFrame.setSize(500, 500);
					
					final Table tablePanel=new Table();
					tableFrame.add(tablePanel).setBounds(0,0,tableFrame.getWidth(),tableFrame.getHeight());
					tableFrame.setLocationRelativeTo(null);
					years.setEditor(de);
					
					try{
						show=new MyButton(ImageIO.read(getClass().getResource("/images/Show.png")),ImageIO.read(getClass().getResource("/images/ShowHover.png")));
						if(referenceButton == daily){
							tablePanel.add(new JScrollPane(sql.dailyTable(UserFrame.this, username,e.getSource()))).setBounds(0,0,tablePanel.getWidth(),tablePanel.getHeight());
							updateTotalAmount();
							revalidate();
							repaint();
						}
						else if(referenceButton == categoryButton){
							tablePanel.add(show).setBounds(0,tablePanel.getHeight()-80,tablePanel.getWidth()/3,40);
							tablePanel.add(categoryCBox).setBounds(0,0,tablePanel.getWidth()-10,20);
							show.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent ae){
									try {
										tablePanel.add(new JScrollPane(sql.categoryTable(UserFrame.this, username,e.getSource(),categoryCBox.getSelectedItem().toString()))).setBounds(0,20,tablePanel.getWidth(),tablePanel.getHeight()-60);
										updateTotalAmount();
										revalidate();
										repaint();
									} catch (SQLException e1) {
										e1.printStackTrace();
									}
								}
							});
							new Pie(username);
						}
						else{
							tablePanel.add(show).setBounds(0,tablePanel.getHeight()-80,tablePanel.getWidth()/3,40);
							tablePanel.add(years).setBounds(tablePanel.getWidth()/2,0,tablePanel.getWidth()/2-10,20);
							if(referenceButton==Monthly){
								tablePanel.add(months).setBounds(0,0,tablePanel.getWidth()/2,20);
								show.addActionListener(new ActionListener(){
									public void actionPerformed(ActionEvent ae){
										try {
											tablePanel.add(new JScrollPane(sql.monthlyTable(UserFrame.this, username,e.getSource(),months.getValue().toString(),de.getFormat().format(years.getValue()).toString()))).setBounds(0,20,tablePanel.getWidth(),tablePanel.getHeight()-60);
											updateTotalAmount();
											revalidate();
											repaint();
										} catch (SQLException e1) {
											e1.printStackTrace();
										}
									}
								});
							}
							else if(referenceButton==yearly){
								show.addActionListener(new ActionListener(){
									public void actionPerformed(ActionEvent ae){
										try {
											tablePanel.add(new JScrollPane(sql.yearlyTable(UserFrame.this, username,e.getSource(),de.getFormat().format(years.getValue()).toString()))).setBounds(0,20,tablePanel.getWidth(),tablePanel.getHeight()-60);
											updateTotalAmount();
											revalidate();
											repaint();
										} catch (SQLException e1) {
											e1.printStackTrace();
										}
									}
								});
							}
						}
					}catch(SQLException | IOException se){
						se.printStackTrace();
					}
					tableFrame.setVisible(true);
				}
			}
	}
	
	//-------------------------------------------------------------Table(Panel)-------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------
	
	class Table extends JPanel{
		
		Table(){
			this.setLayout(null);
			this.setSize(500,500);
			totalLabel= new JLabel("Total");
			totalLabel.setBackground(Color.WHITE);
			totalAmount= new JLabel();
			totalAmount.setBackground(Color.WHITE);
			totalLabel.setVerticalAlignment(SwingConstants.CENTER);
			totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
			totalLabel.setFont(new Font(Font.SANS_SERIF,1,30));
			totalLabel.setBackground(Color.WHITE);
			totalAmount.setVerticalAlignment(SwingConstants.CENTER);
			totalAmount.setHorizontalAlignment(SwingConstants.CENTER);
			totalAmount.setFont(new Font(Font.MONOSPACED,Font.BOLD+Font.ITALIC,30));
			totalAmount.setText("0");
			this.add(totalLabel).setBounds(this.getWidth()/3,this.getWidth()-80,this.getWidth()/3,30);
			this.add(totalAmount).setBounds(2*this.getWidth()/3,this.getWidth()-80,this.getWidth()/3,30);
		}
		
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d= (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setColor(Color.BLACK);
			g2d.setBackground(Color.BLACK);
		}
	}
	
	//----------------------------------------------------------------Dashboard(Panel)------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------
	
	class Dashboard extends JPanel{
		
		private MyButton cPassword;
		
		Dashboard(){
			setLayout(null);
			setSize(480,400);
			setVisible(true);
			try{
				cPassword= new MyButton(ImageIO.read(getClass().getResource("/images/changePassword.png")),ImageIO.read(getClass().getResource("/images/changePasswordHover.png")));
			}catch(IOException e){}
			this.add(cPassword).setBounds(getWidth()-250, 315, 250, 50);;
			cPassword.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					new cPassword();
				}
			});
		}
		
		protected void paintComponent(Graphics paramGraphics){
			super.paintComponent(paramGraphics);
			Graphics2D g= (Graphics2D) paramGraphics;
			Double thisExpense=sql.thisMonthTotal(username, "Expense");
			Double thisIncome=sql.thisMonthTotal(username, "Income");
			double thisDifference=thisIncome-thisExpense;
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setColor(new Color(200,0,0,30));
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(new Color(200,0,0,225));
			g.fillRoundRect(getWidth()-250, 35, 250, 50, 15, 15);
			g.fillRoundRect(getWidth()-200, 105, 200, 50, 15, 15);
			g.fillRoundRect(getWidth()-200, 175, 200, 50, 15, 15);
			g.fillRoundRect(getWidth()-200, 245, 200, 50, 15, 15);
			g.setFont(new Font(Font.DIALOG,Font.BOLD,18));
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setColor(new Color(200,0,0,225));
			g.drawString("NAME:", 146, 68);
			g.setFont(new Font(Font.DIALOG,Font.BOLD,18));
			g.drawString("THIS MONTH'S EXPENSE:",50,137);
			g.drawString("THIS MONTH'S INCOME:", 62, 205);
			g.drawString("DIFFERENCE:", 153, 275);
			g.setColor(Color.WHITE);
			g.drawString(sql.getName(username).toUpperCase(), 250, 67);
			g.drawString(Double.toString(thisExpense), 290, 137);
			g.drawString(Double.toString(thisIncome), 290, 207);
			g.drawString(Double.toString(thisDifference), 290, 277);
		}
	}
	
	//----------------------------------------------------------------addIncome(Panel)-------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	class addIncome extends JPanel {
		
		private SqlDateModel model;
		private JDatePanelImpl datePanel;
		private JDatePickerImpl datePicker;
		private Properties p;
		private UFTextField incomeField;
		private UFTextField iAmountField;
		
		addIncome(){
			setLayout(null);
			setSize(480,400);
			
			model = new SqlDateModel();
			
			p= new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");
			
			datePanel= new JDatePanelImpl(model, p);
			datePicker= new JDatePickerImpl(datePanel, new DateLabelFormatter());
			
			try{
				add=new MyButton(ImageIO.read(getClass().getResource("/images/AddIncome1.png")),ImageIO.read(getClass().getResource("/images/AddIncomeHover1.png")));
			}catch(IOException e){
				e.printStackTrace();
			}
			
			incomeField= new UFTextField("Source",new Color(0,200,100));
			iAmountField = new UFTextField("Amount",new Color(0,200,100));
			
			this.add(incomeField).setBounds(getWidth()-300,70,300,50);
			this.add(iAmountField).setBounds(getWidth()-300, 130, 300, 50);
			this.add(datePicker).setBounds(getWidth()-300, 230, 300, 30);
			this.add(add).setBounds(getWidth()-200, 270, 200, 50);
			
			add.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					java.sql.Date selectedDate=(java.sql.Date)datePicker.getModel().getValue();
					try{
						sql.addRecord(username,incomeField.getText(),"Income",Double.parseDouble(iAmountField.getText()),null,selectedDate);
					}catch(NumberFormatException e1){
						JOptionPane.showMessageDialog(null, "Enter Data Properly");
					}
				}
			});
			setVisible(true);
		}
		
		protected void paintComponent(Graphics paramGraphics){
			super.paintComponent(paramGraphics);
			Graphics2D g= (Graphics2D) paramGraphics;
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setColor(new Color(0,200,100,50));
			g.fillRect(0,0,getWidth(),getHeight());
		}
	}	
	
	//----------------------------------------------------------loadPanel(Method)--------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------------------------
	
	void loadPanel(Window window,Component x){
		remove(bPanel);
		remove(dBoard);
		remove(hPanel);
		remove(expPanel);
		remove(incPanel);
		window.add(bPanel).setLocation(0,0);
		window.add(x).setLocation(120,0);
		window.revalidate();
		window.repaint();
	}
	
}
