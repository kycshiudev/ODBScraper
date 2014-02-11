import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class SwingMenu extends JPanel implements ActionListener {
	public SwingMenu() {
        super(new GridBagLayout());
 
        initializeFileDialogs();
        initializeUIComponents();
        arrangeUIComponents();
    }
	
	/* Determine which event was triggered and call the correct function.
	 */
	public void actionPerformed(ActionEvent evt) {
		String actionCommand = evt.getActionCommand();
		
		if (actionCommand.equals("browseTemplateButton")) {
			String pathToTemplate = browseForFile();
			templateField.setText(pathToTemplate);
		}
		else if (actionCommand.equals("browseOutfileButton")) {
			String pathToOutfile = browseForFileToSave();
			outfileField.setText(pathToOutfile);
		}
		else if (actionCommand.equals("okButton")) {
			generate();
		}
    }
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ODB Devotional Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        frame.add(new SwingMenu());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /* Initializes save and load file dialogs
     */
    private void initializeFileDialogs() {
    	loadFileDialog = new FileDialog((JFrame)SwingUtilities.getRoot(this), "Choose a file", FileDialog.LOAD);
        loadFileDialog.setDirectory("C:\\");
        loadFileDialog.setFile("*.rtf");
        loadFileDialog.setVisible(false);
        
        saveFileDialog = new FileDialog((JFrame)SwingUtilities.getRoot(this), "Choose a file", FileDialog.SAVE);
        saveFileDialog.setDirectory("C:\\");
        saveFileDialog.setFile("*.rtf");
        saveFileDialog.setVisible(false);
    }
    
    /* Initialize component properties, but does not place them on the grid
     */
    private void initializeUIComponents() {
    	// call constructors
        urlLabel = new JLabel(URL_FIELD_LABEL);
        templateLabel = new JLabel(TEMPLATE_FIELD_LABEL);
        outfileLabel = new JLabel(OUTFILE_FIELD_LABEL);
        statusLabel = new JLabel(" ");
        
        urlField = new JTextField(FIELD_LENGTH);
        templateField = new JTextField(FIELD_LENGTH);
        outfileField = new JTextField(FIELD_LENGTH);
        
        browseTemplateButton = new JButton("browse");
        browseOutfileButton = new JButton("browse");
        okButton = new JButton("OK");
        
        // set other properties
        urlLabel.setLabelFor(urlField);
        templateLabel.setLabelFor(templateField);
        outfileLabel.setLabelFor(outfileField);
        
        browseTemplateButton.setActionCommand("browseTemplateButton");
        browseTemplateButton.addActionListener(this);
        browseOutfileButton.setActionCommand("browseOutfileButton");
        browseOutfileButton.addActionListener(this);
        okButton.setActionCommand("okButton");
        okButton.addActionListener(this);
    }
    
    /* Place UI components on the grid
     */
    private void arrangeUIComponents() {
    	//Define constraints
    	GridBagConstraints urlFieldConstraint = new GridBagConstraints();
        urlFieldConstraint.gridwidth = GridBagConstraints.REMAINDER;
        urlFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
    	
        GridBagConstraints textFieldConstraint = new GridBagConstraints();
        textFieldConstraint.weightx = 1.0;
        textFieldConstraint.gridwidth = GridBagConstraints.RELATIVE;
        textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        
        GridBagConstraints browseButtonConstraint = new GridBagConstraints();
        browseButtonConstraint.gridwidth = GridBagConstraints.REMAINDER;
        browseButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        
        GridBagConstraints okButtonConstraint = new GridBagConstraints();
        okButtonConstraint.fill = GridBagConstraints.NONE;
        okButtonConstraint.anchor = GridBagConstraints.CENTER;
        okButtonConstraint.gridwidth = GridBagConstraints.REMAINDER;
        
        //Add components to grid
        add(urlLabel, urlFieldConstraint);
        add(urlField, urlFieldConstraint);
        
        add(templateLabel, urlFieldConstraint);
        add(templateField, textFieldConstraint);
        add(browseTemplateButton, browseButtonConstraint);
        
        add(outfileLabel, urlFieldConstraint);
        add(outfileField, textFieldConstraint);
        add(browseOutfileButton, browseButtonConstraint);
        
        add(okButton, okButtonConstraint);
        
        add(statusLabel, urlFieldConstraint);
    }
    
    /* Opens a file dialog for getting a file and returns the full path to it
     */
    private String browseForFile() {
    	loadFileDialog.setVisible(true);
    	String filepath = loadFileDialog.getDirectory()+loadFileDialog.getFile();
    	
        if (loadFileDialog.getFile() == null) {
        	System.out.println("You cancelled the choice");
        	filepath = null;
        }
        else {
        	System.out.println("You chose " + filepath);
        }
        
        return filepath;
    }
    
    /* Opens a file dialog for getting a file and returns the full path to it
     */
    private String browseForFileToSave() {
    	saveFileDialog.setVisible(true);
    	String filepath = saveFileDialog.getDirectory()+saveFileDialog.getFile();
    	
        if (saveFileDialog.getFile() == null) {
        	System.out.println("You cancelled the choice");
        	filepath = null;
        }
        else {
        	System.out.println("You chose " + filepath);
        }
        
        return filepath;
    }
    
    /* Gets information from all fields and passes them to ODBScraper.
     * Please check that all fields are filled and valid first.
     */
    private void generate() {
    	String urlText = urlField.getText();
    	String templateText = templateField.getText();
    	String outfileText = outfileField.getText();
        
        statusLabel.setText("Generating Your Devotional...");
        ODBScraper.generateWordDoc(urlText, templateText, outfileText);
        statusLabel.setText("DONE!");
    }
	
    // UI elements
    protected JLabel urlLabel;
    protected JLabel templateLabel;
    protected JLabel outfileLabel;
    
	protected JTextField urlField;
	protected JTextField templateField;
	protected JTextField outfileField;
	
	protected JButton browseTemplateButton;
	protected JButton browseOutfileButton;
    protected JButton okButton;
    
    protected JLabel statusLabel;
    protected FileDialog loadFileDialog;
    protected FileDialog saveFileDialog;
    
    // constants
    private static final String URL_FIELD_LABEL = "URL for Sunday's article";
    private static final String TEMPLATE_FIELD_LABEL = "Path to template";
    private static final String OUTFILE_FIELD_LABEL = "Output file name";
    
    private static final int FIELD_LENGTH = 35;
    
    private static final String TEXTBOX_TEMPLATE = ".\\templates\\textbox_rtf.rtf";
	private static final String NEWPAGE_TEMPLATE = ".\\templates\\newpage_rtf.rtf";
	private static final String OUTPUT_NAME = ".\\testout.doc";
}
