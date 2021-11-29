package com.lesstax.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {

	private static final Logger logger = LogManager.getLogger(SendEmail.class);
	
	@Autowired
	private SendOTP sendOTP;

	public Boolean sendOTPOnMail(String receiverMailId, String clientName) {

		logger.info("Inside sendOTPOnMail() of SendOTP ");
		final String username = "lesstax21@gmail.com";
		final String password = "#123Asd123";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("lesstax21@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverMailId));
			message.setSubject("For Email Verification");
			message.setText("Dear " +clientName +"," + "\n\n Your OTP is:- " + sendOTP.generateOTP(6));

			Transport.send(message);

			logger.info("Exiting from sendOTPOnMail() of SendOTP");
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("Got the error in sendOTPOnMail() of SendOTP :- " + e.getMessage());
			return false;
		}

	}

	

}
