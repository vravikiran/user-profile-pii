package com.encrypt.decrypt.kms.pii.services;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.encrypt.decrypt.kms.pii.entities.UserProfile;
import com.encrypt.decrypt.kms.pii.exceptions.UserNotFoundException;
import com.encrypt.decrypt.kms.pii.repositories.UserProfileRepository;
import com.encrypt.decrypt.kms.pii.util.HashGenerator;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class FileService {
	@Autowired
	UserProfileRepository userProfileRepository;
	Logger logger = LoggerFactory.getLogger(FileService.class);
	@Autowired
	S3Client s3Client;

	public String uploadProfileImage(MultipartFile file, long mobileno, String bucketName)
			throws IOException, UserNotFoundException {
		logger.info("Upload image for user profile with mobile number ::" + mobileno);
		String key = Long.valueOf(mobileno) + "/" + LocalDateTime.now() + "_" + file.getOriginalFilename();
		URL url = imageUploadFromMobile(file, bucketName, key);
		logger.info("url of the uploaded image :: " + url.toString());
		UserProfile userProfile = userProfileRepository.getReferenceById(HashGenerator.generateSHA256Hash(Long.toString(mobileno)));
		userProfile.setUser_image_url(url.toString());
		userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		userProfileRepository.save(userProfile);
		logger.info("profile image uploaded successfully");
		return url.toString();
	}

	public String uploadKycImage(MultipartFile file, long mobileno, String bucketName)
			throws S3Exception, AwsServiceException, SdkClientException, IOException {
		logger.info("Upload kyc image for user profile with mobile number ::" + mobileno);
		String key = Long.valueOf(mobileno) + "/" + LocalDateTime.now() + "_" + file.getOriginalFilename();
		URL url = imageUploadFromMobile(file, bucketName, key);
		logger.info("kyc image uploaded successfully");
		return url.toString();
	}

	public String uploadMovieImage(MultipartFile file, String bucketName)
			throws S3Exception, AwsServiceException, SdkClientException, IOException {
		logger.info("Upload movie image started");
		URL url = imageUploadFromMobile(file, bucketName, file.getOriginalFilename());
		logger.info("movie image uploaded successfully");
		return url.toString();
	}

	public URL imageUploadFromMobile(MultipartFile file, String bucketName, String key) throws IOException {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();
		s3Client.putObject(putObjectRequest,
				RequestBody.fromInputStream(file.getInputStream(), file.getBytes().length));
		GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName).key(key).build();
		URL url = s3Client.utilities().getUrl(request);
		return url;
	}
}
