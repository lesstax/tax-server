package com.lesstax.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lesstax.businessDelegate.ClientBusinessDelegate;
import com.lesstax.businessDelegate.SendSMS;
import com.lesstax.model.Client;
import com.lesstax.model.mapper.ClientModel;

@RestController
@RequestMapping("/client")
public class ClientController {

	private static final Logger logger = LogManager.getLogger(ClientController.class);

	@Autowired
	private ClientBusinessDelegate clientBusinessDelegate;

	@PostMapping("/save")
	public Client saveClient(@RequestBody Client client) {
		logger.info("Inside saveClient() of client controller");
		logger.info("Exiting from saveClient() of client controller");
		return clientBusinessDelegate.saveClient(client);
	}

	@GetMapping
	public List<Client> getAllClients() {
		logger.info("Inside getAllClients() of client controller");
		logger.info("Exiting from getAllClients() of client controller");
		return clientBusinessDelegate.getAllClient();
	}

	@GetMapping("/{id}")
	public ClientModel findClient(@PathVariable Long id) throws Exception {
		logger.info("Inside findClient() of client controller");
		ClientModel clientMapper = clientBusinessDelegate.findClient(id);
		logger.info("Exiting from findClient() of client controller");
		return clientMapper;
	}

	@GetMapping("/hello")
	public String helloCheck() {
		
		ClientBusinessDelegate clientBusinessDelegate = new ClientBusinessDelegate();
		clientBusinessDelegate.sendOTPOnMail("");
		SendSMS sendSMS = new SendSMS();
		sendSMS.sendSms();
		return "hello check";
		/*
		 * getGroups(); try { // Construct data String apiKey = "apikey=" +
		 * "NGI1MDcwNTY0ODQ1NGI3MzY3NGQ1NDczNDc2NzcwNTg="; String message = "&message="
		 * + "This is your message"; String sender = "&sender=" + "TXTLCL"; String
		 * numbers = "&numbers=" + "+917829686356";
		 * 
		 * // Send data HttpURLConnection conn = (HttpURLConnection) new
		 * URL("https://api.textlocal.in/send/?").openConnection(); String data = apiKey
		 * + numbers + message + sender; conn.setDoOutput(true);
		 * conn.setRequestMethod("POST"); conn.setRequestProperty("Content-Length",
		 * Integer.toString(data.length()));
		 * conn.getOutputStream().write(data.getBytes("UTF-8")); final BufferedReader rd
		 * = new BufferedReader(new InputStreamReader(conn.getInputStream())); final
		 * StringBuffer stringBuffer = new StringBuffer(); String line; while ((line =
		 * rd.readLine()) != null) { stringBuffer.append(line); } rd.close();
		 * 
		 * return stringBuffer.toString(); } catch (Exception e) {
		 * System.out.println("Error SMS "+e); return "Error "+e; }
		 */
	}

	public String getGroups() {
		try {
			// Construct data
			String apiKey = "apikey=" + "NzUzOTY3N2E2ZTQzNzU3YTcyNjU1NzQ1NmE3MTM0NmE=";

			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/get_groups/?")
					.openConnection();
			String data = apiKey;
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

}
