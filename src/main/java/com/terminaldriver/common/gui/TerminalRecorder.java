package com.terminaldriver.common.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.tn5250j.Session5250;
import org.tn5250j.SessionPanel;
import org.tn5250j.framework.tn5250.Rect;

import com.terminaldriver.common.TerminalDriverChangeListener;
import com.terminaldriver.common.logger.BufferingTDChangeListener;
import com.terminaldriver.common.logger.HTMLLogChangeListener;
import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.By;
import com.terminaldriver.tn5250j.obj.ScreenElement;
import com.terminaldriver.tn5250j.obj.ScreenField;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;
import com.terminaldriver.tn5250j.util.ScreenFieldReader;

import static com.terminaldriver.tn5250j.util.ScreenUtils.*;

 import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader; 
 
public class TerminalRecorder {

	JFrame frame;
	private JTextArea textField;
	private JPanel buttonPanel;
	private JButton recordButton;
	private JButton stopButton;
	private JButton identifyButton;
	private JButton clearButton;
	private JButton loggingButton;
	private JTextField notesField;
	private SessionPanel sessionPanel;
	private TerminalDriver terminalDriver;
	TerminalDriverChangeListener htmlLogChangeListener;
	JFileChooser fileChooser = new JFileChooser();
	
	public TerminalRecorder(){
		frame = new JFrame();
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new TerminalWindowListener());
		
