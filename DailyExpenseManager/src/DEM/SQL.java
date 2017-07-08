package DEM;

import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

public class SQL {
	
	static final String JDBC_DRIVER= "com.mysql.jdbc.Driver";
	static final String DB_URL="jdbc:mysql://localhost/DEM";
	static final String USER="bonny";
	static final String PASS="bonny";
	static final int USER_EXIST=1;
	static final int NEW_USER=2;
	ResultSet rs;
	ResultSetMetaData rsmd;
	
	Connection conn=null;
	static Statement stmt=null;
	
	double totalAmount=0;
	
	SQL(){
		try{
			createConnection();
			try{
				stmt.execute("CREATE TABLE users (username VARCHAR(20), password VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin, name VARCHAR(255) )");
			}catch(SQLException se){}
		}catch(SQLException se){
			JOptionPane.showMessageDialog(null, "Could not Connect to the Server! Try after sometime or Restart the app!","Database Connection error", JOptionPane.ERROR_MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void createConnection() throws SQLException, ClassNotFoundException{
		Connection conn= DriverManager.getConnection(DB_URL,USER,PASS);
		Class.forName(JDBC_DRIVER);
		stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	}
	
	void sqlClose(){
		try{
			conn.close();
			stmt.close();
		}catch(SQLException se){
			se.printStackTrace();
		}
	}
	
	boolean isFound(String username) throws SQLException{
		rs=stmt.executeQuery("SELECT username,password FROM users WHERE username='"+username+"'");
		if(rs.next())
			return true;
		return false;
	}
	
	boolean isFound(String username,char[] password) throws SQLException{
		rs=stmt.executeQuery("SELECT username,password FROM users WHERE username='"+username+"' AND password='"+new String(password)+"'");
		if(rs.next())
			return true;
		return false;
	}
	
	boolean addUser(String username,char[] password, String name) throws SQLException{
		int status;
		try{
			status=stmt.executeUpdate("INSERT INTO users (`username`,`password`,`name`) values ('"+username+"','"+new String(password)+"','"+name+"')");
			if(status==0){
				JOptionPane.showMessageDialog(null, "Couldn't Add User "+username);
				return false;
			}
			else{
				JOptionPane.showMessageDialog(null, "Congrats! You're Part of Daily Expense Manager!");
				try{
					stmt.execute("CREATE TABLE "+username+" ( id INT AUTO_INCREMENT, date DATE, type VARCHAR(7), item VARCHAR(255), amount DECIMAL(12,2), category VARCHAR(20), PRIMARY KEY(id) )");
				}catch(SQLException se){}
			}
		}catch(SQLException se){
			se.printStackTrace();
		}
		return true;
	}
	
	JTable dailyTable(UserFrame f, String user, Object typeOfTrans) throws SQLException{
		try{
			rs=stmt.executeQuery("SELECT id as Id, date as DATE, item as Description, amount as Amount, category as Category FROM "+user+" WHERE type='"+typeOfTrans.toString()+"'");
			totalAmount=getTotalAmount(rs);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return new JTable(buildTableModel(f, user, rs)){
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				if(arg1 == 0)	return false;
				return true;
			}
		};
	}
	
	JTable monthlyTable(UserFrame f, String user, Object typeOfTrans, String month, String year) throws SQLException{
		try{
			rs=stmt.executeQuery("SELECT id as Id, date as DATE, item as Description, amount as Amount, category as Category FROM "+user+" WHERE type='"+typeOfTrans.toString()+"' AND MONTH(date)='"+calenderDigit(month.toUpperCase())+"' AND YEAR(date)='"+year+"'");
			totalAmount=getTotalAmount(rs);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return new JTable(buildTableModel(f, user, rs)){
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				if(arg1 == 0)	return false;
				return true;
			}
		};
	}
	
	JTable yearlyTable(UserFrame f, String user, Object typeOfTrans, String year) throws SQLException{
		try{
			rs=stmt.executeQuery("SELECT id as Id, date as DATE, item as Description, amount as Amount, category as Category FROM "+user+" WHERE type='"+typeOfTrans+"' AND YEAR(date)='"+year+"'");
			totalAmount=getTotalAmount(rs);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return new JTable(buildTableModel(f, user, rs)){
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				if(arg1 == 0)	return false;
				return true;
			}
		};
	}
	
	JTable categoryTable(UserFrame f, String user, Object typeOfTrans, String category) throws SQLException{
		try{
			rs=stmt.executeQuery("SELECT id as Id, date as DATE, item as Description, amount as Amount, category as Category FROM "+user+" WHERE type='"+typeOfTrans+"' AND category='"+category+"'");
			totalAmount=getTotalAmount(rs);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return new JTable(buildTableModel(f, user, rs)){
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				if(arg1 == 0)	return false;
				return true;
			}
		};
	}
	
	private DefaultTableModel buildTableModel(final UserFrame f, final String user, ResultSet rs) throws SQLException{
		rsmd=rs.getMetaData();
		int columnCount=rsmd.getColumnCount();
		Vector<String> columnLabel=new Vector<String>();
		
		for(int i=1;i<=columnCount;i++){
			columnLabel.add(rsmd.getColumnLabel(i));
		}
		columnLabel.add("Delete?");
		
		Vector<Vector<Object>> data= new Vector<Vector<Object>>();
		while(rs.next()){
			Vector<Object> row= new Vector<Object>();
			for(int i=1;i<=columnCount;i++){
				row.add(rs.getObject(i));
			}
			row.add(new Boolean(false));
			data.add(row);
		}
		
		final DefaultTableModel md = new DefaultTableModel(data,columnLabel){
			@Override
			public Class<?> getColumnClass(int arg0) {
				if(arg0 == 5)	return Boolean.class;
				return String.class;
			}
		};
		
		md.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getColumn() == 5 && (Boolean)md.getValueAt(e.getFirstRow(), e.getColumn())){
					int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the record?");
					if(option == JOptionPane.YES_OPTION){
						try {
							int id = (int) md.getValueAt(e.getFirstRow(), 0);
							totalAmount = totalAmount - Double.parseDouble(md.getValueAt(e.getFirstRow(), 3).toString());
							stmt.execute("DELETE FROM "+user+" WHERE id = "+id+"");
							md.removeRow(e.getFirstRow());
							
						} catch (NullPointerException | NumberFormatException | SQLException e1) {
							e1.printStackTrace();
						}
					}else{
						md.setValueAt(new Boolean(false), e.getFirstRow(), e.getColumn());
					}
				}else{
					if(e.getType() != TableModelEvent.DELETE){
						Object data[] = new Object[5];
						for(int i=0; i<5; ++i){
							data[i] = (Object)md.getValueAt(e.getLastRow(), i);
						}
						try {
							ResultSet rs = stmt.executeQuery("SELECT amount FROM "+user+" WHERE id='"+data[0]+"'");
							rs.next();
							totalAmount = totalAmount - rs.getDouble("amount") + Double.parseDouble(md.getValueAt(e.getFirstRow(), 3).toString());
						} catch (NumberFormatException | SQLException e1) {
							e1.printStackTrace();
						}
						updateAt(user, data);
					}
				}
				f.updateTotalAmount();
			}
		});
		return md;
	}
	
	void updateAt(String user, Object[] data){
		try {
			stmt.execute("UPDATE "+ user +" SET date = '"+data[1].toString()+"', item = '"+data[2].toString()+"', amount = '"+Float.parseFloat(data[3].toString())+"', category = '"+data[4]+"' WHERE id = '"+data[0]+"'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addRecord(String user, String description, Object typeOfTrans, double amount, String category, java.sql.Date date){
		try{
			if(typeOfTrans.equals("Expense")){
				stmt.executeUpdate("INSERT INTO "+user+" (`date`, `type`, `item`, `amount`, `category`) VALUES ('"+date+"', '"+typeOfTrans+"', '"+description+"', '"+amount+"', '"+category+"') ");
			}
			else{
				stmt.executeUpdate("INSERT INTO "+user+" (`date`, `type`, `item`, `amount`) VALUES ('"+date+"', '"+typeOfTrans+"', '"+description+"', '"+amount+"') ");
			}
			JOptionPane.showMessageDialog(null, "Added");
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null, "Please Enter Data Properly");
			e.printStackTrace();
		}
	}
	
	private double getTotalAmount(ResultSet rs) throws SQLException{
		double sum=0;
		while(rs.next()){
			sum+=rs.getDouble("amount");
		}
		rs.beforeFirst();
		return sum;
	}
	
	private int calenderDigit(String month){
		switch(month){
		case "JANUARY":
			return Calendar.JANUARY+1;
		case "FEBRUARY":
			return Calendar.FEBRUARY+1;
		case "MARCH":
			return Calendar.MARCH+1;
		case "APRIL":
			return Calendar.APRIL+1;
		case "MAY":
			return Calendar.MAY+1;
		case "JUNE":
			return Calendar.JUNE+1;
		case "JULY":
			return Calendar.JULY+1;
		case "AUGUST":
			return Calendar.AUGUST+1;
		case "SEPTEMBER":
			return Calendar.SEPTEMBER+1;
		case "OCTOBER":
			return Calendar.OCTOBER+1;
		case "NOVEMBER":
			return Calendar.NOVEMBER+1;
		case "DECEMBER":
			return Calendar.DECEMBER+1;
		}
		return 1;
	}
	
	public double thisMonthTotal(String user,String s){
		try{
			rs=stmt.executeQuery("SELECT amount,date FROM "+user+" WHERE type='"+s+"' AND MONTH(date)='"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"'");
			return getTotalAmount(rs);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public void changePassword(String user,char[] password){
		int Status;
		try{
			Status=stmt.executeUpdate("UPDATE users SET password='"+new String(password)+"' WHERE username='"+user+"' ");
			if(Status>0){
				JOptionPane.showMessageDialog(null, "Password Changed!");
			}
			else JOptionPane.showMessageDialog(null, "Couldn't Change Password!");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean oldPassword(String user,char[] oldPassword){
		try{
			rs=stmt.executeQuery("SELECT * FROM users WHERE username='"+user+"' AND password='"+new String(oldPassword)+"'");
			if(rs.next())
				return true;
			else return false;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public String getName(String user){
		try{
			rs=stmt.executeQuery("SELECT name FROM users WHERE username='"+user+"'");
			rs.next();
			return rs.getString(1);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return "";
	}
	
	public ResultSet getTotalAmountOfCategory(String user){
		try {
			ResultSet rs = stmt.executeQuery("SELECT category, amount FROM "+user+" GROUP BY category");
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
