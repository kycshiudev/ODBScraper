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
	
	/* Get title of the ODB article for the provided date
	 */
	public static String getTitle(Calendar cal_date) {
		boolean successful_connect = true;
		String ret_title;
		
		// check if we need to change the page
		if ( !sameDate(cal_date, current_date) ) {
			successful_connect = setPage(cal_date);
		}
		if ( !successful_connect ) {
			return null;
		}
		
		Element titleContainer = current_page.select("h1[class=entry-title]").first();
		ret_title = titleContainer.unwrap().toString();
		return ret_title;
	}
	
	/* The take the url we need from the calendar on the odb page for that month.
	 */
	public static String dateToURL(Calendar date){
		String date_format_str, year, month, 
			day, month_url, sub_url, ret_url;
		SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMdd");
		date_format_str = date_format.format(date.getTime());
		year = date_format_str.substring(0, 4);
		month = date_format_str.substring(4, 6);
		day = date_format_str.substring(6, 8);
		month_url = ODB_URL+year+"/"+month+"/";
		sub_url = month_url+day+"/";
		
		try{
			Document month_page = Jsoup.connect(month_url).get();
			Element url_link = month_page.select("a[href^="+sub_url+"]").first();
			ret_url = url_link.attr("href");
		}
		catch (IOException e){
			// put better error handling here later.  probably pass the error up.
			ret_url = null;
		}
		
		return ret_url;
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
		
		return ret_connected;
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
}

