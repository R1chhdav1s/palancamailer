package org.fourthday;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.fourthday.bean.EmailBean;
 
public class AttachmentMailer {
	private static final String EMAILFILE = "emailList.txt";
	private List<EmailBean> emailList = new ArrayList<EmailBean>();
    public void sendHtmlEmail(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String message) throws AddressException,
            MessagingException {
 
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
 
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        
        // Create a multipart message
        Multipart multipart = new MimeMultipart();
      
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();
        //set the actual message
        messageBodyPart.setContent(message, "text/html");
        // add text to multipart
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        String filename = "C:\\Users\\Rich\\PalancaFromWorcesterMA.pdf";
        String spanishfilename = "C:\\Users\\Rich\\PalancaDeWorcesterMA.pdf";
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        msg.setContent(multipart);

        
        
        // sends the e-mail
        Transport.send(msg);
 
    }
    
    
	/**
	 * read the palanca pdf
	 * @return the lines that have mailto: references
	 */
	public void emailList(){
		try (BufferedReader br = new BufferedReader(new FileReader(EMAILFILE))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				EmailBean email = new EmailBean(sCurrentLine);
				emailList.add(email);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

 
    /**
     * Test the send html e-mail method
     *
     */
    public static void main(String[] args) {
    	
        // SMTP server information
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "r1chhdav1s@gmail.com";
        String password = "dtO122LHO";
 
 
        // message contains HTML markups
        String message = "<b>Blessings!</b><br>";
        message += "Attached is our Palanca Letter, along with which we send all<br>";
        message += "our prayers and sacrifices for your upcoming weekend!<br>";
        message += "Your Brothers and Sisters in Christ,<br><br>";
       message += "The Catholic Cursillo Community in Worcester, Massachusetts ";
          
      
        AttachmentMailer mailer = new AttachmentMailer();
    	mailer.emailList();
    	
    	for (EmailBean email: mailer.emailList ) {
            // outgoing message information
            String mailTo = email.getToAddress();
            String subject = email.getSubject();

            try {
                mailer.sendHtmlEmail(host, port, mailFrom, password, mailTo,
                        subject, message);
                System.out.println("Email sent to "+mailTo+ " for "+subject);
            } catch (Exception ex) {
                System.out.println("Failed to sent email.");
                ex.printStackTrace();
            }
   		
    	}

    }
}