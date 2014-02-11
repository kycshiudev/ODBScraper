import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/* Holds data from a given ODB article page
 */

public class ODBArticle {
	
	// also overload to take Calendar date later
	public ODBArticle(String url) {
		for (short tries = 0; tries < CONNECT_RETRIES; tries++) {
			try{
				current_page = Jsoup.connect(url).get();
				current_date = dateFromStr(url.replaceFirst(ODB_URL, ""));
				created_successfully = true;
				break;
			}
			catch (Exception e){
				created_successfully = false;
				System.out.println("--Connection error: "+e.getMessage());
			}
		}

		setContent();
	}
	
	public String pageParagraphs() {
		String ret_paragraphs = "";
		short par;
		for (par = 0; par < page_paragraphs.size() - 1; par++) {
			ret_paragraphs += page_paragraphs.get(par) + newline_seq + newline_seq;
		}
		ret_paragraphs += page_paragraphs.get(par);
		return ret_paragraphs;
	}
	
	public String pageDate() {
		SimpleDateFormat date_format = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		return date_format.format(current_date.getTime());
	}
	
	/* Populates page_* fields.  To be used immediately after connecting to the page.
	 */
	private void setContent(){
		page_title = scrapeTitle();
		page_read = scrapeRead();
		page_verse = scrapeVerse();
		scrapeParagraphs();
		page_poem = scrapePoem();
		page_thought_box = scrapeThoughtBox();
		
		next_page_url = current_page.select("a[class=article-next]").first().attr("href");
	}
	
	/* Get title of the ODB article for the provided date
	 */
	private String scrapeTitle() {
		String ret_title = "";
		Element titleContainer = current_page.select("h1[class=entry-title]").first();
		ret_title = titleContainer.unwrap().toString();
		return ret_title;
	}
	
	/* Get the "Read: " verses for the article
	 */
	private String scrapeRead() {
		String ret_read = "";
		Element readContainer = current_page.select("a[title=scripture reference verse]").first();
		ret_read = readContainer.unwrap().toString();
		return ret_read;
	}
	
	/* Get the quoted verse for the article
	 */
	private String scrapeVerse() {
		String ret_verse = "";
		Element verseContainer = current_page.select("div.meta-box").get(2);
		ret_verse = verseContainer.text();
		return ret_verse;
	}
	
	/* Get the paragraphs of the ODB article
	 */
	private void scrapeParagraphs() {
		page_paragraphs = new ArrayList<String>();
		Element contentContainer = current_page.select("section[class=entry-content]").first();
		Elements paragraphs = contentContainer.select("p");
		for (int par_index = 0; par_index < paragraphs.size(); par_index++) {
			Element paragraph = paragraphs.get(par_index);
			page_paragraphs.add(paragraph.text());
		}		
	}
	
	/* Get "poem" for the ODB article for the provided date
	 */
	private String scrapePoem() {
		String ret_poem = "";
		
		List<Node> poemTextNodes = current_page.select("div.poem-box").first().childNodes();
		for(int node_index = 0; node_index < poemTextNodes.size(); node_index++) {
			String text = poemTextNodes.get(node_index).toString();
			
			if (text.equals("<br />")) {
				ret_poem += newline_seq;
			} 
			else if (text.contains("<dash>")) {
				ret_poem  += dash_seq;
			}
			else {
				ret_poem += text;
			}
		}
		
		return ret_poem;
	}
	
	/* Get "thought-box" for the ODB article for the provided date
	 */
	private String scrapeThoughtBox() {
		String ret_thought_box = "";
		Element boxContainer = current_page.select("div.thought-box").first();
		ret_thought_box = boxContainer.unwrap().toString();
		return ret_thought_box;
	}
	
	/* Helper for checking dates and ignoring unnecessary fields.
	 */
	private static boolean sameDate(Calendar date1, Calendar date2) {
		boolean date_match, month_match, year_match;
		
		if ( (date1 == null) || (date2 == null) ) {
			return false;
		}
		
		date_match = date1.get(Calendar.DATE) == date2.get(Calendar.DATE);
		month_match = date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH);
		year_match = date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR);
		
		return date_match && month_match && year_match;
	}
	
	/* Expects date_str format as yyyy/MM/dd/ - the end part of the odb url
	 */
	private static Calendar dateFromStr(String date_str) {
		int year, month, day;
		Calendar ret_cal = Calendar.getInstance();
		
		// For Calendar: Jan = 0, Feb = 1, etc. so month is decremented;
		year = Integer.valueOf(date_str.substring(0, 4));
		month = Integer.valueOf(date_str.substring(5, 7)) - 1;
		day = Integer.valueOf(date_str.substring(8, 10));
		
		ret_cal.set(year, month, day);
		
		return ret_cal;
	}
	
	public String page_title;
	public String page_read;
	public String page_verse;
	public ArrayList<String> page_paragraphs;
	public String page_poem;
	public String page_thought_box;
	public String next_page_url = null;
	public Calendar current_date = null;
	public boolean created_successfully;
	
	private Document current_page = null;
	private static String newline_seq = "{\\\\line}";
	private static String dash_seq = "{\\\\emdash}";
	
	private static final String ODB_URL = "http://odb.org/";
	private static final short CONNECT_RETRIES = 5;
	private static final String DATE_FORMAT_PATTERN = "EEE, MMMM dd, yyyy";
}

