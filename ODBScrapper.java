import org.jsoup.*;
import org.jsoup.nodes.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
		cal.set(2013, 1, 1);
	
		System.out.println(cal.get(Calendar.YEAR));
		System.out.println(cal.get(Calendar.MONTH)+1);
		System.out.println(cal.get(Calendar.DATE));
		
		ODBGetter.setPage(cal);
		System.out.println(ODBGetter.getTitle());
		//System.out.println(ODBGetter.getPoem(cal));
	}
	
	/* Try ODBArticle methods
	 */
	static void ODBArticleExamples() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(1999, 5, 30);
	
		// populate links by month
		ODBMonth testMonth = new ODBMonth(cal);
		System.out.println(testMonth.urlForDate(cal));
		
		ODBArticle testArticle = new ODBArticle(testMonth.urlForDate(cal));
		System.out.println("article title: \t\t"+testArticle.page_title);
		for (String par : testArticle.page_paragraphs) {
			System.out.println("article paragraph: \t"+par);
		}
		System.out.println("article poem: \t\t"+testArticle.page_poem);
		System.out.println("article thought box: \t"+testArticle.page_thought_box);
		System.out.println("next page url: \t"+testArticle.next_page_url);
	}
	
	/* Try ODBMonth methods
	 */
	static void ODBMonthExamples() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2013, 1, 5);
	
		// populate links by month
		ODBMonth testMonth = new ODBMonth(cal);
		System.out.println("Month_as_Calendar: "+testMonth.current_month.get(Calendar.MONTH) 
				+ "/" +testMonth.current_month.get(Calendar.DATE));
		System.out.println(testMonth.urlForDate(cal));
		System.out.println(testMonth.urlForDate(9));
		System.out.println(testMonth.urlForDate(testMonth.current_month));
	}
	
	public static void main (String[] args)
	{
		//example1();
		//example2();
		
		//ODBGetterExamples();
		ODBArticleExamples();
		//ODBMonthExamples();
	}
	
}
