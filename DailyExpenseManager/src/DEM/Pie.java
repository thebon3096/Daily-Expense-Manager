/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DEM;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
/**
 *
 * @author BhaVin Jobanputra
 */
public class Pie extends JFrame{
	static SQL sql;
	static String user;
	
    Pie(String user){
        super("Pie Chart");
        setSize(400,400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sql = new SQL();
        Pie.user = user;
        setContentPane(createDemoPanel());
        setVisible(true);
    }
    
    private static PieDataset createdataset(){
        
        try {
        	ResultSet rs = sql.getTotalAmountOfCategory(user);
            DefaultPieDataset ds = new DefaultPieDataset();
			while(rs.next()){
				ds.setValue(rs.getString(1), rs.getDouble(2));
			}
			return ds;
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    private static JFreeChart createChart( PieDataset dataset ) {
      JFreeChart chart = ChartFactory.createPieChart(      
         "Category wise Expense",   // chart title 
         dataset,          // data    
         true,             // include legend   
         true, 
         false);

      return chart;
   }
    
    public static JPanel createDemoPanel( ) {
      JFreeChart chart = createChart(createdataset()); 
      PiePlot plot = (PiePlot) chart.getPlot();
      plot.setSectionPaint(1, Color.decode("#F44336"));
      plot.setSectionPaint(2, Color.decode("#E91E63"));
      plot.setSectionPaint(3, Color.decode("#9C27B0"));
      plot.setSectionPaint(4, Color.decode("#673AB7"));
      plot.setSectionPaint(5, Color.decode("#3F51B5"));
      plot.setSectionPaint(6, Color.decode("#2196F3"));
      plot.setSectionPaint(7, Color.decode("#009688"));
      plot.setSectionPaint(8, Color.decode("#4CAF50"));
      return new ChartPanel( chart ); 
   }
    
    
}
