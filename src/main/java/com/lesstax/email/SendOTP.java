package com.lesstax.email;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SendOTP {
	
	private static final Logger logger = LogManager.getLogger(SendOTP.class);

	public String generateOTP(int len) {

		logger.info("Inside generateOTP() of SendOTP");
		System.out.println("Generating OTP using random() : ");
		System.out.print("You OTP is : ");

		// Using numeric values
		String numbers = "0123456789";

		// Using random method
		Random rndm_method = new Random();

		char[] otp = new char[len];

		for (int i = 0; i < len; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		String generatedOTP = new String(otp);
		logger.info("Exiting from generateOTP() of SendOTP");

		return generatedOTP;
	}
}
