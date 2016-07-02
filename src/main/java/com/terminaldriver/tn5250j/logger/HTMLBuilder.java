package com.terminaldriver.tn5250j.logger;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.terminaldriver.tn5250j.TerminalDriver;

import lombok.Getter;
import lombok.Setter;

public class HTMLBuilder {
	
	List<HTMLLogInfo> infos = new ArrayList<HTMLLogInfo>();

	final Writer writer;
	public HTMLBuilder(Writer writer) throws IOException{
		Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
		Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		Velocity.init();
		this.writer=writer;
	}
	
	public void logScreen(final TerminalDriver driver) throws IOException{
		logScreen(driver,null);
	}
	public void logScreen(final TerminalDriver driver,String notes) throws IOException{
		infos.add(new HTMLLogInfo(HTMLLogger.getHTML(driver.getSession().getScreen()),notes));
	}
	
	public void close() throws IOException{
		VelocityContext context = new VelocityContext();
		context.put("info", infos);
		Velocity.mergeTemplate("/com/terminaldriver/tn5250j/logger/HTMLLogger.template.html", "UTF-8", context, writer );
		writer.close();
	}
	
	public static class HTMLLogInfo{
		@Getter
		public final String screenHtml;
		@Getter
		public final String logText;
		@Getter
		@Setter
		public String testName;
		
		public HTMLLogInfo(String screenHtml, String logText) {
			super();
			this.screenHtml = screenHtml;
			this.logText = logText;
		}
	}
}
