package com.terminaldriver.common.logger;

import java.io.IOException;
import java.io.Writer;

import com.terminaldriver.common.logger.HTMLBuilder.HTMLLogInfo;
import com.terminaldriver.tn5250j.TerminalDriver;

public class HTMLLogChangeListener extends LogChangeListener {

	final HTMLBuilder htmlBuilder;

	public HTMLLogChangeListener(final Writer writer) throws IOException {
		this(writer, false);
	}

	public HTMLLogChangeListener(final Writer writer, final boolean verbose) throws IOException {
		super(verbose);
		htmlBuilder = new HTMLBuilder(writer);
	}

	@Override
	protected String renderScreen(final TerminalDriver driver) {
		return HTMLLogger.getHTML(driver.getSession().getScreen());
	}

	@Override
	public void close() throws IOException {
		htmlBuilder.close();
	}

	@Override
	void addLog(final HTMLLogInfo info, final boolean verbose) {
		htmlBuilder.addLog(info, verbose);
	}

}
