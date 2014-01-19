import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* Use JSoup to get article information from ODB. Output formatted devotionals
 */
public class ODBScraper{
	
	public static void generateWordDoc(String startURL, String templatePath, String outputPath) {
		ODBArticle currentArticle = new ODBArticle(startURL);

		Hashtable ht = new Hashtable();	
		for (short day = 0; day < 7; day++) {
			ht.put("DATE"+day, currentArticle.pageDate());
			ht.put("TITLE"+day, currentArticle.page_title);
			ht.put("READ"+day, currentArticle.page_read);
			ht.put("VERSE"+day, currentArticle.page_verse);
			ht.put("PARAGRAPH"+day, currentArticle.pageParagraphs());
			ht.put("POEM"+day, currentArticle.page_poem);
			ht.put("THOUGHTBOX"+day, currentArticle.page_thought_box);
			ht.put("NEWPAGE", "{\\\\page}");

			currentArticle = new ODBArticle(currentArticle.next_page_url);
		}

		TemplateProcessor.templateReplace(ht, templatePath, outputPath);
		System.out.println("finish doc processing");
	}
	
	public static void main (String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SwingMenu.createAndShowGUI();
            }
        });
	}
}
