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
import com.terminaldriver.common.logger.HTMLLogChangeListener;
import com.terminaldriver.common.logger.KeyBufferingTDChangeListener;
import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.obj.ScreenElement;
import com.terminaldriver.tn5250j.obj.ScreenField;

 import static com.terminaldriver.tn5250j.util.ScreenUtils.*;

 import javax.swing.JFileChooser;
 import java.io.File;
import java.io.FileWriter;
import java.io.IOException; 
 
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
		return new KeyBufferingTDChangeListener(new GUITerminalDriverChangeListener());
	}
	
	class RecordListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			stopButton.setEnabled(true);
			recordButton.setEnabled(false);
			textField.setText(textField.getText() + "Record\n");
		}
		
	}
	class StopListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			stopButton.setEnabled(false);
			recordButton.setEnabled(true);
			textField.setText(textField.getText() + "Stop\n");
		}
		
	}
	class IdentifyListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			final Rect area = sessionPanel.getBoundingArea();
			final String textcontent = sessionPanel.getScreen().copyText(area);
			if(textcontent.length()<200){
				textField.setText(textField.getText() + "id:" +  textcontent + "\n");
			Field fieldX;
			try {
				fieldX = Rect.class.getDeclaredField("x");
				fieldX.setAccessible(true);
				textField.setText( textField.getText() + "rect.x:" +  fieldX.getInt(area)+ "\n");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			}else{
				int lastPos = sessionPanel.getScreen().getLastPos();
				ScreenField field = terminalDriver.getScreenFieldAt(lastPos);
				if (field != null){
					textField.setText(textField.getText() + "field:" + field.getUnderlyingScreenField().toString() + "\n");
				}else{
					ScreenElement x = terminalDriver.findElementByPosition(pos2row(lastPos,sessionPanel.getScreen().getColumns()), pos2col(lastPos,sessionPanel.getScreen().getColumns()), null);
					textField.setText(textField.getText() + "field:" + x.toString() + "\n");
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
					htmlLogChangeListener = new KeyBufferingTDChangeListener(new HTMLLogChangeListener(
							new FileWriter(selectedFile), false));
					//Save the current screen
					htmlLogChangeListener.screenChanged(terminalDriver);
					terminalDriver.addTerminalDriverChangeListener(htmlLogChangeListener);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			}
			else{
				try {
					terminalDriver.closeListeners();
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

		StringBuilder keyBuffer= new StringBuilder();
		boolean bufferKeys = true;
		
		
		public void fieldSetString(TerminalDriver driver, ScreenField screenField, String value) {
			addText("Enter field text:" + value);
		}

		public void sendKeys(TerminalDriver driver, String keys) {
			addText("Send Keys:" + keyBuffer);
		}

		public void screenSizeChanged(TerminalDriver driver, int cols, int rows) {
			addText(String.format("Screen size changed to %s x %s:",rows,cols));
		}

		public void screenPartialsUpdate(TerminalDriver driver, int row1, int col1, int row2, int col2) {
			//addText("Screen updated");
		}

		public void screenChanged(TerminalDriver driver) {
			addText("Screen changed");
		}

		public void note(String note) {
			addText("//" + note);
		}

		public void inputInhibited(boolean inhibited) {
			if(inhibited){
				addText("inhibited, wait for not inhibited");
			}
			if(!inhibited){
				addText("not inhibited");
			}
		}
		
		public void cursorMoved(TerminalDriver driver, int row, int col) {
			addText(String.format("Move cursor To %sx%s",row,col));
		}
		
	}
	
	class NotesFieldListener implements ActionListener{

		public void actionPerformed(ActionEvent event) {
			String note = notesField.getText();
			notesField.setText("");
			if(htmlLogChangeListener != null){
				htmlLogChangeListener.note(note);
			}
			addText("//" + note);
		}
		
	}
}
