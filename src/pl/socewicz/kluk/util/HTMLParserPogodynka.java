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
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

public class HTMLParserPogodynka {
	public HTMLParserPogodynka() {
		URL nbp;
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter("pogodynka.csv");
			NodeList list;
			Node currentNode;
			
			String today = getCurrentTimeStamp();
			String predictionDate = "";
			String time = "";
			String temp = "";
			String pressure = "";
			String wind = "";
			String currentCSVLine = "";
			
			TagNameFilter tr = new TagNameFilter("tr");
			HasAttributeFilter classinfotable = new HasAttributeFilter("class", "info_tab");
			AndFilter filterres = new AndFilter(new NodeFilter[]{tr,classinfotable});
			OrFilter orkolory = new OrFilter(new NodeFilter[]{new HasAttributeFilter("style", "background-color:#cfecff "), new HasAttributeFilter("style", "background-color:#e5f0eb"), new HasAttributeFilter("style", "background-color:#e5f1f8"), new HasAttributeFilter("style", "background-color:#ececec")});
			AndFilter lol = new AndFilter(new NodeFilter[]{tr, orkolory});
			OrFilter orfltr = new OrFilter(new NodeFilter[]{filterres,lol});
			FilterBean fb = new FilterBean();
			nbp = new URL("http://pogodynka.pl/polska/16dni/krakow_krakow");
			URLConnection urlConn = nbp.openConnection();
			fb.setConnection(urlConn);
			fb.setFilters(new NodeFilter[]{orfltr});
			list = fb.getNodes();
			SimpleNodeIterator x = list.elements();
			
			while(x.hasMoreNodes()){
				currentNode = x.nextNode();
				
				if(currentNode.getText().startsWith("tr class='info_tab'"))
					predictionDate = currentNode.getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(0).getText().split(" ")[1].replaceAll("\\s","");	
				else{
					time = currentNode.getChildren().elementAt(1).getChildren().elementAt(0).getText().replaceAll("\\s","");;
					temp = currentNode.getChildren().elementAt(3).getChildren().elementAt(1).getChildren().elementAt(0).getText().split(" ")[0].replaceAll("\\s","");
					pressure = currentNode.getChildren().elementAt(5).getChildren().elementAt(0).getText().split(" ")[0].replaceAll("\\s","");
					wind = currentNode.getChildren().elementAt(7).getChildren().elementAt(3).getChildren().elementAt(0).getText().split(" ")[0].replaceAll("\\s","");
					
					currentCSVLine = String.format("%s,%s,%s,%s,%s,%s", today, predictionDate, time, temp, pressure, wind) + System.getProperty("line.separator");
					currentCSVLine.toString();
					fileWriter.append(currentCSVLine);
				}
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
