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
import org.tn5250j.framework.tn5250.Rect;

import com.terminaldriver.common.TerminalDriverChangeListener;
import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.obj.ScreenElement;
import com.terminaldriver.tn5250j.obj.ScreenField;

 import static com.terminaldriver.tn5250j.util.ScreenUtils.*;

public class TerminalRecorder {

	JFrame frame;
	private JTextArea textField;
	private JPanel buttonPanel;
	private JButton recordButton;
	private JButton stopButton;
	private JButton identifyButton;
	private JButton clearButton;
	private SessionPanel sessionPanel;
	private TerminalDriver terminalDriver;
	
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
		clearButton = new JButton("Clear");
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		identifyButton = new JButton("Identify");
		buttonPanel.add(recordButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(identifyButton);
		buttonPanel.add(clearButton);
		recordButton.addActionListener(new RecordListener());
		stopButton.addActionListener(new StopListener());
		identifyButton.addActionListener(new IdentifyListener());
		clearButton.addActionListener(new ClearListener());
		
		
		
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
	
	class TerminalWindowListener extends WindowAdapter{

		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			
		}
		
	}
	
	class GUITerminalDriverChangeListener implements TerminalDriverChangeListener{

		StringBuilder keyBuffer= new StringBuilder();
		boolean bufferKeys = true;
		
		void addText(String text){
			textField.append("\r\n" + text);
		}
		
		public void fieldSetString(TerminalDriver driver, ScreenField screenField, String value) {
			addText("Enter field text:" + value);
		}

		public void sendKeys(TerminalDriver driver, String keys) {
			if(keys.length()==1){
				ScreenField field = driver.getScreenFieldAt(driver.getSession().getScreen().getLastPos());
				if(field != null && ScreenAttribute.getAttrEnum(field.getAttr().charAt(0)).isNonDisplay() ){
					keyBuffer.append("*");
				}else{
					keyBuffer.append(keys);
				}
			}else{
				keyBuffer.append(keys);
			}
			if(!bufferKeys || (keys.startsWith("[") && keys.endsWith("]"))){
				addText("Send Keys:" + keyBuffer);
				keyBuffer.setLength(0);
			}
		}

		public void screenSizeChanged(TerminalDriver driver, int cols, int rows) {
			addText(String.format("Screen size changed to %s x %s:",rows,cols));
		}

		public void screenPartialsUpdate(TerminalDriver driver, int row1, int col1, int row2, int col2) {
			//addText("Screen updated");
		}

		public void screenChanged(TerminalDriver driver) {
			flushKeys();
			addText("Screen changed");
		}

		public void note(String note) {
			addText("//" + note);
		}

		public void inputInhibited(boolean inhibited) {
			if(inhibited){
				flushKeys();
				addText("inhibited, wait for not inhibited");
			}
			if(!inhibited){
				flushKeys();
				addText("not inhibited");
			}
		}
		
		void flushKeys(){
			if(keyBuffer.length()>0){
				addText("Send Keys:" + keyBuffer);
				keyBuffer.setLength(0);
			}
		}

		public void cursorMoved(TerminalDriver driver, int row, int col) {
			addText(String.format("Move cursor To %sx%s",row,col));
		}
		
	}
}
