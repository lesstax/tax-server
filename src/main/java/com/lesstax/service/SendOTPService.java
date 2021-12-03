package com.lesstax.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class SendOTPService {

	private static final Logger logger = LogManager.getLogger(SendOTPService.class);

	private LoadingCache<String, Integer> otpCache;

	public SendOTPService() {
		super();
		otpCache = CacheBuilder.newBuilder().maximumSize(100) // maximum 100 records can be cached
				.expireAfterAccess(30, TimeUnit.MINUTES) // cache will expire after 30 minutes of access
				.build(new CacheLoader<String, Integer>() { // build the cacheloader

					public Integer load(String key) throws Exception {
						return null;
					}

		});
	}

	// This method is used to push the opt number against Key. Rewrite the OTP if it
	// exists
	// Using user id as key
	public Integer generateOTP(String clientEmail) {

		Random random = new Random();
		Integer otp = 100000 + random.nextInt(900000);
		otpCache.put(clientEmail, otp);
		return otp;
	}

	// This method is used to return the OPT number against Key->Key values is
	// username
	public int getOtp(String clientEmail) {
		try {
			return otpCache.get(clientEmail);
		} catch (Exception e) {
			return 0;
		}
	}

	// This method is used to clear the OTP catched already
	public void clearOTP(String key){ 
		 otpCache.invalidate(key);
	 }
}
