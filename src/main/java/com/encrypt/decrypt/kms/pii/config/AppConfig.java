package com.encrypt.decrypt.kms.pii.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

@Configuration
public class AppConfig {
	@Autowired
	TwilioConfig twilioConfig;

	@PostConstruct
	public void init() {
		Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
	}

}
