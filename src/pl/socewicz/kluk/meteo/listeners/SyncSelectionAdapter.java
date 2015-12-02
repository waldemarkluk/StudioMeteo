package pl.socewicz.kluk.meteo.listeners;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.xml.sax.SAXException;

import pl.socewicz.kluk.util.HTMLParserMeteo;

public class SyncSelectionAdapter extends SelectionAdapter {
	StyledText log;
	
	public SyncSelectionAdapter(StyledText infoLogText){
		this.log = infoLogText;
	}

	@Override
	public void widgetSelected(SelectionEvent e){
		super.widgetSelected(e);
		log.append("Started parsing website...\r\n");
		try{downloadData();}
		catch(Exception exc){log.append(exc.getMessage()+"\r\n");log.append("Failed, while parsing\r\n");return;}
		log.append("Website parse success!\r\n");
		
	}
	
	private void downloadData() throws SAXException, IOException, URISyntaxException, ParserConfigurationException{
		new HTMLParserMeteo();
	}
}