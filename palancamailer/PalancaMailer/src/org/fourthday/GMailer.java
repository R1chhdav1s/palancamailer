package org.fourthday;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.fourthday.bean.EmailBean;
import org.fourthday.mail.SMTPGmailSession;
import org.fourthday.mail.SMTPSession;
 
public class GMailer extends Mailer {
	SMTPSession smtpSession;
	
	public GMailer() {
		smtpSession = new SMTPGmailSession(); //testing with Gmail
	}
 
	@Override
    public void sendHtmlEmail(Message message, EmailBean email) throws AddressException,MessagingException {
    	Session session = smtpSession.getSession();
 
        // sends the e-mail
        Transport transport = session.getTransport("smtp");
        transport.connect(smtpSession.getHost(), smtpSession.getUserName(), smtpSession.getPswd());
        InternetAddress[] toAddresses = { new InternetAddress(email.getToAddress()) };
        transport.sendMessage(message,toAddresses);
        transport.close();
    }
 
    /**
     * Test the send html e-mail method
     *
     */
    public static void main(String[] args) {

		try {
			GMailer mailer = new GMailer();
			EmailBean email = new EmailBean("rich_davis@charter.net?subject=Palanca for Women's 85 (English)");
			Message message = mailer.getMessage(email);
			mailer.sendHtmlEmail(message, email);
			System.out.println("Email sent.");
		} catch (Exception ex) {
			System.out.println("Failed to sent email.");
			ex.printStackTrace();
		}
    }
    @Override
	protected Message getMessage(EmailBean email) throws MessagingException {
		// creates a new e-mail message
		Message message =null;
		try {
			Session session = smtpSession.getSession();
			message = new MimeMessage(session);

			message.setFrom(new InternetAddress(smtpSession.getUserName()));
			message.setSubject(email.getSubject());
			message.setSentDate(new Date());
			// set plain text message
			message.setContent(EmailBean.getEmailMessage(), "text/html");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MessagingException();
		}

		return message;
	}
}