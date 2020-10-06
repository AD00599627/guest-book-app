package com.bt.form.dao;

public class UserInfo {

	private Integer id;

	private String name;

	private Integer contact;

	private String email;

	private String password;

	private Boolean isAdmin;

	private Boolean isApproved;

	private String comments;

	public boolean isNew() {
		return (this.id == null);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getContact() {
		return contact;
	}

	public void setContact(Integer contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", name=" + name + ", contact=" + contact + ", email=" + email + ", password="
				+ password + ", isAdmin=" + isAdmin + ", isApproved=" + isApproved + ", comments=" + comments + "]";
	}

}
