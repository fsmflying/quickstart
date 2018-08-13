package com.fsmflying.database;

import java.io.Serializable;

public class CsdnUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String email;
	
	
	
	public CsdnUser() {
		super();
	}

	public CsdnUser(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "CsdnUser [username=" + username + ", password=" + password + ", email=" + email + "]";
	}

}