		frame.setLayout(new BorderLayout());
		textField = new JTextArea();
		JScrollPane scrPane = new JScrollPane(textField);
		frame.add(scrPane,BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		frame.add(bottomPanel,BorderLayout.SOUTH);
		buttonPanel=new JPanel();
		bottomPanel.add(buttonPanel,BorderLayout.SOUTH);
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		c.fill = java.awt.GridBagConstraints.HORIZONTAL;
		GridBagLayout gblo = new GridBagLayout();
		c.weightx = 1.0;
		//gblo.setConstraints(c, constraints);
		JPanel notesPanel = new JPanel(gblo);
		
		notesPanel.add(new JLabel("Note:"),c);
		notesField = new JTextField(30);
		notesPanel.add(notesField,c);
		notesField.addActionListener(new NotesFieldListener());
		bottomPanel.add(notesPanel,BorderLayout.CENTER);
		
		recordButton = new JButton("Record");
		clearButton = new JButton("Clear");
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		identifyButton = new JButton("Identify");
		loggingButton = new JButton("Log");
		buttonPanel.add(recordButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(identifyButton);
		buttonPanel.add(loggingButton);
		buttonPanel.add(clearButton);
		recordButton.addActionListener(new RecordListener());
		stopButton.addActionListener(new StopListener());
		identifyButton.addActionListener(new IdentifyListener());
		clearButton.addActionListener(new ClearListener());
		loggingButton.addActionListener(new LogListener());
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileFilter(new FileFilter() {

			   public String getDescription() {
			       return "HTML (*.html)";
			   }

			   public boolean accept(File f) {
			       if (f.isDirectory()) {
			           return true;
			       } else {
			           String filename = f.getName().toLowerCase();
			           return filename.endsWith(".html") || filename.endsWith(".htm") ;
			       }
			   }
			});
			
	}
	
	public void startGUI(){
		frame.setVisible(true);
	}
	
	public static TerminalRecorder listen(SessionPanel sessionPanel){
		TerminalRecorder retval = listen(sessionPanel.getSession());
		retval.sessionPanel = sessionPanel;
		return retval;
	}	
	public static TerminalRecorder listen(Session5250 session){
		TerminalRecorder recorder = new TerminalRecorder();
		recorder.terminalDriver = new TerminalDriver();
		recorder.terminalDriver.setSession(session);
		recorder.terminalDriver.addTerminalDriverChangeListener(recorder.createListener());
		recorder.startGUI();
		return recorder;
	}
	
	public TerminalDriverChangeListener createListener(){
		return new BufferingTDChangeListener(new GUITerminalDriverChangeListener());
	}
	
	class RecordListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			stopButton.setEnabled(true);
			recordButton.setEnabled(false);
			textField.setText("import static com.terminaldriver.tn5250j.Assert.assertBy;");
			addText("public class DriverTest {");
			addText("");
			addText("    @Test");
			addText("    public void testDriver() throws Exception {");
			addText("        final TerminalDriver driver = new TerminalDriver();");
			addText("        driver.connectTo(\"0.0.0.0\", 23);");
		}
		
	}
	class StopListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			stopButton.setEnabled(false);
			recordButton.setEnabled(true);
			addText("    }");
			addText("}");
		}
		
	}
	class IdentifyListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			final Rect area = sessionPanel.getBoundingArea();
			final String textcontent = sessionPanel.getScreen().copyText(area);
			addText("        //Confirm screen contents");
			if(textcontent.length()<200){
				Field fieldX;
				Field fieldY;
				try {
					fieldX = Rect.class.getDeclaredField("x");
					fieldX.setAccessible(true);
					fieldY = Rect.class.getDeclaredField("y");
					fieldY.setAccessible(true);
					int x = fieldX.getInt(area);
					int y = fieldY.getInt(area);
					BufferedReader reader = new BufferedReader(new StringReader(textcontent));
					String line = null;
					while((line = reader.readLine()) != null){
						addText(String.format("        assertBy(By.position(%s,%s).and(By.text(\"%s\")),driver);",x,y,line));
						y+=1;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}else{
				int lastPos = sessionPanel.getScreen().getLastPos();
				ScreenField field = terminalDriver.getScreenFieldAt(lastPos);
				if (field != null){
					//textField.setText(textField.getText() + "field:" + field.getUnderlyingScreenField().toString() + "\n");
					addText(String.format("assertBy(By.row(%s).and(By.text(\"%s\")),driver);",field.startRow(),field.getString()));
				}else{
					ScreenElement x = terminalDriver.findElementByPosition(pos2row(lastPos,sessionPanel.getScreen().getColumns()), pos2col(lastPos,sessionPanel.getScreen().getColumns()), null);
					//textField.setText(textField.getText() + "field:" + x.toString() + "\n");
					addText(String.format("assertBy(By.position(%s,%s).and(By.text(\"%s\")),driver);",x.startRow(),x.startCol(),x.getString()));
				}
			}
		}
		
	}
	class ClearListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			textField.setText("");
		}
		
	}
	class LogListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if (loggingButton.getText().equals("Log")){
				int result = fileChooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					loggingButton.setText("End Log");
					File selectedFile = fileChooser.getSelectedFile();
					if (!selectedFile.getName().endsWith(".html") && !selectedFile.getName().endsWith(".htm")){
						selectedFile = new File(selectedFile.getAbsolutePath() + ".html");
					}
					try {
						htmlLogChangeListener = new BufferingTDChangeListener(new HTMLLogChangeListener(
								new FileWriter(selectedFile), false));
						terminalDriver.addTerminalDriverChangeListener(htmlLogChangeListener);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			else{
				try {
					htmlLogChangeListener.screenChanged(terminalDriver);
					//terminalDriver.closeListeners();
					((Closeable)htmlLogChangeListener).close();
					terminalDriver.removeTerminalDriverChangeListener(htmlLogChangeListener);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				htmlLogChangeListener=null;
				loggingButton.setText("Log");
			}
		}
		
	}
	
	class TerminalWindowListener extends WindowAdapter{

		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			e.getWindow().dispose();
			if(terminalDriver != null)
			try {
				terminalDriver.closeListeners();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	void addText(String text){
		textField.append("\r\n" + text);
	}

	class GUITerminalDriverChangeListener implements TerminalDriverChangeListener{

		public void fieldSetString(TerminalDriver driver, ScreenField screenField, String value) {
			addText("Enter field text:" + value);
		}

		public void sendKeys(TerminalDriver driver, String keys) {
			addText("        driver.sendKeys(\"" + keys + "\");");
		}

		public void screenSizeChanged(TerminalDriver driver, int cols, int rows) {
			addText(String.format("        //Screen size changed to %s x %s:",rows,cols));
		}

		public void screenPartialsUpdate(TerminalDriver driver, int row1, int col1, int row2, int col2) {
			//addText("Screen updated");
		}

		public void screenChanged(TerminalDriver driver) {
			String nameId= identifyScreenName(driver);
			if(nameId != null){
				addText("        //" + nameId);
			}
			addText("        driver.waitForScreen(5000);");
			addText("        driver.waitForInputNotInhibited(2000);\n");
		}

		private String identifyScreenName(TerminalDriver driver) {
			final ScreenFieldReader reader = new ScreenFieldReader(driver);
			ScreenTextBlock field = null;
			while ((field = reader.readField()) != null && field.startRow() <=3 ) {
				if (field.getString() != null && field.getString().trim().length() >= 10) {
					return field.getString();
				}
			}
			return null;
		}

		public void note(String note) {
			addText("//" + note);
		}

		public void inputInhibited(boolean inhibited) {
			//if(!inhibited){
			//	addText("        driver.waitForInputNotInhibited(2000);");
			//}
		}
		
		public void cursorMoved(TerminalDriver driver, int row, int col) {
			addText(String.format("        driver.getSession().getScreen().setCursor(%s, %s);",row,col));
		}
		
	}
	
	class NotesFieldListener implements ActionListener{

		public void actionPerformed(ActionEvent event) {
			String note = notesField.getText();
			notesField.setText("");
			if(htmlLogChangeListener != null){
				htmlLogChangeListener.note(note);
			}
			addText("         //" + note);
		}
		
	}
}
