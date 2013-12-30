import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;

/* Responsible for pulling out the interesting chunks
 * from odb.org.  JSoup does the heavy lifting.
 */

public abstract class ODBGetter {
	
	/* Accessor for page_title
	 */
	public static String getTitle() {
		return page_title;
	}
	
	/* Accessor for page_poem
	 */
	public static String getPoem() {
		return page_poem;
	}
	
	public static Calendar getDate() {
		return current_date;
	}
	
	/* Sets the page so we don't have to reconnect each time.
	 */
	public static boolean setPage(Calendar cal_date){
		boolean ret_connected = false;
		
		// need to set month
		if ( (current_odb_month == null) || (!current_odb_month.sameMonth(cal_date)) ) {
			current_odb_month = new ODBMonth(cal_date);
		}
		
		// make connection to ODB article for the date
		for (short tries = 0; tries < CONNECT_RETRIES; tries++) {
			try{
				current_page = Jsoup.connect(current_odb_month.urlForDate(cal_date)).get();
				current_date = (Calendar) cal_date.clone();
				ret_connected = true;
				break;
			}
			catch (Exception e){
				System.out.println("--Connection error: "+e.getMessage());
			}
		}
		
		setContent();
		return ret_connected;
	}
	
	/* Populates page_* fields.  To be used immediately after connecting to the page.
	 */
	private static void setContent(){
		page_title = scrapeTitle(current_date);
		//page_poem = scrapePoem(current_date);
	}
	
	/* Get title of the ODB article for the provided date
	 */
	private static String scrapeTitle(Calendar cal_date) {
		boolean successful_connect = true;
		String ret_title;
		
		Element titleContainer = current_page.select("h1[class=entry-title]").first();
		ret_title = titleContainer.unwrap().toString();
		return ret_title;
	}
	
	/* Get "poem" for the ODB article for the provided date
	 */
	private static String scrapePoem(Calendar cal_date) {
		boolean successful_connect = true;
		String ret_title = null;
		
		// check if we need to change the page
		if ( !sameDate(cal_date, current_date) ) {
			successful_connect = setPage(cal_date);
		}
		if ( !successful_connect ) {
			return null;
		}
		
		//Element titleContainer = current_page.select("h1[class=entry-title]").first();
		//ret_title = titleContainer.unwrap().toString();
		return ret_title;
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
	
	private static final String ODB_URL = "http://odb.org/";
	private static final short CONNECT_RETRIES = 5;
	
	private static Document current_page = null;
	private static Calendar current_date = null;
	private static ODBMonth current_odb_month = null;
	
	private static String page_title;
	private static String page_paragraphs;
	private static String page_poem;
	private static String page_thought_box;
}

