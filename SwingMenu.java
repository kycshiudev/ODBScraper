import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SwingMenu extends JPanel implements ActionListener {
	public SwingMenu() {
        super(new GridBagLayout());
 
        JLabel urlLabel = new JLabel(URL_FIELD_LABEL);
        JLabel templateLabel = new JLabel(TEMPLATE_FIELD_LABEL);
        JLabel outfileLabel = new JLabel(OUTFILE_FIELD_LABEL);
        statusLabel = new JLabel(" ");
        
        urlField = new JTextField(35);
        
        okButton = new JButton("OK");
        okButton.addActionListener(this);
 
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
 
        c.fill = GridBagConstraints.HORIZONTAL;
        add(urlLabel, c);
        add(urlField, c);
        add(templateLabel, c);
        add(outfileLabel, c);
        
        c.fill = GridBagConstraints.CENTER;
        add(okButton, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        add(statusLabel, c);
    }
	
	public void actionPerformed(ActionEvent evt) {
        String urlText = urlField.getText();
        
        statusLabel.setText("Generating Your Devotional...");
        ODBScraper.generateWordDoc(urlText, NEWPAGE_TEMPLATE, OUTPUT_NAME);
        statusLabel.setText("DONE!");
    }
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        frame.add(new SwingMenu());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	protected JTextField urlField;
	protected JTextField templateField;
	protected JTextField outfileField;
    protected JButton okButton;
    protected JLabel statusLabel;
    
    private static final String URL_FIELD_LABEL = "URL for Sunday's article";
    private static final String TEMPLATE_FIELD_LABEL = "Path to template";
    private static final String OUTFILE_FIELD_LABEL = "Output file name";
    
    private static final String TEXTBOX_TEMPLATE = ".\\templates\\textbox_rtf.rtf";
	private static final String NEWPAGE_TEMPLATE = ".\\templates\\newpage_rtf.rtf";
	private static final String OUTPUT_NAME = ".\\testout.doc";
}
