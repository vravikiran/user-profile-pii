package com.encrypt.decrypt.kms.pii.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.encrypt.decrypt.kms.pii.entities.BankAccountDetails;
import com.encrypt.decrypt.kms.pii.entities.KycDetails;
import com.encrypt.decrypt.kms.pii.entities.Role;
import com.encrypt.decrypt.kms.pii.entities.UserDto;
import com.encrypt.decrypt.kms.pii.entities.UserInfoDetails;
import com.encrypt.decrypt.kms.pii.entities.UserProfile;
import com.encrypt.decrypt.kms.pii.enums.RoleTypeEnum;
import com.encrypt.decrypt.kms.pii.exceptions.DuplicateUserException;
import com.encrypt.decrypt.kms.pii.exceptions.InvalidBankDetailsException;
import com.encrypt.decrypt.kms.pii.exceptions.InvalidKycDetailsException;
import com.encrypt.decrypt.kms.pii.exceptions.UserNotFoundException;
import com.encrypt.decrypt.kms.pii.repositories.UserProfileRepository;
import com.encrypt.decrypt.kms.pii.util.Constants;
import com.encrypt.decrypt.kms.pii.util.HashGenerator;
import com.encrypt.decrypt.kms.pii.util.KmsUtil;

@Service
public class UserProfileService implements UserDetailsService {
	@Autowired
	UserProfileRepository userProfileRepository;
	@Autowired
	KmsUtil kmsUtil;

	Logger logger = LoggerFactory.getLogger(UserProfileService.class);

	public UserProfile createUserProfile(UserDto userDto, boolean isAdminUser) throws Exception {
		logger.info("Creation of user profile started :: ");
		UserProfile userProfile = new UserProfile();
		if (Long.valueOf(userDto.getMobileno()) != null) {
			if (isUserExistsByMobileNumber(userDto.getMobileno())) {
				logger.info("Creation of user profile failed as user already exists with given mobile number");
				throw new DuplicateUserException("user already exists with given mobile number");
			} else {
				userProfile.setMobileno_hash(HashGenerator.generateSHA256Hash(Long.toString(userDto.getMobileno())));
			}
		}
		if (userDto.getPan_number() != null) {
			if (isUserExistsWithPanNo(userDto.getPan_number())) {
				logger.info("Creation of user profile failed as user already exists with given pan number");
				throw new DuplicateUserException("user already exists with given pan number");
			} else {
				userProfile.setPanno_hash(HashGenerator.generateSHA256Hash(userDto.getPan_number()));
			}
		}
		if (userDto.getEmail() != null) {
			if (isUserExistsWithEmail(userDto.getEmail())) {
				logger.info("Creation of user profile failed as user already exists with given email");
				throw new DuplicateUserException("user already exists with given email");
			} else {
				userProfile.setEmail_hash(HashGenerator.generateSHA256Hash(userDto.getEmail()));
			}
		}
		userProfile.setCreated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		Role role = null;
		if (isAdminUser) {
			role = new Role(RoleTypeEnum.ADMIN.getRoleid(), RoleTypeEnum.ADMIN.getRole_name());
		} else {
			role = new Role(RoleTypeEnum.CUSTOMER.getRoleid(), RoleTypeEnum.CUSTOMER.getRole_name());
		}
		userProfile.setMobileno(userDto.getMobileno());
		userProfile.setEmail(userDto.getEmail());
		userProfile.setFull_name(userDto.getFull_name());
		userProfile.setPan_number(userDto.getPan_number());
		userProfile.setDate_of_birth(userDto.getDate_of_birth());
		userProfile.setRole(role);
		UserProfile createdUserProfile = userProfileRepository.save(userProfile);
		logger.info("user profile created successfully for given mobile number :: " + userProfile.getMobileno());
		return createdUserProfile;
	}

