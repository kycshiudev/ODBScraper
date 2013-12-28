import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
		catch (Exception e){
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
		month_url = odb_url+year+"/"+month+"/";
		sub_url = month_url+day+"/";
		
		try{
			Document month_page = Jsoup.connect(month_url).get();
			//Element odb_calendar = month_page.select("table[id=wp-calendar]").first();
			Element url_link = month_page.select("a[href^="+sub_url+"]").first();
			//System.out.println(url_link.attr("href"));
			ret_url = url_link.attr("href");
		}
		catch (Exception e){
			// put better error handling here later.  probably pass the error up.
			ret_url = null;
		}
		
		return ret_url;
	}
	
	private static String odb_url = "http://odb.org/";
}

