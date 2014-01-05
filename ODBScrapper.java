import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

public class ODBScrapper{

	public static void main (String[] args)
	{
		String articleURL = "http://odb.org/2014/01/05/adoption/";
		String templatePathFilename = ".\\testrtf.rtf";
		String outputPathFilename = ".\\testrtfout1.doc";
		
		TemplateProcessor.generateWordDoc(articleURL, templatePathFilename, outputPathFilename);
	}
	
}
