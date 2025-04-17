package com.encrypt.decrypt.kms.pii.exceptions;

public class InvalidBankDetailsException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidBankDetailsException() {
	}

	public InvalidBankDetailsException(String message) {
		super(message);
	}
}
