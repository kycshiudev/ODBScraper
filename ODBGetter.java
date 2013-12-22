import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import java.text.DateFormat;
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
		try{
			siteAtDate = Jsoup.connect(dateToURL(date)).get();
			Element titleContainer = siteAtDate.select("h3[class=entry-title]").first();
			Node titleNode = titleContainer.childNode(1);
			return titleNode.unwrap().toString();
		}
		catch (Exception e){
			return null;
		}
	}
	
	/* Using standardized Java date objects, create String representation
	 * of the ODB URL we need
	 */
	public static String dateToURL(Calendar date){
		String year, month, day;
		year = Integer.toString(date.get(Calendar.YEAR));
		month = Integer.toString(date.get(Calendar.MONTH) + 1);
		day = Integer.toString(date.get(Calendar.DATE));
		
		return odbURL+"/"+year+"/"+month+"/"+day+"/";
	}
	
	private static String odbURL = "http://odb.org";
}

