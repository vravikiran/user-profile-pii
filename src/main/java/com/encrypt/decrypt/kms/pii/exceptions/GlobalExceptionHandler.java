package com.encrypt.decrypt.kms.pii.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(exception = DuplicateUserException.class)
	public ResponseEntity<String> handleDuplicateUser(DuplicateUserException duplicateUserException) {
		return ResponseEntity.unprocessableEntity().body(duplicateUserException.getMessage());
	}

	@ExceptionHandler(exception = UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFoundException.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<List<String>> handleValidationException(ConstraintViolationException exception) {
		Set<ConstraintViolation<?>> exceptions = exception.getConstraintViolations();
		List<String> violations = new ArrayList<>();
		for (ConstraintViolation<?> e : exceptions) {
			violations.add(e.getPropertyPath() + "-" + e.getMessageTemplate());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(violations);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(InvalidBankDetailsException.class)
	public ResponseEntity<String> handleInvalidBankDetailsException(InvalidBankDetailsException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(InvalidKycDetailsException.class)
	public ResponseEntity<String> handleInvalidKycDetailsException(InvalidKycDetailsException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}
	
}
