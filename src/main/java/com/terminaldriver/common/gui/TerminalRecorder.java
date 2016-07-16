package com.terminaldriver.common.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.tn5250j.Session5250;
import org.tn5250j.SessionPanel;
import org.tn5250j.SessionPanel.TNRubberBand;
import org.tn5250j.framework.tn5250.Rect;
import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.common.TerminalDriverChangeListener;
import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenField;

public class TerminalRecorder {

	JFrame frame;
	private JTextArea textField;
	private JPanel buttonPanel;
	private JButton recordButton;
	private JButton stopButton;
	private JButton identifyButton;
	private SessionPanel sessionPanel;
	
	public TerminalRecorder(){
		frame = new JFrame();
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new TerminalWindowListener());
		
		frame.setLayout(new BorderLayout());
		textField = new JTextArea();
		JScrollPane scrPane = new JScrollPane(textField);
		frame.add(scrPane,BorderLayout.CENTER);
		
		buttonPanel=new JPanel();
		frame.add(buttonPanel,BorderLayout.SOUTH);
		
		recordButton = new JButton("Record");
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		identifyButton = new JButton("Identify");
		buttonPanel.add(recordButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(identifyButton);
		recordButton.addActionListener(new RecordListener());
		stopButton.addActionListener(new StopListener());
		identifyButton.addActionListener(new IdentifyListener());
		
		
		
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
		TerminalDriver t = new TerminalDriver();
		t.setSession(session);
		TerminalRecorder recorder = new TerminalRecorder();
		t.addTerminalDriverChangeListener(recorder.createListener());
		recorder.startGUI();
		return recorder;
	}
	
	public GUITerminalDriverChangeListener createListener(){
		return new GUITerminalDriverChangeListener();
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
			//sessionPanel.getScreen().
			final String textcontent = sessionPanel.getScreen().copyText(area);
			if(textcontent.length()<200)
				textField.setText(textField.getText() + "id:" +  textcontent + "\n");
			else{
				try{
					Field field = Screen5250.class.getDeclaredField("lastPos");
					field.setAccessible(true);
					int lastPos = (Integer)field.get(sessionPanel.getScreen()) + 1;
					textField.setText(textField.getText() + "id:" +  lastPos + "\n");
				}catch(Exception ex){ex.printStackTrace();}
				
			}
		}
		
	}
	
	class TerminalWindowListener extends WindowAdapter{

		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			
		}
		
	}
	
	class GUITerminalDriverChangeListener implements TerminalDriverChangeListener{

		void addText(String text){
			textField.append("\r\n" + text);
		}
		
		public void fieldSetString(TerminalDriver driver, ScreenField screenField, String value) {
			addText("Enter field text:" + value);
		}

		public void sendKeys(TerminalDriver driver, String keys) {
			addText("Send Keys:" + keys);
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
		}
		
	}
}
