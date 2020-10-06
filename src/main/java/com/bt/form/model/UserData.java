package com.bt.form.model;

import com.bt.form.dao.UserInfo;

public class UserData {
	private Boolean isUserExists;
	private String statusMessage;
	private UserInfo userInfo;

	public Boolean getIsUserExists() {
		return isUserExists;
	}

	public void setIsUserExists(Boolean isUserExists) {
		this.isUserExists = isUserExists;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public String toString() {
		return "UserData [isUserExists=" + isUserExists + ", statusMessage=" + statusMessage + ", userInfo=" + userInfo
				+ "]";
	}
}
