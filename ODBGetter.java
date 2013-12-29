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
	public static String getTitle(Calendar date) {
		Document siteAtDate;
		String ret_title;
		try{
			siteAtDate = Jsoup.connect(dateToURL(date)).get();
			Element titleContainer = siteAtDate.select("h1[class=entry-title]").first();
			ret_title = titleContainer.unwrap().toString();
		}
		catch (IOException e){
			ret_title = null;
		}
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
	public static boolean setPage(Calendar date){
		boolean ret_connected = false;
		
		try{
			
		}
		catch (Exception e){
			System.out.println("--Connection error: "+e.getMessage());
		}
		
		return ret_connected;
	}
	
	/* Fills arrray of links with the appropriate link.  Indexed by date.
	 * Return true or false based on success of connection
	 */
	public static boolean populateLinksByMonth(Calendar date){
		String date_format_str, year, month, month_url;
		Elements links_elements;
		
		SimpleDateFormat date_format = new SimpleDateFormat("yyyyMM");
		date_format_str = date_format.format(date.getTime());
		year = date_format_str.substring(0, 4);
		month = date_format_str.substring(4, 6);
		month_url = ODB_URL+year+"/"+month+"/";

		// connect and detect errors
		try{
			Document month_page = Jsoup.connect(month_url).get();
			links_elements = month_page.select("a[href^="+month_url+"]");
		}
		catch (IOException e){
			// put better error handling here later.  probably pass the error up.
			System.out.println("--Connection error: "+e.getMessage());
			return false;
		}
		
		// parse through links
		for (int date_index = 0; date_index < links_elements.size(); date_index++){
			links_by_month[date_index] = links_elements.get(date_index).attr("href");
			System.out.println("Debug: "+date_index+" - "+links_by_month[date_index]);
		}
		
		return true;
	}
	
	private static final String ODB_URL = "http://odb.org/";
	private static Document current_page;
	private static Calendar current_date;
	private static String[] links_by_month = new String[31]; //later create ODBCalendar that handles storing and giving links
}

