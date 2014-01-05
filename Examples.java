import java.util.Calendar;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Examples {
	/* jsoup sample.  parse html from string
	 * and read out some nodes from the tree
	 */
	public static void example1(){
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
	public static void example2(){
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
	
	/* test scraping
	 */
	public static void scrapeExample() {
		String URLstr = "http://odb.org/2014/01/30/precious-in-gods-eyes/";
		Document doc;
		
		try{
           	doc = Jsoup.connect(URLstr).get();

    		Elements elements = doc.select("div.meta-box");
    		for (Element element : elements) {
    			System.out.println(element.text());
    		}
        }
        catch (Exception e)
        {
            System.out.println("couldn't connect to " + URLstr);
        }
		
	}
	
	/* Try ODBGetter methods
	 */
	public static void ODBGetterExamples(){
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
	public static void ODBArticleExamples() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2014, 0, 11);
	
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
	public static void ODBMonthExamples() {
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
	
	/* for checking templateReplace.  make it public first if you need to use this
	public static void XMLOutputExamples() {
		String templatePathFilename = "C:\\Users\\Kenny\\Desktop\\testrtf1.doc";
		String outputPathFilename = "C:\\Users\\Kenny\\Desktop\\word_output.doc";

		Hashtable ht = new Hashtable();
		ht.put("TEST_TITLE1","REPLACE1");
		
		TemplateProcessor.templateReplace(ht, templatePathFilename, outputPathFilename);
		System.out.println("finish doc processing");
	}
	*/
}
