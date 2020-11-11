package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private List<Album> albums;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.albums = new ArrayList<Album>();
	}

	/**
	 * Returns this User's username.
	 * @return String value of username field.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns this User's password.
	 * @return String value of password field.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Adds a new album to user's album list.
	 * @param album New album to be added.
	 */
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	/**
	 * Deletes album to user's album list.
	 * @param name Album to be deleted.
	 */
	public void deleteAlbum(String name) {
		for(int i = 0; i < albums.size(); i++) {
			if(albums.get(i).getAlbumName().equals(name)) {
				albums.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Returns this User's albums.
	 * @return Array list value of albums.
	 */
	public List<Album> getAlbums() {
		return albums;
	}
}
