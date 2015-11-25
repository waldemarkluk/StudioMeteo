package pl.socewicz.kluk.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

public class HTMLParserMeteo {
	public HTMLParserMeteo() {
		FileWriter fileWriter = null;
		String timestamp;
		int hour = getCurrentHour();
		timestamp = (hour<12)?getYesterdayDateString():getCurrentTimeStamp();
		hour = ((hour/12*12)+12)%24;  // dla hour 0-11 zwraca 12;        dla hour 12-23 zwraca 0
		
		
		try {
			fileWriter = new FileWriter("meteo.csv", true);
			URL url = new URL("http://www.meteo.pl/metco/mgram_pict.php?ntype=2n&fdate="+timestamp+hour+"&row=151&col=91&lang=pl");
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1!=(n=in.read(buf)))
			{
			   out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			FileOutputStream fos = new FileOutputStream("obrazek.png");
			fos.write(response);
			fos.close();
			BufferedImage img = null;
			img = ImageIO.read(new File("obrazek.png"));
			int szerokosc = img.getWidth();
			int wysokosc = img.getHeight();
			boolean skok=false;
			wyjscie:
			for(int i=0; i<szerokosc; i++){
				if (skok==true)
					break;
				for(int j=0; j<wysokosc; j++){
					int kolor[] = getPixelColor(i, j, img);
					if (kolor[0]>200 && (kolor[1]+kolor[2]<100)){
						System.out.println("i: "+i+", j: "+j);
						skok=true;
						break wyjscie;
					}
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
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		
		return strDate;
	}
	
	public static int getCurrentHour() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		
		return Integer.parseInt(strDate);
	}
	
	public static String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);    
        return dateFormat.format(cal.getTime());
	}
	
	public static int[] getPixelColor(int x, int y, BufferedImage img){
	  // Getting pixel color by position x and y 
	  int clr=  img.getRGB(x,y); 
	  int  red   = (clr & 0x00ff0000) >> 16;
	  int  green = (clr & 0x0000ff00) >> 8;
	  int  blue  =  clr & 0x000000ff;
	  int color[]={1,2,3};
	  color[0]=red; 
	  color[1]=green; 
	  color[2]=blue;
	  return color;
	}
}
