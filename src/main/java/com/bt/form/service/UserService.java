package com.bt.form.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.bt.form.dao.UserInfo;
import com.bt.form.model.LoginForm;
import com.bt.form.model.RegisterForm;
import com.bt.form.model.UserData;

public interface UserService {

	UserInfo findById(Integer id);

	UserData validateUser(LoginForm loginForm);

	List<UserInfo> findAll();

	void saveOrUpdate(UserInfo userInfo);
	
	void save(RegisterForm registerForm);
	
	void approve(Integer id);

	void delete(int id);
	
	public String decrypt(String str) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException;

}