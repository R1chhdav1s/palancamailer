package org.fourthday.mail;

import java.util.Properties;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;

public class SMTPGmailSession extends SMTPSession implements SMTPSessionInterface {
	private Properties properties;
	private Session session;
	
	public SMTPGmailSession() {
		super();
		init();
	}
	@Override
	public Session getSession() throws NoSuchProviderException {
		// sets SMTP server properties
		properties = new Properties();
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		session = Session.getInstance(properties);
		return session;
	}

	private void init() {
		// SMTP server information
		host = "smtp.gmail.com";
		port = "587";
		userName = "worcursillo@gmail.com";
		password = "2pZ64YlA7j";
		System.out.println("SMTPGmailSession.init() completed");
	}
	

}
