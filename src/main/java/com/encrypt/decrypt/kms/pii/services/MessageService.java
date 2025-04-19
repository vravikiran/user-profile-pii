package com.encrypt.decrypt.kms.pii.services;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.encrypt.decrypt.kms.pii.config.TwilioConfig;
import com.encrypt.decrypt.kms.pii.entities.EmailAuthRequest;
import com.encrypt.decrypt.kms.pii.entities.MobileAuthRequest;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.mail.MessagingException;

@Service
public class MessageService {
	private static final Integer EXPIRE_MIN = 5;
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	EmailService emailService;
	@Autowired
	TwilioConfig twilioConfig;
	Logger logger = LoggerFactory.getLogger(MessageService.class);

	private String getRandomEmailOtp(String email) {
		String otp = String.valueOf(new Random().nextInt(1000, 10000));
		redisTemplate.opsForValue().set(email, otp);
		redisTemplate.expire(email, EXPIRE_MIN, TimeUnit.MINUTES);
		return otp;
	}

	private String getRandomOtp(String mobileno) {
		String otp = String.valueOf(new Random().nextInt(1000, 10000));
		redisTemplate.opsForValue().set(mobileno, otp);
		redisTemplate.expire(mobileno, EXPIRE_MIN, TimeUnit.MINUTES);
		return otp;
	}

	public MessageService() {
		super();
	}

	public void generateOtpToEmail(String email) throws UnsupportedEncodingException, MessagingException {
		String otp = getRandomEmailOtp(email);
		String content = "Please find the OTP to login into App: " + otp;
		emailService.sendEmail(email, "OTP to login application", content);
	}

	public boolean validateEmailOtp(EmailAuthRequest emailAuthRequest) throws ExecutionException {
		if (redisTemplate.opsForValue().get(emailAuthRequest.getEmail()) != null
				&& redisTemplate.opsForValue().get(emailAuthRequest.getEmail()).equals(emailAuthRequest.getOtp())) {
			logger.info("otp validation is successful");
			return true;
		}
		logger.error("invalid otp");
		return false;
	}

	public void generateOtpToMobile(String mobileNo) throws ApiException {
		if (mobileNo != null) {
			PhoneNumber to = new PhoneNumber("+91" + mobileNo);
			String otpMessage = "Please find the OTP to login into App: " + getRandomOtp(mobileNo);
			Message.creator(to, twilioConfig.getServiceId(), otpMessage).create();
		}
	}
	
	public boolean validateMobileOtp(MobileAuthRequest mobileAuthRequest) throws Exception {
		if (redisTemplate.opsForValue().get(mobileAuthRequest.getMobileNo()) != null
				&& redisTemplate.opsForValue().get(mobileAuthRequest.getMobileNo()).equals(mobileAuthRequest.getOtp())) {
			logger.info("otp validation is successful");
			return true;
		}
		logger.error("invalid otp");
		return false;
	}
}
