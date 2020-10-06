package com.bt.form.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.form.dao.UserDao;
import com.bt.form.dao.UserInfo;
import com.bt.form.model.LoginForm;
import com.bt.form.model.RegisterForm;
import com.bt.form.model.UserData;

/**
 * The Class UserServiceImpl.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	UserDao userDao;

	SecretKey key;

	Cipher dcipher;

	@Autowired
	public void setSecretKey(SecretKey key) {
		this.key = key;
	}

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public Cipher setconfigs() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		logger.debug("configuring Cipher");
		dcipher = Cipher.getInstance("AES");
		dcipher.init(Cipher.DECRYPT_MODE, key);
		return dcipher;
	}

	@Override
	public UserInfo findById(Integer id) {
		return userDao.findById(id);
	}

	@Override
	public List<UserInfo> findAll() {
		return userDao.findAll();
	}

	/**
	 * Save or update.
	 *
	 * @param userInfo the user info Based on user details it will save/update the
	 *                 userInfo details
	 */
	@Override
	public void saveOrUpdate(UserInfo userInfo) {
		if (Objects.isNull(findById(userInfo.getId()))) {
			logger.debug("Saving user in Database for User: {} ", userInfo.getName());
			userDao.save(userInfo);
		} else {
			logger.debug("Saving user in Database for ID: {} ", userInfo.getId());
			userDao.updateComments(userInfo);
		}
	}

	/**
	 * Save.
	 *
	 * @param registerForm the register form this service method with used to create
	 *                     user registration.
	 */
	public void save(RegisterForm registerForm) {
		UserInfo userInfo = new UserInfo();
		userInfo.setName(registerForm.getName());
		userInfo.setPassword(registerForm.getPassword());
		userInfo.setContact(registerForm.getContact());
		userInfo.setEmail(registerForm.getEmail());
		userDao.save(userInfo);
	}

	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	@Override
	public void delete(int id) {
		logger.debug("Deleting user in Database for ID: {} ", id);
		userDao.delete(id);
	}

	/**
	 * Validate user.
	 *
	 * @param loginForm the login form
	 * @return the user data this method validate the user and user is admin or not.
	 */
	@Override
	public UserData validateUser(LoginForm loginForm) {
		UserData userData = new UserData();
		UserInfo userInfo = userDao.findByEmail(loginForm.getEmail());
		if (Objects.isNull(userInfo)) {
			userData.setIsUserExists(false);
			userData.setStatusMessage("User not register in the system!");
			logger.debug("User not register in the system!");
		} else if (!loginForm.getPassword().equals(userInfo.getPassword())) {
			userData.setIsUserExists(false);
			userData.setStatusMessage("Wrong password Please provide currect password!");
			logger.debug("Wrong password Please provide currect password!");
		} else {
			userData.setIsUserExists(true);
			userData.setStatusMessage("Success!");
			logger.debug("User exists in the system!");
			userData.setUserInfo(userInfo);
		}
		return userData;
	}

	/**
	 * Approve.
	 *
	 * @param id the id Approved the user comments by Admin
	 */
	@Override
	public void approve(Integer id) {
		logger.debug("User comments were Approved!");
		userDao.updateApproveStatus(id);
	}

	/**
	 * decrypt.
	 *
	 * @param String the str will decript the password from application.properties file.
	 */
	@Override
	public String decrypt(String str) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException  {
		logger.debug("Calling decrypt method to decrypt password on {}", str);
		byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
		Cipher decipher = setconfigs();
		byte[] utf8 = decipher.doFinal(dec);
		return new String(utf8, StandardCharsets.UTF_8);
	}

}