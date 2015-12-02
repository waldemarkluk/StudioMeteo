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

public class LineChartWind extends JFrame {
	private static final long serialVersionUID = -4619093118842552184L;
	int type = 0;
	
    public LineChartWind(final String title, int type) throws IOException, ParseException {
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
        final String series3 = "Pogodynka";

        final String series4 = "Meteo";

        final Map<Date, Double> ekoWind = getEkologiaWind();
        final Map<Date, Double> pogWind = getPogodynkaWind();    
        final Map<Date, Double> actWind = getActualWind();   
        final Map<Date, Double> metWind = getMeteoWind();
        
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Double summet = 0.0, sumeko = 0.0, sumpog = 0.0;
        int ile = 0;
        
        for(Date x : actWind.keySet()){
        	if(ekoWind.get(x) != null && pogWind.get(x) != null && actWind.get(x) != null){
	        	dataset.addValue(actWind.get(x), series1, format.format(x));
	        	dataset.addValue(ekoWind.get(x), series2, format.format(x));
	        	dataset.addValue(pogWind.get(x), series3, format.format(x));
	        	if(type == 0) dataset.addValue(metWind.get(x), series4, format.format(x));
	        	
	        	System.out.print(String.format("B³¹d bezwzglêdny ekologia dla wiatru (%td.%tm.%tY): %f%n",x , x, x, Math.abs(actWind.get(x) - ekoWind.get(x)))); sumeko += Math.abs(actWind.get(x) - ekoWind.get(x));
	        	System.out.print(String.format("B³¹d bezwzglêdny pogodynka dla wiatru (%td.%tm.%tY): %f%n",x , x, x, Math.abs(actWind.get(x) - pogWind.get(x)))); sumpog += Math.abs(actWind.get(x) - pogWind.get(x));
	        	if(type == 0) System.out.print(String.format("B³¹d bezwzglêdny meteo dla wiatru (%td.%tm.%tY): %f%n------------------------------------%n",x , x, x, Math.abs(actWind.get(x) - metWind.get(x)))); if(type == 0)summet += Math.abs(actWind.get(x) - metWind.get(x));
	        	
	        	ile++;
        	}
        }
        
        System.out.print(String.format("Œredni b³¹d bezwzglêdny dla ekologii: %f%n", (sumeko/(double)ile)));
        System.out.print(String.format("Œredni b³¹d bezwzglêdny dla pogodynki: %f%n", (sumpog/(double)ile)));
        if(type == 0) System.out.print(String.format("Œredni b³¹d bezwzglêdny dla meteo: %f%n", (summet/(double)ile)));
        
        return dataset;
    }
    
    private Map<Date, Double> getActualWind() throws NumberFormatException, IOException, ParseException {
    	Map<Date, Double> temps = new TreeMap<Date, Double>();
		CSVReader reader = new CSVReader(new FileReader("pomiary.csv"));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String [] nextLine;
		
    	while ((nextLine = reader.readNext()) != null) {
    		temps.put(df.parse(nextLine[0]), Double.parseDouble(nextLine[1])*3.6);
        }
    	reader.close();
    	
		return temps;
	}
    
    private Map<Date, Double> getMeteoWind() throws NumberFormatException, IOException, ParseException {
    	Map<Date, Double> temps = new TreeMap<Date, Double>();
		CSVReader reader = new CSVReader(new FileReader("meteo.csv"));
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String [] nextLine;
		
    	while ((nextLine = reader.readNext()) != null) {
    		long timediff = TimeUnit.DAYS.convert(df.parse(nextLine[1]).getTime() - df.parse(nextLine[0]).getTime(), TimeUnit.MILLISECONDS);

    		switch(type){
	    		case 0:
	    			if(timediff <= 3)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[5]));
	    			break;
	    		
	    		case 1:
	    			if(timediff >= 4 && timediff <= 7)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[5]));
	    			break;
	    		
	    		case 2:
	    			if(timediff >= 8 && timediff <= 14)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[5]));
	    			break;
	    		
				default:
					break;
			}
        }
    	
    	reader.close();
    	
		return temps;
	}

	private Map<Date, Double> getPogodynkaWind() throws NumberFormatException, IOException, ParseException {
    	DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    	Map<Date, Double> temps = new TreeMap<Date, Double>();
    	String[] nextLine;
    	CSVReader reader = new CSVReader(new FileReader("pogodynka.csv"));
    	nextLine = reader.readNext();
    	Double currWind = Double.parseDouble(nextLine[5]);
    	String date = nextLine[1];
    	Date thisdate = df.parse(nextLine[0]);
    	
    	while ((nextLine = reader.readNext()) != null) {
			if(nextLine[1].equals(date)){
    			if(Double.parseDouble(nextLine[5]) > currWind){
    				currWind = Double.parseDouble(nextLine[5]);
    				thisdate = df.parse(nextLine[0]);	
    			}
			}
			else{
				long timediff = TimeUnit.DAYS.convert(df.parse(date).getTime() - thisdate.getTime(), TimeUnit.MILLISECONDS);

	    		switch(type){
		    		case 0:
		    			if(timediff <= 3)
		    				temps.put(df.parse(date), currWind*3.6);
		    			break;
		    		
		    		case 1:
		    			if(timediff >= 4 && timediff <= 7)
		    				temps.put(df.parse(date), currWind*3.6);
		    			break;
		    		
		    		case 2:
		    			if(timediff >= 8 && timediff <= 14)
		    				temps.put(df.parse(date), currWind*3.6);
		    			break;
		    		
					default:
						break;
				}
				
				
				currWind = Double.parseDouble(nextLine[5]);
				date = nextLine[1];
			}
        }
    	
    	reader.close();
		
    	return temps;
	}

	private Map<Date, Double> getEkologiaWind() throws IOException, NumberFormatException, ParseException {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		
		Map<Date, Double> temps = new TreeMap<Date, Double>();
    	String [] nextLine;
    	CSVReader reader = new CSVReader(new FileReader("ekologia.csv"));
    	
    	while ((nextLine = reader.readNext()) != null) {
    		long timediff = TimeUnit.DAYS.convert(df.parse(nextLine[1]).getTime() - df.parse(nextLine[0]).getTime(), TimeUnit.MILLISECONDS);

    		switch(type){
	    		case 0:
	    			if(timediff <= 3)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[5]));
	    			break;
	    		
	    		case 1:
	    			if(timediff >= 4 && timediff <= 7)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[5]));
	    			break;
	    		
	    		case 2:
	    			if(timediff >= 8 && timediff <= 14)
	    				temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[5]));
	    			break;
	    		
				default:
					break;
			}
    		
        }
    	reader.close();
    	
		return temps;
	}
	
    private JFreeChart createChart(final CategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createLineChart(
            "Porównanie danych - Wiatr", 
            "Dzieñ",             
            "Wiatr [km/h]",              
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