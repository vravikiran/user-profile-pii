package com.encrypt.decrypt.kms.pii.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="aws")
public class KmsConfig {
	@Value("${aws.kms.key}")
	private String key;
	public KmsConfig() {
		super();
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public KmsConfig(String key) {
		super();
		this.key = key;
	}
}
