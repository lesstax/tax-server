package com.lesstax.businessDelegate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lesstax.businessRepositories.ClientBusinessRepository;
import com.lesstax.mapper.ClientMapper;
import com.lesstax.model.Client;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.repositories.ClientRepository;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;

@Service
public class ClientBusinessDelegate implements ClientBusinessRepository {

	private static final Logger logger = LogManager.getLogger(ClientBusinessDelegate.class);

	@Autowired
	private ClientRepository clientRepository;

	private ClientMapper mapper = Mappers.getMapper(ClientMapper.class);

	public static final String ACCOUNT_SID = "ACbea70e5a47debf62e7de325aa1d2136e";
	public static final String AUTH_TOKEN = "acca2f6c05de61e7b7cb3a005405211a";

	@Override
	public Client saveClient(Client client) {

		logger.info("Inside saveClient() of Client BusinessDelegate");

		client = clientRepository.save(client);
		if (client != null) {
			sendOTPOnMail(client.getEmail());
		}
		logger.info("Exiting from saveClient() of Client BusinessDelegate");
		return client;
	}

	@Override
	public List<Client> getAllClient() {
		logger.info("Inside getAllClient() of Client BusinessDelegate");
		logger.info("Exiting from getAllClient() of Client BusinessDelegate");
		return clientRepository.findAll();
	}

	@Override
	public ClientModel findClient(Long id) throws Exception {

		logger.info("Inside findClient() of Client BusinessDelegate");
		Client client = clientRepository.findById(id).orElseThrow(() -> new Exception("Client not available"));
		ClientModel clientMapper = mapper.clientToClientModel(client);
		logger.info("Exiting from findClient() of Client BusinessDelegate");
		return clientMapper;
	}

	/*
	  public Boolean sendOTP(String phoneNumber) {
	  
	  Twilio.init(ACCOUNT_SID, AUTH_TOKEN); Message message = Message.creator(new
	  com.twilio.type.PhoneNumber("+91" + phoneNumber), new
	  com.twilio.type.PhoneNumber("+12059533258"), "Where's Wallace?").create(); if
	  (message.getSid() != null) { return true; } return false; }
	 */

	public String sendSms(String phoneNumber) {
		try {
			// Construct data
			String apiKey = "apikey=" + "NGI1MDcwNTY0ODQ1NGI3MzY3NGQ1NDczNDc2NzcwNTg=";
			String message = "&message=" + "This is your message";
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + "+91" + phoneNumber;

			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();

			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS " + e);
			return "Error " + e;
		}
	}
	
	
	public Boolean sendOTPOnMail(String receiverMailId) {
		
		logger.info("Inside sendOTPOnMail() of Client BusinessDelegate");
		final String username = "sonidharmendra633@gmail.com";
        final String password = "dharmendra2021";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sonidharmendra633@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("vaibhavsoni196@gmail.com")// "receiverMailId)
            );
            message.setSubject("Testing Gmail SSL");
            message.setText("Dear Mail Crawler,"
                    + "\n\n Your OTP is:- " +generateOTP(6));

            Transport.send(message);

        	logger.info("Exiting from sendOTPOnMail() of Client BusinessDelegate");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Got the error in sendOTPOnMail() of Client BusinessDelegate :- " +e.getMessage());
            return false;
        }
		
	}
	
	public String generateOTP(int len)
    {
		
		logger.info("Inside generateOTP() of Client BusinessDelegate");
        System.out.println("Generating OTP using random() : ");
        System.out.print("You OTP is : ");
  
        // Using numeric values
        String numbers = "0123456789";
  
        // Using random method
        Random rndm_method = new Random();
  
        char[] otp = new char[len];
  
        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] =
             numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        String generatedOTP = new String(otp);
        logger.info("Exiting from generateOTP() of Client BusinessDelegate");
       
        return generatedOTP;
    }
}
