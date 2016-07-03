package com.terminaldriver.common.logger;

import java.io.IOException;
import java.io.Writer;

import com.terminaldriver.common.TerminalDriverChangeListener;
import com.terminaldriver.common.logger.HTMLBuilder.HTMLLogInfo;
import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenField;

public class HTMLLogChangeListener implements TerminalDriverChangeListener {

	final HTMLBuilder htmlBuilder;
	HTMLLogInfo info = null;
	private final boolean verbose;

	public HTMLLogChangeListener(final Writer writer) throws IOException {
		this(writer, false);
	}

	public HTMLLogChangeListener(final Writer writer, final boolean verbose) throws IOException {
		htmlBuilder = new HTMLBuilder(writer);
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
			final String newHtmlScreen = HTMLLogger.getHTML(driver.getSession().getScreen());
			if (info != null) {
				if (info.getScreenHtml().equals(newHtmlScreen)) {
					info.addText("Screen updated.");
					return;
				}
				htmlBuilder.addLog(info);
			}
			info = new HTMLLogInfo(newHtmlScreen, null);

		} else {
			if (info != null) {
				info.addText("Screen updated.");
			}
		}
	}

	public void screenChanged(final TerminalDriver driver) {
		if (info != null) {
			htmlBuilder.addLog(info);
		}
		info = new HTMLLogInfo(HTMLLogger.getHTML(driver.getSession().getScreen()), null);
	}

	public void close() throws IOException {
		htmlBuilder.close();
	}

}
