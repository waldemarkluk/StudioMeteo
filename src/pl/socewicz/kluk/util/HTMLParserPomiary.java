package pl.socewicz.kluk.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParserPomiary {

    private List<String> cookies;
    private HttpURLConnection conn;

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36";

    private String url = "http://meteo.ftj.agh.edu.pl/meteo/archiwalneDaneMeteo";
    private String fileUri = "http://meteo.ftj.agh.edu.pl/meteo/pobierzDane";
    private String login = "waldemarkluk@onet.eu";
    private String pass = "studio";
    private String charset = "UTF-8";
    private String loginQuery = "";

	FileWriter fileWriter = new FileWriter("pomiary.csv", true);
    
    public static void main(String[] args) throws Exception {
      HTMLParserPomiary http = new HTMLParserPomiary();
    }

    
    public HTMLParserPomiary() throws Exception {

        //CookieHandler się buguje nie włączać!!
        //CookieHandler.setDefault(new CookieManager());
        loginQuery = String.format("login=%s&password=%s",
                URLEncoder.encode(login, charset),
                URLEncoder.encode(pass, charset));
        sendPost(url, loginQuery);
        //System.out.println(GetPageContent(url));
        //przyklad pobierania strony z plikiem
        /*
        fromDate:%s&
        toDate:%s&
        strefa:%s&
        separator:%s&
        id_data_type[]:%s&
        calculateValues[]:%s&
        calculateValues[]:%s&
        calculateValues[]:%s&
        subPeriod:%s&
        unique:%s
         */

        //System.out.println(getCRSFToken());
        getFile();
        try {
			fileWriter.flush();
			fileWriter.close();
		}catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
			e.printStackTrace();
		}
    }

    private void sendPost(String url, String postParams) throws Exception {

        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();

        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "pl,en-US;q=0.7,en;q=0.3");
        conn.setRequestProperty("Accept-encoding", "gzip, deflate");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Referer", url);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (this.cookies != null)
            for (String cookie : this.cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";")[0]);
        }
        //force manual content length controll
        byte[] postDataBytes = postParams.getBytes(charset);
        ((HttpURLConnection) conn).setFixedLengthStreamingMode(postDataBytes.length);
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode);
        setCookies(conn.getHeaderFields().get("Set-Cookie").subList(0,1));
        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        conn.disconnect();
    }
    private String getFile() throws Exception {
        /*
        fromDate:2015-11-01
        toDate:2015-11-02
        strefa:czas lokalny
        separator:1
        id_data_type[]:rc
        calculateValues[]:avg
        calculateValues[]:max
        calculateValues[]:min
        subPeriod:hour
        unique:14fa29330342e85709ff801c304df03a
         */
        String fromDate = getYesterdayDateString(),toDate = getCurrentDateString();
        String fileQuery=
                String.format("fromDate=%s&" +
                        "toDate=%s&" +
                        "strefa=czas lokalny&" +
                        "separator=1&" +
                        "id_data_type[]=sx&" +
                        "id_data_type[]=ta&" +
                        "id_data_type[]=pa&" +
                        "id_data_type[]=rc&" +
                        "calculateValues[]=max&" +
                        "subPeriod=day&" +
                        "unique=%s", fromDate, toDate, getCRSFToken());
        sendPost(url, fileQuery);
        //ok generacja pliku jest statefull - formularz zapisuje dane w sesji trzeba tylko pobrać pliczek
        System.out.println("==========Pliczek===========");
        System.out.println(GetPageContent(fileUri));
        return "";
    }
    private String getCRSFToken() throws Exception {
        Pattern inputPattern = Pattern.compile("<input type=\"hidden\" name=\"unique\" value=\"([0-9a-z]+)\" />");
        Matcher matcher = inputPattern.matcher(GetPageContent(url));
        if(matcher.find())
            return matcher.group(1);
        return null;
    }
    private String GetPageContent(String url) throws Exception {

        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();

        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "pl,en-US;q=0.7,en;q=0.3");

        if (cookies != null) {
            for (String cookie : this.cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";")[0]);
            }
        }
        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        Pattern p = Pattern.compile("2015.*");
        
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine+"\n");
            if(inputLine.length()>0 && p.matcher(inputLine).matches())
            	fileWriter.append(inputLine+"\n");
        }
        in.close();

        // Get the response cookies
        //setCookies(conn.getHeaderFields().get("Set-Cookie"));
        conn.disconnect();
        return response.toString();

    }

    public List<String> getCookies() {
        return cookies;
    }

    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }
    
	public static String getCurrentDateString() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		
		return strDate;
	}
	
	public static String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);    
        return dateFormat.format(cal.getTime());
	}

}