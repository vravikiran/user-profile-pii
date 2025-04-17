package com.encrypt.decrypt.kms.pii.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * Checks given phone number is valid
 */
public class PhoneNumberValidator  implements ConstraintValidator<IsValidPhoneNumber, Long>{

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return (value == null || (value != null && Long.toString(value).length() != 10)) ? false : true;
	}

}
