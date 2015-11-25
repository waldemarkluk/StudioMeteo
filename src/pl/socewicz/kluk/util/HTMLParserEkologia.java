package pl.socewicz.kluk.util;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.beans.FilterBean;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

public class HTMLParserEkologia {
	public HTMLParserEkologia() {
		URL nbp;
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter("ekologia.csv", true);
			NodeList list;
			Node currentNode;
			
			String today = getCurrentTimeStamp();
			String predictionDate = "";
			String time = "";
			String temp = "";
			String pressure = "";
			String wind = "";
			String raindrops = "";
			String currentCSVLine = "";
			
			HasAttributeFilter classdayoddslim = new HasAttributeFilter("class", "day odd slim");
			HasAttributeFilter classdayevenslim = new HasAttributeFilter("class", "day even slim");
			OrFilter orfltr = new OrFilter(new NodeFilter[]{classdayevenslim, classdayoddslim});
			FilterBean fb = new FilterBean();
			nbp = new URL("https://www.ekologia.pl/pogoda/polska/malopolskie/krakow/dlugoterminowa,15-dni");
			URLConnection urlConn = nbp.openConnection();
			fb.setConnection(urlConn);
			fb.setFilters(new NodeFilter[]{orfltr});
			list = fb.getNodes();
			SimpleNodeIterator x = list.elements();
			
			while(x.hasMoreNodes()){
				currentNode = x.nextNode();
				
					predictionDate = currentNode.getChildren().elementAt(1).getChildren().elementAt(0).getText().split(" ")[1].replaceAll("\\s","").concat(".2015");	
					temp = currentNode.getChildren().elementAt(3).getChildren().elementAt(0).getText().split(" ")[0].replaceAll("\\s","");
					pressure = currentNode.getChildren().elementAt(12).getChildren().elementAt(0).getText().split(" ")[0].replaceAll("\\s","");
					wind = currentNode.getChildren().elementAt(14).getChildren().elementAt(0).getText().split(" ")[0].replaceAll("\\s","");
					raindrops = currentNode.getChildren().elementAt(16).getChildren().elementAt(0).getText().split(" ")[0].replaceAll("\\s", "");
					
					currentCSVLine = String.format("%s,%s,%s,%s,%s,%s,%s", today, predictionDate, time, temp, pressure, wind, raindrops) + System.getProperty("line.separator");
					currentCSVLine.toString();
					fileWriter.append(currentCSVLine);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			}catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}
	
	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		
		return strDate;
	}
}
