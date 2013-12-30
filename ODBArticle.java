import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
	
	/* Populates page_* fields.  To be used immediately after connecting to the page.
	 */
	private void setContent(){
		page_title = scrapeTitle(current_date);
		page_poem = scrapePoem(current_date);
	}
	
	/* Get title of the ODB article for the provided date
	 */
	private String scrapeTitle(Calendar cal_date) {
		boolean successful_connect = true;
		String ret_title;
		
		Element titleContainer = current_page.select("h1[class=entry-title]").first();
		ret_title = titleContainer.unwrap().toString();
		return ret_title;
	}
	
	/* Get "poem" for the ODB article for the provided date
	 */
	private String scrapePoem(Calendar cal_date) {
		boolean successful_connect = true;
		String ret_poem = "";
		
		List<Node> poemTextNodes = current_page.select("div.poem-box").first().childNodes();
		for(int node_index = 0; node_index < poemTextNodes.size(); node_index++) {
			String text = poemTextNodes.get(node_index).toString();
			if (text.equals("<br />")) {
				ret_poem += "\n";
			} 
			else {
				ret_poem += text;
			}
		}
		
		return ret_poem;
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
		
		year = Integer.valueOf(date_str.substring(0, 4));
		month = Integer.valueOf(date_str.substring(5, 7));
		day = Integer.valueOf(date_str.substring(8, 10));
		
		ret_cal.set(year, month, day);
		
		return ret_cal;
	}
	
	public String page_title;
	public String page_paragraphs;
	public String page_poem;
	public String page_thought_box;
	public Calendar current_date = null;
	public boolean created_successfully;
	
	private Document current_page = null;
	private String next_page_url = null;
	
	private static final String ODB_URL = "http://odb.org/";
	private static final short CONNECT_RETRIES = 5;
}

