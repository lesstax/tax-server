package com.lesstax.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BusinessUtility {
	
	private static final Logger logger = LogManager.getLogger(BusinessUtility.class);
	
	private static MessageDigest md;

	public static String cryptWithMD5(String pass) {
		logger.info("Inside cryptWithMD5() of Client BusinessDelegate");
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			logger.info("Exiting from cryptWithMD5() of Client BusinessDeleg");
		}
		return null;

	}

}
