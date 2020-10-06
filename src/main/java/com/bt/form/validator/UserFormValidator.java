package com.bt.form.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.bt.form.dao.UserInfo;
import com.bt.form.model.FileUpload;
import com.bt.form.model.LoginForm;
import com.bt.form.model.RegisterForm;
import com.bt.form.service.UserService;

/**
 * The Class UserFormValidator validate user provided form and creates error
 * message on the validated input parameters
 */
@Component
public class UserFormValidator implements Validator {

	@Autowired
	@Qualifier("emailValidator")
	EmailValidator emailValidator;
	
	@Autowired
	@Qualifier("passwordValidator")
	PasswordValidator passwordValidator;

	/** The user service. */
	@Autowired
	UserService userService;

	private final Logger logger = LoggerFactory.getLogger(UserFormValidator.class);
	private static final String EMAIL = "email";
	private static final String NOTEMPTY_EMAIL =  "NotEmpty.userForm.email";
	private static final String PASSWORD =  "password";
	/**
	 * Supports.
	 *
	 * @param clazz the clazz
	 * @return true, if successful it supports RegisterForm,LoginForm,UserInfo beans
	 *         for validations
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return RegisterForm.class.equals(clazz) || LoginForm.class.equals(clazz) || UserInfo.class.equals(clazz)
				|| FileUpload.class.equals(clazz);
	}

	/**
	 * Validate.
	 *
	 * @param target the target
	 * @param errors the errors this method validate all the classes provided in
	 *               supports method
	 */
	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("inside validate method");
		if (target instanceof LoginForm) {
			logger.debug("inside loginform validate");
			LoginForm loginForm = (LoginForm) target;

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, EMAIL, NOTEMPTY_EMAIL);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD, "NotEmpty.userForm.password");
			if (!emailValidator.valid(loginForm.getEmail())) {
				errors.rejectValue(EMAIL, "Pattern.userForm.email");
			}

		} else if (target instanceof RegisterForm) {
			logger.debug("inside RegisterForm validate");
			RegisterForm registerForm = (RegisterForm) target;

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.userForm.name");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, EMAIL, NOTEMPTY_EMAIL);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD, "NotEmpty.userForm.password");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.userForm.confirmPassword");

			if (registerForm.getEmail() == null || registerForm.getEmail().length() < 2
					|| !emailValidator.valid(registerForm.getEmail())) {
				errors.rejectValue(EMAIL, NOTEMPTY_EMAIL);
			}
			if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
				errors.rejectValue("confirmPassword", "Diff.userform.confirmPassword");
			}
			if (!passwordValidator.isValidPassword(registerForm.getPassword())) {
				errors.rejectValue(PASSWORD, "NoStandard.userForm.password");
			}

		} else if (target instanceof UserInfo) {
			logger.debug("inside UserInfo validate");
		}
	}
	
}