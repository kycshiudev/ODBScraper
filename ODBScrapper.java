import org.jsoup.*;
import org.jsoup.nodes.*;
import java.text.DateFormat;
import java.util.Calendar;

public class ODBScrapper{

	/* jsoup sample.  parse html from string
	 * and read out some nodes from the tree
	 */
	static void example1(){
		String html1 = "<html><head><title>First parse</title></head>"
				+ "<body><p>Parsed HTML into a doc.</p></body></html>";
		String html2 = "<html><head><title>First parse</title></head>"
				+ "<body><p>Parsed HTML into a doc.</p></body></html>";
		String html = html1;
		Document doc = Jsoup.parse(html);
		System.out.println("Hello World");
		//for (Node child:doc.childNodes()){
		//	System.out.println(child.toString());
		//}
		System.out.println(doc.childNode(0).toString());
		System.out.println(doc.childNode(0).childNodes().size());
		System.out.println(doc.childNode(0).childNode(1).toString());
	}
	
	/* Get HTML from string URL
	 */
	static void example2(){
		String URLstr = "http://www-inst.eecs.berkeley.edu/~cs164/fa13/";
       	Document doc2;
        
       	try{
           	doc2 = Jsoup.connect(URLstr).get();
           	System.out.println(doc2.childNode(0).toString());
        }
        catch (Exception e)
        {
            System.out.println("couldn't connect to " + URLstr);
        }
        
	}
	
	/* Try ODBGetter methods
	 */
	static void ODBGetterExamples(){
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2013, 11, 1);
		
		System.out.println(cal.get(Calendar.YEAR));
		System.out.println(cal.get(Calendar.MONTH));
		System.out.println(cal.get(Calendar.DATE));
		
		System.out.println(ODBGetter.dateToURL(cal));
		
		// title
		System.out.println(ODBGetter.getTitle(cal));
	}
	
	public static void main (String[] args)
	{
		//example1();
		//example2();
		
		ODBGetterExamples();
	}
	
}
