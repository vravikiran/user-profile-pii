package com.encrypt.decrypt.kms.pii.exceptions;

public class DuplicateUserException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateUserException() {
		super();
	}

	public DuplicateUserException(String message) {
		super(message);
	}
	

}
