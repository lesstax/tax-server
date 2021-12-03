//package com.lesstax.controller;
//
//import java.util.HashMap;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.lesstax.config.OtpService;
//import com.lesstax.email.SendEmail;
//
//public class OtpController {
//
//	private static final Logger logger = LogManager.getLogger(OtpController.class);
//
//	@Autowired
//	public OtpService otpService;
//
//	@Autowired
//	public SendEmail myEmailService;
//
//	@GetMapping("/generateOtp")
//	public String generateOtp( String clientMailId, String clientName) {
//
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String username = auth.getName();
//
//		int otp = otpService.generateOTP(username);
//
//		logger.info("OTP : " + otp);
//
//		// Generate The Template to send OTP
//		EmailTemplate template = new EmailTemplate("SendOtp.html");
//
//		Map<String, String> replacements = new HashMap<String, String>();
//		replacements.put("user", username);
//		replacements.put("otpnum", String.valueOf(otp));
//
//		String message = template.getTemplate(replacements);
//
//		myEmailService.sendOTPOnMail(clientMailId,clientName);
//
//		return "otppage";
//	}
//
//	@RequestMapping(value = "/validateOtp", method = RequestMethod.GET)
//	public @ResponseBody String validateOtp(@RequestParam("otpnum") int otpnum) {
//
//		final String SUCCESS = "Entered Otp is valid";
//
//		final String FAIL = "Entered Otp is NOT valid. Please Retry!";
//
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String username = auth.getName();
//
//		logger.info(" Otp Number : " + otpnum);
//
//		// Validate the Otp
//		if (otpnum >= 0) {
//			int serverOtp = otpService.getOtp(username);
//
//			if (serverOtp > 0) {
//				if (otpnum == serverOtp) {
//					otpService.clearOTP(username);
//					return ("Entered Otp is valid");
//				} else {
//					return SUCCESS;
//				}
//			} else {
//				return FAIL;
//			}
//		} else {
//			return FAIL;
//		}
//	}
//}
