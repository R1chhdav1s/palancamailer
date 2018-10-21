package org.fourthday.mail;

import java.util.Properties;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import org.fourthday.mail.SMTPSessionInterface;

public class SMTPSession implements SMTPSessionInterface {
	protected Properties properties;
	protected Session session;
	protected String host = "";
	protected String port = "";
	protected String userName = "";
	protected String password = "";


	@Override
	public Session getSession() throws NoSuchProviderException {
		properties = new Properties();
		session = Session.getInstance(properties);
		return session;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public String getPswd() {
		return password;
	}

}
