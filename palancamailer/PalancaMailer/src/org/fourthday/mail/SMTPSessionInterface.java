package org.fourthday.mail;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;

public interface SMTPSessionInterface {
	public Session getSession() throws NoSuchProviderException;
	public String getHost();
	public String getUserName();
	public String getPswd();
}
