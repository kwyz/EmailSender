package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.io.*;
import java.util.Properties;
import java.util.Date;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.*;


class SMTPMethods {

	userAccountEntire uAE = new userAccountEntire();
	
	String giver = null;
	String mesageTitle = null;
	String mesageContent = null;
	File file;
	ArrayList<String> users = new ArrayList<String>();
	
	PropertiesLoadWrite psw = new PropertiesLoadWrite();
	
	public SMTPMethods() {}
	public SMTPMethods(String username,String messageTitle, String messageContent, ArrayList<String> receivers,File file) throws AddressException, SQLException, MessagingException {
		this.giver = username;
		this.mesageTitle = messageTitle;
		this.mesageContent = messageContent;
		this.users = receivers;
		this.file = file;
		buildMessageWithFile(file);
	}
	public SMTPMethods(String username,String messageTitle, String messageContent, ArrayList<String> receivers) throws AddressException, SQLException, MessagingException {
		this.giver = username;
		this.mesageTitle = messageTitle;
		this.mesageContent = messageContent;
		this.users = receivers;
		buildMessage();
	}
	
	
	public String getSMTPServer(String email) {
		String[] smtserver = email.split("@");
		return smtserver[1];
	}
	
	public String getEmail(String username) throws SQLException {
		return new DBMethods().getUserEmail(username);
	}
	

	
	
	public void buildMessage() throws SQLException, AddressException, MessagingException {
		Properties properties = System.getProperties();
		properties.put("mail.smtps.host", "smtp."+getSMTPServer(psw.loadProperties("smtpEmail")));
		properties.put("mail.smtps.auth", "true");
		Session session = Session.getInstance(properties, null);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(psw.loadProperties("smtpEmail")));
		for(int i = 0; i < users.size(); i++) {			message.setRecipients(Message.RecipientType.TO, 
					InternetAddress.parse(getEmail(users.get(i))));
			message.setSubject(giver+" "+mesageTitle);
			message.setText(mesageContent);
			message.setSentDate(new Date());
			SMTPTransport transport = (SMTPTransport)session.getTransport("smtps");
			transport.connect("smtp."+getSMTPServer(psw.loadProperties("smtpEmail")),
					psw.loadProperties("smtpEmail"),psw.loadProperties("smtpPass"));
			transport.sendMessage(message, message.getAllRecipients());

			
			
			transport.close();
			
		}
	}
		public void buildMessageWithFile(File file) throws SQLException, AddressException, MessagingException {
			Properties properties = System.getProperties();
			properties.put("mail.smtps.host", "smtp."+getSMTPServer(psw.loadProperties("smtpEmail")));
			properties.put("mail.smtps.auth", "true");
			Session session = Session.getInstance(properties, null);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(psw.loadProperties("smtpEmail")));
			for(int i = 0; i < users.size(); i++) {

				message.setRecipients(Message.RecipientType.TO, 
						InternetAddress.parse(getEmail(users.get(i))));
				message.setSubject(giver+" "+mesageTitle);
				message.setText(mesageContent);
				message.setSentDate(new Date());
				SMTPTransport transport = (SMTPTransport)session.getTransport("smtps");
				transport.connect("smtp."+getSMTPServer(psw.loadProperties("smtpEmail")),
						psw.loadProperties("smtpEmail"),psw.loadProperties("smtpPass"));
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText("File:");
		        Multipart multipart = new MimeMultipart();
		        multipart.addBodyPart(messageBodyPart);
		        messageBodyPart = new MimeBodyPart();
		        DataSource source = new FileDataSource(file.getAbsolutePath());
		        messageBodyPart.setDataHandler(new DataHandler(source));
		        messageBodyPart.setFileName(file.getName());
		        multipart.addBodyPart(messageBodyPart);
		        message.setContent(multipart);
		        
				transport.sendMessage(message, message.getAllRecipients());

				
				
				transport.close();
				
			}
		
	}
}
