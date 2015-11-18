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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import com.opencsv.CSVReader;

public class LineChartPressure extends ApplicationFrame {
	private static final long serialVersionUID = -4619093118842552184L;

    public LineChartPressure(final String title) throws IOException, ParseException {
        super(title);
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    private CategoryDataset createDataset() throws IOException, ParseException {
    	DateFormat format = new SimpleDateFormat("dd.MM");
        //final String series1 = "Meteo";
        final String series2 = "Ekologia";
        final String series3 = "Pogodynka";

        final Map<Date, Double> ekoPress = getEkologiaPress();
        final Map<Date, Double> pogPress = getPogodynkaPress();        
        
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for(Date x : ekoPress.keySet()){
        	dataset.addValue(ekoPress.get(x), series2, format.format(x));
        }
        
        for(Date x : pogPress.keySet()){
        	dataset.addValue(pogPress.get(x), series3, format.format(x));
        }
        
        return dataset;
    }
    
    private Map<Date, Double> getPogodynkaPress() throws NumberFormatException, IOException, ParseException {
    	DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    	Map<Date, Double> temps = new TreeMap<Date, Double>();
    	String[] nextLine;
    	CSVReader reader = new CSVReader(new FileReader("pogodynka.csv"));
    	nextLine = reader.readNext();
    	Double currPress = Double.parseDouble(nextLine[4]);
    	String date = nextLine[1];
    			
    	while ((nextLine = reader.readNext()) != null) {
    		
			if(nextLine[1].equals(date)){
    			if(Double.parseDouble(nextLine[4]) > currPress)
    				currPress = Double.parseDouble(nextLine[4]);
			}
			else{
				temps.put(df.parse(date), currPress);
				
				currPress = Double.parseDouble(nextLine[4]);
				date = nextLine[1];
			}
        }
    	
    	reader.close();
		
    	return temps;
	}

	private Map<Date, Double> getEkologiaPress() throws IOException, NumberFormatException, ParseException {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		
		Map<Date, Double> temps = new TreeMap<Date, Double>();
    	String [] nextLine;
    	CSVReader reader = new CSVReader(new FileReader("ekologia.csv"));
    	
    	while ((nextLine = reader.readNext()) != null) {
    		temps.put(df.parse(nextLine[1]), Double.parseDouble(nextLine[4]));
        }
    	
    	reader.close();
    	
		return temps;
	}
	
    private JFreeChart createChart(final CategoryDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart(
            "Por�wnanie danych - Ci�nienie", 
            "Dzie�",             
            "Ci�nienie [hPa]",              
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