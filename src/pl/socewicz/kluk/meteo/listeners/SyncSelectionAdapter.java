package pl.socewicz.kluk.meteo.listeners;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import pl.socewicz.kluk.util.HTMLParser;

public class SyncSelectionAdapter extends SelectionAdapter {
	StyledText log;
	
	public SyncSelectionAdapter(StyledText infoLogText){
		this.log = infoLogText;
	}

	@Override
	public void widgetSelected(SelectionEvent e){
		super.widgetSelected(e);
		log.append("Started parsing website...\r\n");
		try{downloadFromPogodynka();}
		catch(Exception exc){log.append(exc.getMessage()+"\r\n");log.append("Failed, while parsing\r\n");return;}
		log.append("Website parse success!\r\n");
		
	}
	
	private void downloadFromPogodynka() throws SAXException, IOException, URISyntaxException, ParserConfigurationException{
		HTMLParser pars = new HTMLParser();
	}
}