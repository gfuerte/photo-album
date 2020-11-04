package models;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
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
	
	@Override
	public int compareTo(User user) {
		if(this.username.equals(user.getUsername())) {
			return this.password.compareTo(user.getPassword());
		}
		return this.username.compareTo(user.getUsername());
	}
	
	public String toString() {
		return this.username + " " + this.password;
	}
}
