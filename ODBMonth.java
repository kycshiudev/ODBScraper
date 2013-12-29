import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.io.IOException;

/* Holds the links that are on ODB's sidebar calendar. Makes them available in
 * a convenient package for the ODBGetter.
 */
public class ODBMonth {
	
	/* Holds one month's links and populate our internal structures. The 
	 * public created_successfully parameter will tell anyone who's interested
	 * whether or not we were able to connect and create our month abstraction.
	 */
	public ODBMonth(Calendar cal_date) {
		Document month_page;
		month_as_calendar = (Calendar) cal_date.clone();
		
		month_page = connectToMonthPage(cal_date);
		if (month_page == null) {
			created_successfully = false;
			return;
		}
		
		populateLinksByMonth(cal_date, month_page);
		created_successfully = true;
	}
	
	// these need better error checking later
	public String urlForDate(int date) {
		return links_by_month.get(date);
	}
	
	public String urlForDate(Calendar cal_date) {
		return links_by_month.get(cal_date.get(Calendar.DATE));
	}
	
	public boolean sameMonth(Calendar cal_date) {
		boolean month_match, year_match;
		Calendar my_date = this.month_as_calendar;
		
		month_match = my_date.get(Calendar.MONTH) == cal_date.get(Calendar.MONTH);
		year_match = my_date.get(Calendar.YEAR) == cal_date.get(Calendar.YEAR);
		
		return month_match && year_match;
	}
	
	public boolean sameMonth(ODBMonth other) {
		return this.sameMonth(other.month_as_calendar);
	}
	
	/* Used by the constructor to extract the Document from the ODB page for 
	 * the month given by cal_date.
	 */
	private Document connectToMonthPage(Calendar cal_date) {
		String year_month_str, year, month, month_url;
		Document ret_month_page = null;
		SimpleDateFormat year_and_month_format = new SimpleDateFormat("yyyy/MM/");
		
		month_url = ODB_URL+year_and_month_format.format(cal_date.getTime());

		// connect and detect errors
		for (short tries = 0; tries < CONNECT_RETRIES; tries++) {
			try {
				Connection month_con = Jsoup.connect(month_url);
				month_con.ignoreHttpErrors(true);
				ret_month_page = month_con.get();
				break;
			}
			catch (IOException e) {
				// put better error handling here later.  probably pass the error up.
				System.out.println("--Connection error: "+e.getMessage());
				e.printStackTrace();
			}
		}
		
		// will be null if we couldn't connect successfully
		return ret_month_page;
	}
	
	/* Used by constructor to fill the internal structure that holds the links.
	 */
	private void populateLinksByMonth(Calendar cal_date, Document month_page) {
		String full_date_str;
		int start_month = cal_date.get(Calendar.MONTH);
		int current_date;
		SimpleDateFormat full_date_format = new SimpleDateFormat("yyyy/MM/dd");
		
		links_by_month = new ArrayList<String>(MONTH_CAPACITY);
		links_by_month.add(ODB_URL); //align index with the first of the month
		cal_date.set(Calendar.DATE, 1);
		
		while (cal_date.get(Calendar.MONTH) == start_month) {
			full_date_str = full_date_format.format(cal_date.getTime()); 
			Node link_node = month_page.select("a[href^="+ODB_URL+full_date_str+"]").first();
			links_by_month.add(link_node.attr("href"));
			cal_date.add(Calendar.DATE, 1);
		}
	}
	
	public boolean created_successfully;
	public Calendar month_as_calendar;
	
	private static final String ODB_URL = "http://odb.org/";
	private static final short MONTH_CAPACITY = 31;
	private static final short CONNECT_RETRIES = 5;
	private ArrayList<String> links_by_month;
}
