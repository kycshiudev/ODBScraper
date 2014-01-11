import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* Use JSoup to get article information from ODB. Output formatted devotionals
 */
public class ODBScraper extends JPanel implements ActionListener{
	
	
	public static void generateWordDoc(String startURL, String templatePath, String outputPath) {
		ODBArticle currentArticle = new ODBArticle("http://odb.org/2014/01/12/a-neighbor-on-the-fence/");

		Hashtable ht = new Hashtable();	
		for (short day = 0; day < 7; day++) {
			ht.put("DATE"+day, currentArticle.pageDate());
			ht.put("TITLE"+day, currentArticle.page_title);
			ht.put("READ"+day, currentArticle.page_read);
			ht.put("VERSE"+day, currentArticle.page_verse);
			ht.put("PARAGRAPH"+day, currentArticle.pageParagraphs());
			ht.put("POEM"+day, currentArticle.page_poem);
			ht.put("THOUGHTBOX"+day, currentArticle.page_thought_box);
			ht.put("NEWPAGE", "{\\\\page}");

			currentArticle = new ODBArticle(currentArticle.next_page_url);
		}

		TemplateProcessor.templateReplace(ht, templatePath, outputPath);
		System.out.println("finish doc processing");
	}
	
	public ODBScraper() {
        super(new GridBagLayout());
 
        textField = new JTextField(20);
        textField.addActionListener(this);
 
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
 
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
 
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
 
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }
 
    public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
        textArea.append(text + newline);
        textField.selectAll();
 
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        frame.add(new ODBScraper());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	public static void main (String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
		
		String articleURL = "http://odb.org/2014/01/05/adoption/";
		String templatePathFilename = NEWPAGE_TEMPLATE;
		String outputPathFilename = OUTPUT_NAME;
		
		generateWordDoc(articleURL, templatePathFilename, outputPathFilename);
	}
	
	private static final String TEXTBOX_TEMPLATE = ".\\templates\\textbox_rtf.rtf";
	private static final String NEWPAGE_TEMPLATE = ".\\templates\\newpage_rtf.rtf";
	private static final String OUTPUT_NAME = ".\\testout.doc";
	
	protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";
}
