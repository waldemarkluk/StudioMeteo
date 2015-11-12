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
			fileWriter = new FileWriter("meteo.csv");
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
			System.out.println("szerokosc: " + img.getWidth());
			System.out.println("wysokosc: " + img.getHeight());
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
}
