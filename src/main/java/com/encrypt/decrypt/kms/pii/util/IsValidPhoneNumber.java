package com.encrypt.decrypt.kms.pii.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
/**
 * Annotation to check given phoneNumber is valid
 */
@Target({ ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface IsValidPhoneNumber {
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
