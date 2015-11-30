package pl.socewicz.kluk.util;

import java.awt.Color;
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
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

public class HTMLParserMeteo {
	public HTMLParserMeteo() {
		FileWriter fileWriter = null;
		String timestamp;
		int hour = getCurrentHour();
		timestamp = (hour<12)?getYesterdayDateString():getCurrentTimeStamp();
		hour = ((hour/12*12)+12)%24;  // dla hour 0-11 zwraca 12;        dla hour 12-23 zwraca 0
		String zera ="";
		if (hour==0)
			zera+="0";
		
		
		try {
			fileWriter = new FileWriter("meteo.csv", true);
			URL url = new URL("http://www.meteo.pl/metco/mgram_pict.php?ntype=2n&fdate=2015113000&row=151&col=91&lang=pl");
			//URL url = new URL("http://www.meteo.pl/metco/mgram_pict.php?ntype=2n&fdate="+timestamp+hour+zera+"&row=151&col=91&lang=pl");
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
			
			int x1=0,x2=0, x3=0, x4=0;
			if (hour ==12){
				x1 = 135;
				x2 = 286;
				x3 = 437;
				x4 = 588;
			} else {
				x1 = 210;
				x2 = 361;
				x3 = 512;
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////
			///                                        TEMPERATURA                                     ////////
			///////////////////////////////////////////////////////////////////////////////////////////////////
			
			int temp_max_px=0;          // wysokosc piksela z najwyzsza temp
			double temp_max1=0;
			double temp_max2=0;
			double temp_max3=0;
			int kolor[]={0,0,0};
			int zero_px=0;              // wysokosc piksela z najwyzszym zerem na skali
			double zero_val =0;          // wartosc temperatury konczacej sie zerem najwyzej na skali
			boolean minus = false;   //  do rozpoznania
			boolean jeden = false;
			boolean dwa = false;
			boolean trzy = false;    //  tekstu na obrazie

			int licznik_pikseli =0; //szuka szesciu czarnych pikseli z rzedu w kolumnie
			for(int j=54; j<140; j++){                                  // ta petla szuka polozenia zero_px - patrz wyzej
				kolor= getPixelColor(57, j, img);
				if (kolor[0]+kolor[1]+kolor[2]<20){
					//System.out.println("znalazlo czarne pola, j="+j);
					licznik_pikseli++;
				} else{
					licznik_pikseli=0;
				}
				if(licznik_pikseli == 6){
					zero_px=j-3;
					kolor= getPixelColor(45, j-2, img);
					if (kolor[0]+kolor[1]+kolor[2]<20)
						minus=true;
					kolor =  getPixelColor(49, j, img);
					if (kolor[0]+kolor[1]+kolor[2]<20)
						jeden=true;
					kolor =  getPixelColor(50, j-2, img);
					if (kolor[0]+kolor[1]+kolor[2]<20)
						dwa=true;
					kolor =  getPixelColor(50, j-3, img);
					if (kolor[0]+kolor[1]+kolor[2]<20)
						trzy=true;
					
					if (minus && trzy)
						zero_val=-30;
					if (minus && dwa)
						zero_val=-20;
					if (minus && jeden)
						zero_val=-10;
					if (!minus && jeden)
						zero_val=10;
					if (!minus && dwa)
						zero_val=20;
					if (!minus && trzy)
						zero_val=30;
					if (!minus && !trzy && !dwa && !jeden)
						zero_val=0;
					
					break;
				}
				
			}
			
			boolean skok=false;
			wyjscie:
			for(int j=58; j<134; j++){                            // ta petla szuka polozenia temp_max_px - patrz wyzej
				if (skok==true)   // do wyjscia z podwojnej petli
					break;
				for(int i=x1; i<x2; i++){
					kolor= getPixelColor(i, j, img);
					if (kolor[0]>200 && (kolor[1]+kolor[2]<100)){
						//System.out.println("i: "+i+", j: "+j);
						temp_max_px = j; 
						skok=true;
						break wyjscie;
					}
				}
			}

			temp_max1 = zero_val - (temp_max_px - zero_px) * 5.0 / 23.0;  // wzor do liczenia temperatury
			temp_max1 = Math.round(temp_max1 * 10);
			temp_max1 = temp_max1/10;
			
			
			
			
			skok=false;
			wyjscie:
			for(int j=58; j<134; j++){                            // ta petla szuka polozenia temp_max_px - patrz wyzej
				if (skok==true)   // do wyjscia z podwojnej petli
					break;
				for(int i=x2; i<x3; i++){
					kolor = getPixelColor(i, j, img);
					if (kolor[0]>200 && (kolor[1]+kolor[2]<100)){
						//System.out.println("i: "+i+", j: "+j);
						temp_max_px = j; 
						skok=true;
						break wyjscie;
					}
				}
			}

			temp_max2 = zero_val - (temp_max_px - zero_px) * 5.0 / 23.0;  // wzor do liczenia temperatury
			temp_max2 = Math.round(temp_max2 * 10);
			temp_max2 = temp_max2/10;
			
			
			if(hour == 12){
				skok=false;
				wyjscie:
				for(int j=58; j<134; j++){                            // ta petla szuka polozenia temp_max_px - patrz wyzej
					if (skok==true)   // do wyjscia z podwojnej petli
						break;
					for(int i=x3; i<x4; i++){
						kolor = getPixelColor(i, j, img);
						if (kolor[0]>200 && (kolor[1]+kolor[2]<100)){
							//System.out.println("i: "+i+", j: "+j);
							temp_max_px = j; 
							skok=true;
							break wyjscie;
						}
					}
				}
				temp_max3 = zero_val - (temp_max_px - zero_px) * 5.0 / 23.0;  // wzor do liczenia temperatury
				temp_max3 = Math.round(temp_max3 * 10);
				temp_max3 = temp_max3/10;
			}


			
			///////////////////////////////////////////////////////////////////////////////////////////////////
			///                                        OPADY                                           ////////
			///////////////////////////////////////////////////////////////////////////////////////////////////
			
			double opad1 =0;
			double opad2=0;
			double opad3=0;
			for(int j=143; j<220; j++){
				for(int i=x1; i<x2; i++){
					kolor = getPixelColor(i, j, img);
					if ((kolor[1]>150 && (kolor[0]+kolor[2]<100)) || (kolor[2]>150 && (kolor[1]+kolor[0]<100))){
						opad1++;
					}
				}
			}
			opad1 = opad1*100.0 / ((220-143)*(x2-x1));                      // wzor na deszcz
			opad1 = Math.round(opad1 * 100);
			opad1 = opad1/100;
			
			
			for(int j=143; j<220; j++){
				for(int i=x2; i<x3; i++){
					 kolor = getPixelColor(i, j, img);
					 if ((kolor[1]>150 && (kolor[0]+kolor[2]<100)) || (kolor[2]>150 && (kolor[1]+kolor[0]<100))){
						opad2++;
					}
				}
			}
			opad2 = opad2*100.0 / ((220-143)*(x2-x1));                      // wzor na deszcz
			opad2 = Math.round(opad2 * 100);
			opad2 = opad2/100;
			
			if(hour == 12){
				for(int j=143; j<220; j++){
					for(int i=x3; i<x4; i++){
						kolor = getPixelColor(i, j, img);
						if ((kolor[1]>150 && (kolor[0]+kolor[2]<100)) || (kolor[2]>150 && (kolor[1]+kolor[0]<100))){
							opad3++;
						}
					}
				}
				opad3 = opad3*100.0 / ((220-143)*(x2-x1));                      // wzor na deszcz
				opad3 = Math.round(opad3 * 100);
				opad3 = opad3/100;
			}
			System.out.println("Opady ok");
			
			///////////////////////////////////////////////////////////////////////////////////////////////////
			///                                        WIATR                                           ////////
			///////////////////////////////////////////////////////////////////////////////////////////////////
		
			
			double wiatr1= 0;
			double wiatr2=0;
			double wiatr3=0;
			double wiatr_temp;
			Map<Integer, Double> mapa_wiatrow = new TreeMap<Integer, Double>(new Comparator<Integer>()
	        {
	            public int compare(Integer o1, Integer o2)
	            {
	                return o1.compareTo(o2);
	            } 
	        });
			mapa_wiatrow.put(0x0080ffff, 0.5*3.6);
			mapa_wiatrow.put(0x0000ffff, 1.5*3.6);
			mapa_wiatrow.put(0x0000c3ff, 2.5*3.6);
			mapa_wiatrow.put(0x000082ff, 3.5*3.6);
			mapa_wiatrow.put(0x00004b82, 4.5*3.6);
			mapa_wiatrow.put(0x00008200, 5.5*3.6);
			mapa_wiatrow.put(0x001ec800, 6.5*3.6);
			mapa_wiatrow.put(0x004bff1e, 7.5*3.6);
			mapa_wiatrow.put(0x0082ff4b, 8.5*3.6);
			mapa_wiatrow.put(0x00000000, 0.0*3.6);
			
			
			for(int i=x1; i<x2; i++){
				kolor = getPixelColor(i, 390, img);
				int kol =kolor[0]*256*256+kolor[1]*256+kolor[2];
				if(!mapa_wiatrow.containsKey(kol))
					continue;
				wiatr_temp=mapa_wiatrow.get(kol);
				if(wiatr_temp>wiatr1){
					wiatr1=wiatr_temp;
				}
			}
			
			for(int i=x2; i<x3; i++){
				kolor = getPixelColor(i, 390, img);
				int kol =kolor[0]*256*256+kolor[1]*256+kolor[2];
				if(!mapa_wiatrow.containsKey(kol))
					continue;
				wiatr_temp=mapa_wiatrow.get(kol);
				if(wiatr_temp>wiatr2){
					wiatr2=wiatr_temp;
				}
			}
			
			if(hour == 12){
				for(int i=x3; i<x4; i++){
					kolor = getPixelColor(i, 390, img);
					int kol =kolor[0]*256*256+kolor[1]*256+kolor[2];
					if(!mapa_wiatrow.containsKey(kol))
						continue;
					wiatr_temp=mapa_wiatrow.get(kol);
					if(wiatr_temp>wiatr3){
						wiatr3=wiatr_temp;
					}
				}
			}
			
			wiatr1 = Math.round(wiatr1 * 100);
			wiatr1 = wiatr1/100;
			wiatr2 = Math.round(wiatr2 * 100);
			wiatr2 = wiatr2/100;
			wiatr3 = Math.round(wiatr3 * 100);
			wiatr3 = wiatr3/100;
			
			System.out.println("Wiatr ok");
			
			///////////////////////////////////////////////////////////////////////////////////////////////////
			///                                        CIŚNIENIE                                       ////////
			///////////////////////////////////////////////////////////////////////////////////////////////////
		
			float[] hsv = new float[3];
			double h = 0.0;
			double cisn1= 0;
			double cisn2=0;
			double cisn3=0;
			double cisn_temp;
			
			
			for(int i=x1; i<x2; i++){
				kolor = getPixelColor(i, 303,  img);
				if(kolor[0]+kolor[1]+kolor[2]<20) continue;
				Color.RGBtoHSB(kolor[0],kolor[1],kolor[2],hsv);
				h=hsv[0]*360.0;
				//System.out.println(h);
				cisn_temp=1040-35.0-45.0/270.0*((h+60)%360);                          // wzor na cisnienie
				if(cisn_temp>cisn1){
					cisn1=cisn_temp;
				}
			}
			
			for(int i=x2; i<x3; i++){
				kolor = getPixelColor(i, 303, img);
				if(kolor[0]+kolor[1]+kolor[2]<20) continue;
				Color.RGBtoHSB(kolor[0],kolor[1],kolor[2],hsv);
				h=hsv[0]*360.0;
				cisn_temp=1040-35.0-45.0/270.0*((h+60)%360);                           // wzor na cisnienie
				if(cisn_temp>cisn2){
					cisn2=cisn_temp;
				}
			}
			
			if(hour == 12){
				for(int i=x3; i<x4; i++){
					kolor = getPixelColor(i, 303, img);
					if(kolor[0]+kolor[1]+kolor[2]<20) continue;
					Color.RGBtoHSB(kolor[0],kolor[1],kolor[2],hsv);
					h=hsv[0]*360.0;
					cisn_temp=1040-35.0-45.0/270.0*((h+60)%360);                           // wzor na cisnienie
					if(cisn_temp>cisn3){
						cisn3=cisn_temp;
					}
				}
			}
			
			

			cisn1= Math.round(cisn1 * 100);
			cisn1 = cisn1/100;
			cisn2 = Math.round(cisn2 * 100);
			cisn2 = cisn2/100;
			cisn3 = Math.round(cisn3 * 100);
			cisn3 = cisn3/100;
			
			
			String currentCSVLine = "";
			if(hour == 12){
				currentCSVLine = String.format("%s,%s,%s,%s,%s,%s,%s", getToday(), getToday(), "", temp_max1, cisn1, wiatr1, opad1) +
					System.getProperty("line.separator");
				currentCSVLine.toString();
				fileWriter.append(currentCSVLine);
				currentCSVLine = String.format("%s,%s,%s,%s,%s,%s,%s", getToday(), getTomorrow(), "", temp_max2, cisn2, wiatr2, opad2) +
					System.getProperty("line.separator");
				currentCSVLine.toString();
				fileWriter.append(currentCSVLine);
				currentCSVLine = String.format("%s,%s,%s,%s,%s,%s,%s", getToday(), getAfterTomorrow(), "", temp_max3, cisn3, wiatr3, opad3) +
					System.getProperty("line.separator");
				currentCSVLine.toString();
				fileWriter.append(currentCSVLine);
			}else{
				currentCSVLine = String.format("%s,%s,%s,%s,%s,%s,%s", getToday(), getTomorrow(), "", temp_max1, cisn1, wiatr1, opad1) +
						System.getProperty("line.separator");
				currentCSVLine.toString();
				fileWriter.append(currentCSVLine);
				currentCSVLine = String.format("%s,%s,%s,%s,%s,%s,%s", getToday(), getAfterTomorrow(), "", temp_max2, cisn2, wiatr2, opad2) +
						System.getProperty("line.separator");
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
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		
		return strDate;
	}
	
	public static String getToday(){
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		
		return strDate;
	}
	
	public static String getTomorrow(){
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);    
        return dateFormat.format(cal.getTime());
	}
	
	public static String getAfterTomorrow(){
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);    
        return dateFormat.format(cal.getTime());
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
