package org.fourthday;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.fourthday.bean.EmailBean;
import org.fourthday.mail.SMTPGmailSession;
import org.fourthday.mail.SMTPSession;

public class PalancaManager {
	private SMTPSession smtpSession;
	private static final String EMAILFILE = "emailList.txt";
	private List<EmailBean> emailList = new ArrayList<EmailBean>();


	public PalancaManager() {
		this.loadEmailList();
		smtpSession = new SMTPGmailSession(); //using charter.net email
	}
	public static void main(String[] args) {
		PalancaManager pm = new PalancaManager();
		int counter = 1;
		try {
			Session session = pm.smtpSession.getSession();
			Transport transport = session.getTransport("smtp");
			transport.connect(pm.smtpSession.getHost(), pm.smtpSession.getUserName(), pm.smtpSession.getPswd());

			for(EmailBean email:pm.emailList) {
				System.out.println(counter+". set up email and attachment for "+email.toString());
				Message message = pm.getMessage(email);
				InternetAddress[] toAddresses = { new InternetAddress(email.getToAddress()) };
				transport.sendMessage(message, toAddresses);
				System.out.println("     ==> sent message.");
				if (counter % 20 == 0) {
					System.out.println(".... closing and reconnecting....please wait.....");
					transport.close();
					Thread.sleep(15000);
					transport.connect(pm.smtpSession.getHost(), pm.smtpSession.getUserName(), pm.smtpSession.getPswd());
				}
				counter++;
			}
			transport.close();
			System.out.println("** Job Completed Successfully.  **");

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected Message getMessage(EmailBean email) throws MessagingException {
		// creates a new e-mail message
		Message message = null;
		try {
			Session session = smtpSession.getSession();
	       // creates a new e-mail message
	        message = new MimeMessage(session);
	 
	        message.setFrom(new InternetAddress(smtpSession.getUserName()));
	        InternetAddress[] toAddresses = { new InternetAddress(email.getToAddress() ) };
	        message.setRecipients(Message.RecipientType.TO, toAddresses);
	        message.setSubject(email.getSubject());
	        message.setSentDate(new Date());
	        
	        // Create a multipart message
	        Multipart multipart = new MimeMultipart();
	      
	        // Create the message part
	        BodyPart messageBodyPart = new MimeBodyPart();
	        //set the actual message
	        messageBodyPart.setContent(EmailBean.getEmailMessage(), "text/html");
	        // add text to multipart
	        multipart.addBodyPart(messageBodyPart);

	        // Part two is attachment
	        messageBodyPart = new MimeBodyPart();
	        String filename = "C:\\Users\\Rich\\PalancaFromWorcesterMA.pdf";

	        if (email.getLanguage().equals("Spanish")) {
	        	filename = "C:\\Users\\Rich\\PalancaDeWorcesterMA.pdf";
	        }
	        DataSource source = new FileDataSource(filename);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(filename);
	        multipart.addBodyPart(messageBodyPart);

	        // Send the complete message parts
	        message.setContent(multipart);

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


	/**
	 * read the palanca pdf
	 * @return the lines that have mailto: references
	 */
	protected void loadEmailList(){
		try (BufferedReader br = new BufferedReader(new FileReader(EMAILFILE))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.equalsIgnoreCase("STOP"))
					break;
				EmailBean email = new EmailBean(sCurrentLine);
				emailList.add(email);
			}
			System.out.println("emailList loaded with "+emailList.size()+" rows.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
