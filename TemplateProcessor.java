import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

/**
 * This code takes in a hashtable containing key fields required to populate
 * values into a Word template (XML) and output a Word document (also XML).
 * Template should contain ##KEY## fields for each hashtable key with same
 * name (without the ##s); the ##KEY## will be replaced by the value.
 * The main() method is written as an example.
 * Modified from code found at http://dinoch.dyndns.org:7070/WordML/AboutWordML.jsp
 * @author C. Peter Chen of http://dev-notes.com
 * @date 20080327
 */

public class TemplateProcessor {
	/**
	 * 
	 * @param ht
	 * @param templatePathFileName
	 * @param outputPathFileName
	 * @author C. Peter Chen of http://dev-notes.com
	 * @date 20080327
	 */
	public static void templateReplace(Hashtable ht, String templatePathFilename, String outputPathFilename) {	
		try {
			BufferedReader reader = new BufferedReader(new FileReader(templatePathFilename));
			
			File destination = new File(outputPathFilename);
			BufferedWriter writer = new BufferedWriter(new FileWriter(destination));
			//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination), "UTF-8"));
			
			String thisLine;
			int i = 0;
			
			while ((thisLine = reader.readLine()) != null) {				
				for (java.util.Enumeration e = ht.keys(); e.hasMoreElements();) {
					String name = (String) e.nextElement();
					String value = ht.get(name).toString();
					// Use this if we need to XML-encode the string in hashtable value...
					//thisLine = thisLine.replaceAll("##" + name.toUpperCase() + "##", XmlEncode(value));
					// ... or this if we do not need to do XML-encode.
					thisLine= thisLine.replaceAll(name.toUpperCase(), value);
			    }
				writer.write(thisLine);
				writer.newLine();
				i++;
			}
			writer.close();
		}
		catch (Exception e) {
			System.out.println("exception!=" + e);
		}
	}

	/**
	 * Encodes regular text to XML.
	 * @param text
	 * @return string
	 * @author http://dinoch.dyndns.org:7070/WordML/AboutWordML.jsp
	 * @date 20050328
	 */
	private static String XmlEncode(String text) {
		int[] charsRequiringEncoding = {38, 60, 62, 34, 61, 39};
		for(int i = 0; i < charsRequiringEncoding.length - 1; i++) {
			text = text.replaceAll(String.valueOf((char)charsRequiringEncoding[i]),"&#"+charsRequiringEncoding[i]+";");
		}
		return text; 
	}
	
}