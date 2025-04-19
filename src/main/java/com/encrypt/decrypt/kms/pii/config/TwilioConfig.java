package com.encrypt.decrypt.kms.pii.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {
	@Value("${twilio.account_sid}")
	private String accountSid;
	@Value("${twilio.auth_token}")
	private String authToken;
	@Value("${twilio.service_id}")
	private String serviceId;

	public String getAccountSid() {
		return accountSid;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getServiceId() {
		return serviceId;
	}

	public TwilioConfig() {
		super();
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
}
