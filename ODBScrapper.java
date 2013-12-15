import org.jsoup.*;
import org.jsoup.nodes.*;

public class ODBScrapper{

	public static void main (String[] args)
	{
		String html = "<html><head><title>First parse</title></head>"
				  + "<body><p>Parsed HTML into a doc.</p></body></html>";
		Document doc = Jsoup.parse(html);
		System.out.println("Hello World");
		for (Node child:doc.childNodes()){
			System.out.println(child.toString());
		}
	}
	
}