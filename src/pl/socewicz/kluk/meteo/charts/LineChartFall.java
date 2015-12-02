package pl.socewicz.kluk.meteo.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.opencsv.CSVReader;

public class LineChartFall extends JFrame {
	private static final long serialVersionUID = -4619093118842552184L;
	int type = 0;
    public LineChartFall(final String title, int type) throws IOException, ParseException {
        super(title);
        this.type = type;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    private CategoryDataset createDataset() throws IOException, ParseException {
    	DateFormat format = new SimpleDateFormat("dd.MM");
    	
        final String series1 = "Pomiary";
        final String series2 = "Ekologia";
        final String series4 = "Meteo";

        final Map<Date, Double> ekoFall = getEkologiaFall();    
        final Map<Date, Double> actFall = getActualFall();
        final Map<Date, Double> metFall = getMeteoFall();
        
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for(Date x : actFall.keySet()){
        	if((actFall.get(x) != null && ekoFall.get(x) != null)  ){
	        	dataset.addValue(actFall.get(x), series1, format.format(x));
	        	dataset.addValue(ekoFall.get(x), series2, format.format(x));
	        	dataset.addValue(metFall.get(x), series4, format.format(x));
	        	
	        	System.out.print(String.format("B³¹d bezwzglêdny meteo dla opadu (%td.%tm.%tY): %f%n",x ,x, x, Math.abs(actFall.get(x) - metFall.get(x))));
	        	System.out.print(String.format("B³¹d bezwzglêdny ekologia dla opadu (%td.%tm.%tY): %f%n------------------------------------%n",x ,x, x, Math.abs(actFall.get(x) - ekoFall.get(x))));
        	}
        }
        
        return dataset;
    }

	private Map<Date, Double> getActualFall() throws IOException, NumberFormatException, ParseException {
		Map<Date, Double> temps = new TreeMap<Date, Double>();
		CSVReader reader = new CSVReader(new FileReader("pomiary.csv"));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String [] nextLine;
		
    	while ((nextLine = reader.readNext()) != null) {
    		temps.put(df.parse(nextLine[0]), Double.parseDouble(nextLine[4]));
        }
    	
    	reader.close();
    	
		return temps;
	}
	
	private Map<Date, Double> getMeteoFall() throws IOException, NumberFormatException, ParseException {
		Map<Date, Double> temps = new TreeMap<Date, Double>();
		CSVReader reader = new CSVReader(new FileReader("meteo.csv"));
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String [] nextLine;
		
    	while ((nextLine = reader.readNext()) != null) {
    		long timediff = TimeUnit.DAYS.convert(df.parse(nextLine[1]).getTime() - df.parse(nextLine[0]).getTime(), TimeUnit.MILLISECONDS);

    		switch(type){
	    		case 0:
	    			if(timediff <= 3)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6]));
	    			break;
	    		
	    		case 1:
	    			if(timediff >= 4 && timediff <= 7)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6]));
	    			break;
	    		
	    		case 2:
	    			if(timediff >= 8 && timediff <= 14)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6]));
	    			break;
	    		
				default:
					break;
			}
    		
        }
    	
    	reader.close();
    	
		return temps;
	}

	private Map<Date, Double> getEkologiaFall() throws IOException, NumberFormatException, ParseException {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		
		Map<Date, Double> temps = new TreeMap<Date, Double>();
    	String [] nextLine;
    	CSVReader reader = new CSVReader(new FileReader("ekologia.csv"));
    	
    	while ((nextLine = reader.readNext()) != null) {
    		long timediff = TimeUnit.DAYS.convert(df.parse(nextLine[1]).getTime() - df.parse(nextLine[0]).getTime(), TimeUnit.MILLISECONDS);

    		switch(type){
	    		case 0:
	    			if(timediff <= 3){
	    				if (nextLine[6].contains("Ã·"))
	    	    			temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6].split("Ã·")[1]));
	    	    		else
	    	    			temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6]));
	    			}
	    			break;
	    		
	    		case 1:
	    			if(timediff >= 4 && timediff <= 7){
	    				if (nextLine[6].contains("Ã·"))
	    	    			temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6].split("Ã·")[1]));
	    	    		else
	    	    			temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6]));
	    			}
	    			break;
	    		
	    		case 2:
	    			if(timediff >= 8 && timediff <= 14){
	    				if (nextLine[6].contains("Ã·"))
	    	    			temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6].split("Ã·")[1]));
	    	    		else
	    	    			temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[6]));
	    			}
	    			break;
	    		
				default:
					break;
			}
    		
    		
        }
    	
    	reader.close();
    	
		return temps;
	}
	
    private JFreeChart createChart(final CategoryDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart(
            "Porównanie danych - Opad", 
            "Dzieñ",             
            "Opad [mm/h]",              
            dataset,           
            PlotOrientation.VERTICAL,
            true,                  
            true,                    
            false                      
        );
        
        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesStroke(
            0, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {10.0f, 6.0f}, 0.0f
            )
        );
        renderer.setSeriesStroke(
            1, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {6.0f, 6.0f}, 0.0f
            )
        );
        renderer.setSeriesStroke(
            2, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {2.0f, 6.0f}, 0.0f
            )
        );
        
        return chart;
    }
}