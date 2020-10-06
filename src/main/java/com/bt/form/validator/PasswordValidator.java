package com.bt.form.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component("passwordValidator")
public class PasswordValidator {
	private static final String PASSWORD_REGEX = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])"
			+ "(?=\\S+$).{8,20}$";

	public boolean isValidPassword(String password) {
		Pattern p = Pattern.compile(PASSWORD_REGEX);
		Matcher m = p.matcher(password);
		return m.matches();
	}
}
