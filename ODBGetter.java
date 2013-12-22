import org.jsoup.nodes.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/* Responsible for pulling out the interesting chunks
 * from odb.org.  JSoup does the heavy lifting.
 */

public abstract class ODBGetter {
	
	public static String getTitle(Calendar date) {
		return "blah";
	}
	
	private static String dateToURL(Calendar date){
		String year, month, day;
		year = Integer.toString(date.get(Calendar.YEAR));
		month = Integer.toString(date.get(Calendar.MONTH));
		day = Integer.toString(date.get(Calendar.DATE));
		
		return odbURL+"/"+year+"/"+month+"/"+day+"/";
	}
	
	private static String odbURL = "odb.org";
}

