package com.terminaldriver.common.logger;

import java.io.Closeable;
import java.io.IOException;

import com.terminaldriver.common.TerminalDriverChangeListener;
import com.terminaldriver.common.logger.HTMLBuilder.HTMLLogInfo;
import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenField;

public abstract class LogChangeListener implements TerminalDriverChangeListener,Closeable {

	HTMLLogInfo info;
	final boolean verbose;
	boolean screenChangePending;

	public LogChangeListener() throws IOException {
		this(false);
	}

	public LogChangeListener(final boolean verbose) throws IOException {
		this.verbose = verbose;
	}

	public void fieldSetString(final TerminalDriver driver, final ScreenField screenField, final String value) {
		if (info != null) {
			info.addText("Enter field text:" + value);
		}
	}

	public void sendKeys(final TerminalDriver driver, final String keys) {
		if (info != null) {
			info.addText("Send Keys:" + keys);
		}
	}

	public void screenSizeChanged(final TerminalDriver driver, final int cols, final int rows) {
		if (info != null) {
			info.addText(String.format("Screen size changed to %sx%s:", cols, rows));
		}
	}

	public void screenPartialsUpdate(final TerminalDriver driver, final int row1, final int col1, final int row2,
			final int col2) {

		if (verbose) {
			final String screenString = renderScreen(driver);
			if (info != null) {
				if (info.getScreenHtml().equals(screenString)) {
					info.addText("Screen updated." + (driver.acceptingInput() ? "Accepting input" : "Input inhibited"));
					return;
				}
				try {
					addLog(info, verbose);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			info = new HTMLLogInfo(screenString, null);
		}
		// If the screen was not accepting input when it changed, replace the
		// log with when it does.
		else if (screenChangePending && driver.acceptingInput()) {
			screenChangePending = false;
			info = new HTMLLogInfo(renderScreen(driver), info.getLogText());
		} else {
			if (info != null) {
				info.addText("Screen updated." + (driver.acceptingInput() ? "Accepting input" : "Input inhibited"));
			}
		}
	}

	abstract void addLog(HTMLLogInfo info, boolean verbose) throws IOException;

	public void screenChanged(final TerminalDriver driver) {
		// If a screen change is happening under input inhibited, you can assume
		// is a single screen, and not multiples.
		// Verbose prints all the steps.
		if (!verbose && screenChangePending) {
			info = new HTMLLogInfo(renderScreen(driver), info.getLogText());
			screenChangePending = !driver.acceptingInput();
			return;
		}
		if (info != null) {
			try {
				addLog(info, verbose);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		info = new HTMLLogInfo(renderScreen(driver), null);
		screenChangePending = !driver.acceptingInput();
		// info.addText((driver.acceptingInput()?"Accepting input":"Input
		// inhibited"));

	}

	protected abstract String renderScreen(TerminalDriver driver);

	public abstract void close() throws IOException;

	public void note(final String note) {
		if (info != null) {
			info.addText(note);
		}
	}

}
