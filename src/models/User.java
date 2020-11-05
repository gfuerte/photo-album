package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable, Comparable<User> {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private List<Album> albums;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.albums = new ArrayList<Album>();
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
	
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	public void deleteAlbum(String name) {
		for(int i = 0; i < albums.size(); i++) {
			if(albums.get(i).getAlbumName().equals(name)) albums.remove(i);
		}
	}
	
	public List<Album> getAlbums() {
		return albums;
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
