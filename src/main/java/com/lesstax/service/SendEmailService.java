package com.lesstax.service;

import com.google.common.cache.LoadingCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendEmailService {

    private static final Logger logger = LogManager.getLogger(SendEmailService.class);

    @Autowired
    private SendOTPService sendOTPService;

    @Value("${email.id}")
    private String emailId;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private String smtpPort;

    @Value("${mail.smtp.auth}")
    private String smtpAuth;

    @Value("${mail.smtp.socketFactory.port}")
    private String smtpSocketFactoryPort;

    @Value("${mail.smtp.socketFactory.class}")
    private String smtpSocketFactoryClass;

    private LoadingCache<String, String> otpCache;

    public Boolean sendOTPOnMail(String clientMailId, String clientName, String sendingPurpose) {
        logger.info("Inside sendOTPOnMail() of SendOTP ");
        Properties prop = new Properties();
        prop.put("mail.smtp.host", smtpHost);
        prop.put("mail.smtp.port", smtpPort);
        prop.put("mail.smtp.auth", smtpAuth);
        prop.put("mail.smtp.socketFactory.port", smtpSocketFactoryPort);
        prop.put("mail.smtp.socketFactory.class", smtpSocketFactoryClass);
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailId, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailId));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(clientMailId));
            message.setSubject("For Email Verification");
            Integer otp = sendOTPService.generateOTP(clientMailId);
            if (sendingPurpose.equals("OTP")) {
                message.setText("Dear " + clientName + "," + "\n\n Your OTP is:- " + otp + "\n\n Regards, \n LessTax");
            } else {
                message.setText(
                        "Hello Dear " + "," + "\n\n Your New Password is:- " + otp + "\n\n Regards, \n LessTax");
            }

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
