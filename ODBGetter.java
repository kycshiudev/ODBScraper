import org.jsoup.nodes.*;
import java.text.DateFormat;;

/* Responsible for pulling out the interesting chunks
 * from odb.org.  JSoup does the heavy lifting.
 */

public abstract class ODBGetter {
	
	public static String getTitle(DateFormat date) {
		return "blah";
	}
	
	private static String dateToURL(DateFormat date){
		String year, month, day;
		year = Integer.toString(date.YEAR_FIELD);
		month = Integer.toString(date.MONTH_FIELD);
		day = Integer.toString(date.DATE_FIELD);
		
		return odbURL+"/"+year+"/"+month+"/"+day+"/";
	}
	
	private static String odbURL = "odb.org";
}

