package com.encrypt.decrypt.kms.pii.controllers;

import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encrypt.decrypt.kms.pii.entities.EmailAuthRequest;
import com.encrypt.decrypt.kms.pii.entities.MobileAuthRequest;
import com.encrypt.decrypt.kms.pii.services.MessageService;
import com.encrypt.decrypt.kms.pii.util.JwtHelper;
import com.twilio.exception.ApiException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;

@Tag(description = "Validates the user through otp sent to mobile number or email", name = "Authenticates and authorizes user")
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	MessageService messageService;
	@Autowired
	JwtHelper jwtHelper;

	@Operation(method = "GET", description = "Generates an otp and sent to given email")
	@GetMapping("/emailotp")
	public ResponseEntity<String> getOtp(@RequestParam String email)
			throws UnsupportedEncodingException, MessagingException {
		messageService.generateOtpToEmail(email);
		return ResponseEntity.ok("otp generated successfully");
	}

	@Operation(method = "POST", description = "Validates the otp sent to given email and authorizes user")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "User validation through email is successful"),
			@ApiResponse(responseCode = "403", description = "Submitted OTP is invali") })
	@PostMapping("/verify/emailotp")
	public ResponseEntity<String> validateOtp(@RequestBody EmailAuthRequest emailAuthRequest)
			throws ExecutionException, UsernameNotFoundException {
		if (messageService.validateEmailOtp(emailAuthRequest)) {
			String token = jwtHelper.generateTokenByEmail(emailAuthRequest.getEmail());
			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid OTP");
		}
	}

	@GetMapping("/mobileotp")
	public ResponseEntity<HttpStatus> generateOtp(@RequestParam String mobileNo) throws ApiException {
		messageService.generateOtpToMobile(mobileNo);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/verify/mobileotp")
	public ResponseEntity<String> validateOtp(@RequestBody MobileAuthRequest mobileAuthRequest)
			throws NoSuchElementException, Exception {
		if (messageService.validateMobileOtp(mobileAuthRequest)) {
			String token = jwtHelper.generateTokenByMobileNo(mobileAuthRequest.getMobileNo());
			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.badRequest().body("Invalid otp");
		}
	}
}