	public UserProfile getUserProfile(long mobileno) throws UserNotFoundException {
		logger.info("Fetching user profile for given mobile number :: " + mobileno);
		if (userProfileRepository.existsById(generateMobileNumberHash(mobileno))) {
			logger.info("successfully fetched user profile with given mobile number :: " + mobileno);
			return userProfileRepository.getReferenceById(generateMobileNumberHash(mobileno));
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileno);
			throw new UserNotFoundException("User not found with given mobile number");
		}
	}

	public void addBankAccDetailsToProfile(long mobileno, BankAccountDetails bankAccountDetails)
			throws UserNotFoundException, InvalidBankDetailsException {
		logger.info("verifying bank account details and adding it to user profile with mobile number :: " + mobileno
				+ " started");
		UserProfile userProfile = null;
		if (userProfileRepository.existsById(generateMobileNumberHash(mobileno))) {
			userProfile = userProfileRepository.getReferenceById(generateMobileNumberHash(mobileno));
			bankAccountDetails.setCreated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			bankAccountDetails.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			userProfile.setBankAccountDetails(bankAccountDetails);
			userProfile.setBank_details_verified(true);
			userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			userProfileRepository.save(userProfile);
			logger.info(
					"bank account details are verified successfully and added it to user profile with mobile number :: "
							+ mobileno);
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileno);
			throw new UserNotFoundException("User with given mobile number doesn't exists");
		}
	}

	public void verifyAndAddKycDetails(long mobileno, KycDetails kycDetails)
			throws UserNotFoundException, InvalidKycDetailsException {
		logger.info("verifying kyc details and adding it to user profile with given mobile number :: " + mobileno
				+ " started");
		UserProfile userProfile = null;
		if (userProfileRepository.existsById(generateMobileNumberHash(mobileno))) {
			userProfile = userProfileRepository.getReferenceById(generateMobileNumberHash(mobileno));
			userProfile.setKycDetails(kycDetails);
			userProfile.setAadhar_verified(true);
			userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			userProfileRepository.save(userProfile);
			logger.info("kyc details are verified successfully and added it to user profile with mobile number :: "
					+ mobileno);
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileno);
			throw new UserNotFoundException("User with given mobile number doesn't exists");
		}
	}

	public UserProfile updateUserDetails(long mobileNo, Map<String, String> valuesToUpdate)
			throws UserNotFoundException, DuplicateUserException, NoSuchElementException {
		UserProfile userProfile = null;
		UserProfile updatedUserProfile = null;
		if (userProfileRepository.existsById(generateMobileNumberHash(mobileNo))) {
			userProfile = userProfileRepository.getReferenceById(generateMobileNumberHash(mobileNo));
			if (valuesToUpdate.containsKey(Constants.PAN_NO)) {
				if (valuesToUpdate.get(Constants.PAN_NO) != null) {
					if (!isUserExistsWithPanNo(valuesToUpdate.get(Constants.PAN_NO))) {
						valuesToUpdate.put(Constants.PAN_NO, valuesToUpdate.get(Constants.PAN_NO));
						valuesToUpdate.put(Constants.PANNO_HASH,
								HashGenerator.generateSHA256Hash(valuesToUpdate.get(Constants.PANNO_HASH)));
					} else {
						logger.info("user already exists with given pan number");
						throw new DuplicateUserException("user already exists with given pan number");
					}
				}
			}
			if (valuesToUpdate.containsKey(Constants.EMAIL)) {
				if (valuesToUpdate.get(Constants.EMAIL) != null) {
					if (!isUserExistsWithEmail(valuesToUpdate.get(Constants.EMAIL))) {
						valuesToUpdate.put(Constants.EMAIL, valuesToUpdate.get(Constants.EMAIL));
						valuesToUpdate.put(Constants.EMAIL_HASH,
								HashGenerator.generateSHA256Hash(valuesToUpdate.get(Constants.EMAIL)));
					} else {
						logger.info("user already exists with given email");
						throw new DuplicateUserException("user already exists with given email");
					}
				}
			}
			try {
				userProfile.updateValues(userProfile, valuesToUpdate);
				userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			} catch (NoSuchElementException exception) {
				throw new NoSuchElementException("one or more fields are not valid");
			}
			updatedUserProfile = userProfileRepository.save(userProfile);
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileNo);
			throw new UserNotFoundException("User profile not found with given mobile number :: " + mobileNo);
		}
		return updatedUserProfile;
	}

	public UserDetails loadUserByUsername(String username) {
		Optional<UserProfile> userProfile = userProfileRepository.findById(HashGenerator.generateSHA256Hash(username));
		return userProfile.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with given mobileNo " + username));
	}

	public UserInfoDetails loadUserByEmail(String email) throws UsernameNotFoundException {
		Optional<UserProfile> userProfile = userProfileRepository.findByEmail(email);
		return userProfile.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with given email " + email));
	}

	public boolean isUserExistsByMobileNumber(long mobileNo) throws DuplicateUserException {
		return userProfileRepository.existsById(generateMobileNumberHash(mobileNo));
	}

	private boolean isUserExistsWithEmail(String email) {
		return userProfileRepository.existsByEmail(HashGenerator.generateSHA256Hash(email));
	}

	private boolean isUserExistsWithPanNo(String panNumber) {
		return userProfileRepository.existsByPanNumber(HashGenerator.generateSHA256Hash(panNumber));
	}

	private String generateMobileNumberHash(long mobileNo) {
		return HashGenerator.generateSHA256Hash(Long.toString(mobileNo));
	}
}