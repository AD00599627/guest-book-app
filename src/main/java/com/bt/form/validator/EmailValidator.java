package com.bt.form.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * The Class EmailValidator.
 */
@Component("emailValidator")
public class EmailValidator {

	private Pattern pattern;

	/** The is REGEX for email pattern. */
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	/**
	 * Valid.
	 *
	 * @param email the email
	 * @return true, if successful
	 * this method validate user provide email 
	 */
	public boolean valid(final String email) {

		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}
}