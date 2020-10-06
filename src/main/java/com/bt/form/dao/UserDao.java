package com.bt.form.dao;

import java.util.List;

public interface UserDao {

	UserInfo findById(Integer id);

	List<UserInfo> findAll();

	void save(UserInfo user);

	void updateComments(UserInfo user);

	void delete(Integer id);

	UserInfo findByEmail(String email);
	
	void updateApproveStatus(Integer id);

}