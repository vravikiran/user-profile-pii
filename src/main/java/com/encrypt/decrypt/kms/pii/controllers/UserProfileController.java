package com.encrypt.decrypt.kms.pii.controllers;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.encrypt.decrypt.kms.pii.entities.BankAccountDetails;
import com.encrypt.decrypt.kms.pii.entities.KycDetails;
import com.encrypt.decrypt.kms.pii.entities.UserProfile;
import com.encrypt.decrypt.kms.pii.exceptions.DuplicateUserException;
import com.encrypt.decrypt.kms.pii.exceptions.InvalidBankDetailsException;
import com.encrypt.decrypt.kms.pii.exceptions.InvalidKycDetailsException;
import com.encrypt.decrypt.kms.pii.exceptions.UserNotFoundException;
import com.encrypt.decrypt.kms.pii.services.FileService;
import com.encrypt.decrypt.kms.pii.services.UserProfileService;

@Controller
@RequestMapping("/userprofile")
public class UserProfileController {
	Logger logger = LoggerFactory.getLogger(UserProfileController.class);
	@Autowired
	UserProfileService userProfileService;
	@Autowired
	FileService fileService;
	private static final String profileBucket = "mtb-profile-images";
	private static final String kycBucket = "mtb-kyc-images";

	@PostMapping
	public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile)
			throws DuplicateUserException, Exception {
		logger.info("creating user profile started :: ");
		UserProfile createdUserProfile = userProfileService.createUserProfile(userProfile,false);
		logger.info("creation of user profile successful");
		return ResponseEntity.ok(createdUserProfile);
	}
	
	@PostMapping("/admin")
	public ResponseEntity<UserProfile> createAdminUser(@RequestBody UserProfile userProfile) throws DuplicateUserException, Exception {
		logger.info("creating Admin user profile started :: ");
		userProfileService.createUserProfile(userProfile,true);
		logger.info("creation of Admin user profile successful");
		return ResponseEntity.ok(userProfile);
	}

	@GetMapping
	public ResponseEntity<UserProfile> getUserProfile(@RequestParam long mobileno) throws UserNotFoundException {
		logger.info("fetching user profile with mobile number :: " + mobileno);
		UserProfile userProfile = userProfileService.getUserProfile(mobileno);
		logger.info("user profile fetched successfully");
		return ResponseEntity.ok(userProfile);
	}

	@PatchMapping("/update")
	public ResponseEntity<String> updateUserProfile(@RequestBody Map<String, String> valuesToUpdate,
			@RequestParam long mobileNo) throws UserNotFoundException, DuplicateUserException {
		logger.info("Updating user profile with given mobile number :: " + mobileNo);
		userProfileService.updateUserDetails(mobileNo, valuesToUpdate);
		logger.info("user profile updated successfully with given mobile number:: " + mobileNo);
		return ResponseEntity.ok("user profile updated successfully");
	}

	@PostMapping("/verify/bankdetails")
	public ResponseEntity<String> addAccountDetailsToProfile(@RequestParam long mobileno,
			@RequestBody BankAccountDetails bankAccountDetails)
			throws UserNotFoundException, InvalidBankDetailsException {
		logger.info("verification of bank details and adding to user profile with mobile number :: " + mobileno
				+ " started");
		userProfileService.addBankAccDetailsToProfile(mobileno, bankAccountDetails);
		logger.info(
				"verification of bank details completed successfully and added to user profile with mobile number ::"
						+ mobileno);
		return ResponseEntity.ok("account details verified successfully");
	}

	@PostMapping("/verify/kyc")
	public ResponseEntity<String> addKycDetailsToProfile(@RequestParam long mobileno,
			@RequestBody KycDetails kycDetails) throws UserNotFoundException, InvalidKycDetailsException {
		logger.info("kyc verification started and adding kyc details to user profile with mobile number :: " + mobileno
				+ " started");
		userProfileService.verifyAndAddKycDetails(mobileno, kycDetails);
		logger.info(
				"kyc verification completed successfully and added to user profile with mobile number :: " + mobileno);
		return ResponseEntity.ok("kyc details verified successfully");
	}

	@PostMapping(path = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadProfileImage(@RequestPart("photo") MultipartFile file,
			@RequestParam long mobileno)
			throws IOException, UserNotFoundException {
		logger.info("uploading profile image for user profile with mobile number :: " + mobileno);
		String url = fileService.uploadProfileImage(file, mobileno, profileBucket);
		logger.info("profile image uploaded successfully for user profile");
		return ResponseEntity.ok(url);
	}

	@PostMapping(path = "/kycimage/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadKycImage(@RequestPart("photo") MultipartFile file, @RequestParam long mobileno)
			throws IOException, UserNotFoundException {
		logger.info("uploading kyc image for user profile with mobile number :: " + mobileno);
		String url = fileService.uploadKycImage(file, mobileno, kycBucket);
		logger.info("kyc image uploaded successfully for user profile");
		return ResponseEntity.ok(url);
	}

}
