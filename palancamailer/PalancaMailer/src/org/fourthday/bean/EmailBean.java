package org.fourthday.bean;

public class EmailBean {
	private String toAddress;
	private String subject;
	private String language;
	
	public EmailBean(String inString) {
		super();
		int s = inString.indexOf("?");
		toAddress = inString.substring(0,s);
		subject = inString.substring(s+9);
		language = subject.contains("Spanish")? "Spanish" : "English";
	}
	
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("EmailBean:{").append("toAddress:").append(toAddress)
				.append(", subject:").append(subject).append(", language:").append(language).append("}");
		return sb.toString();
	}
	
	public static String getEmailMessage() {
		return emailMessage.toString();
	}
	private static final StringBuilder emailMessage = new StringBuilder("<h2>Blessings!</h2>")
			.append("<p>Attached is our Palanca Letter, along with which we send all<br>")
			.append("our prayers and sacrifices for your upcoming weekend!</p>")
			.append("<p>Your Brothers and Sisters in Christ,<br><br>")
			.append("The Catholic Cursillo Community in Worcester, Massachusetts</p>");
	
	
}
