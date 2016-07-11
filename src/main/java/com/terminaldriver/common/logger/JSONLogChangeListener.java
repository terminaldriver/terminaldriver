package com.terminaldriver.common.logger;

import java.io.IOException;
import java.io.Writer;

import org.tn5250j.framework.tn5250.ScreenField;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.terminaldriver.common.logger.HTMLBuilder.HTMLLogInfo;
import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenDataContainer;

public class JSONLogChangeListener extends LogChangeListener {

	final JsonGenerator generator;

	public JSONLogChangeListener(final Writer writer) throws IOException {
		this(writer, false);
	}

	public JSONLogChangeListener(final Writer writer, final boolean verbose) throws IOException {
		super(verbose);
		final JsonFactory factory = new JsonFactory();
		generator = factory.createGenerator(writer).useDefaultPrettyPrinter();
		generator.writeStartArray();
	}

	@Override
	protected String renderScreen(final TerminalDriver driver) {
		final ScreenDataContainer container = new ScreenDataContainer(driver.getSession().getScreen());
		final StringBuilder sb = new StringBuilder();
		sb.append(container.text).append("\r\n");
		sb.append(container.attr).append("\r\n");
		sb.append(container.isAttr).append("\r\n");
		sb.append(container.color).append("\r\n");
		sb.append(container.extended).append("\r\n");
		sb.append(container.graphic).append("\r\n");
		sb.append(container.field).append("\r\n");
		boolean firstfield = true;
		for (final ScreenField fld : driver.getSession().getScreen().getScreenFields().getFields()) {
			if (!firstfield) {
				sb.append(",");
			}
			firstfield = false;
			sb.append("{\"string\":\"").append(fld.getString()).append("\"");
			sb.append(",\"fieldId\":").append(fld.getFieldId());
			sb.append(",\"attr\":").append(fld.getAttr());
			sb.append(",\"endPos\":").append(fld.endPos());
			sb.append(",\"startCol\":").append(fld.startCol());
			sb.append(",\"startRow\":").append(fld.startRow());
			sb.append(",\"startPos\":").append(fld.startPos());
			sb.append(",\"length\":").append(fld.getLength());
			sb.append(",\"fieldLength\":").append(fld.getFieldLength());
			sb.append(",\"FCW1\":").append(fld.getFCW1());
			sb.append(",\"FCW2\":").append(fld.getFCW2());
			sb.append(",\"FFW1\":").append(fld.getFFW1());
			sb.append(",\"FFW2\":").append(fld.getFFW2());
			sb.append(",\"fieldShift\":").append(fld.getFieldShift());
			sb.append(",\"highlightedAttr\":").append(fld.getHighlightedAttr());
			sb.append("}");
		}
		// sb.append("]");
		return sb.toString();
	}

	@Override
	public void close() throws IOException {
		generator.writeEndArray();
		generator.close();
	}

	@Override
	void addLog(final HTMLLogInfo info, final boolean verbose) throws IOException {
		generator.writeStartObject();
		final String[] screenInfo = info.getScreenHtml().split("\r\n");
		generator.writeStringField("text", screenInfo[0]);
		generator.writeStringField("attr", screenInfo[1]);
		generator.writeStringField("isAttr", screenInfo[2]);
		generator.writeStringField("color", screenInfo[3]);
		generator.writeStringField("extended", screenInfo[4]);
		generator.writeStringField("graphic", screenInfo[5]);
		generator.writeStringField("field", screenInfo[6]);
		generator.writeStringField("description", info.getLogText());
		if (screenInfo.length > 7) {
			generator.writeArrayFieldStart("fields");
			generator.writeRaw(screenInfo[7]);
			generator.writeEndArray();
		}
		generator.writeEndObject();
	}

}
