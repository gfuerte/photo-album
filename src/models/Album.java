package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class Album implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int photoCount;
	private List<Photo> photos;
	
	public Album(String name) {
		this.name = name;
		this.photos = new ArrayList<Photo>();
		this.photoCount = 0;
	}

	/**
	 * Returns this album name.
	 * @return String value of this album name.
	 */
	public String getAlbumName() {
		return this.name;
	}

	/**
	 * Sets album name to name parameter.
	 * @param name Sets this album name to name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Adds photo to this album.
	 * @param photo Photo to be added.
	 */
	public void addPhoto(Photo photo) {
		this.photos.add(photo);
		this.photoCount++;
	}
	
	/**
	 * Removes photo from this album.
	 * @param photo Photo to be removed.
	 */
	public void removePhoto(Photo photo) {
		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).getPhotoName().equals(photo.getPhotoName())) {
				photos.remove(i);
				break;
			}
		}
		this.photoCount--;
	}

	/**
	 * Returns this album's photo count.
	 * @return int value of photo count.
	 */
	public int getPhotoCount() {
		return photoCount;
	}
	
	/**
	 * Returns this album's photos.
	 * @return List Photo field of this album's photos.
	 */
	public List<Photo> getPhotos() {
		return this.photos;
	}
	
	/**
	 * Given photo parameter, returns the next photo in the album.
	 * @param photo Photo that is previous to the desired photo.
	 * @return Photo object of the next photo in the album.
	 */
	public Photo getNextPhoto(Photo photo) {
		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).getPhotoName().equals(photo.getPhotoName())) {
				if(i == photos.size()-1) {
					return photos.get(0);
				} else {
					return photos.get(i+1);
				}
			}
		}
		return photo;
	}
	
	/**
	 * Given photo parameter, returns the previous photo in the album.
	 * @param photo Photo that is next to the desired photo.
	 * @return Photo object of the previous photo in the album.
	 */
	public Photo getPrevPhoto(Photo photo) {
		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).getPhotoName().equals(photo.getPhotoName())) {
				if(i == 0) {
					return photos.get(photos.size()-1);
				} else {
					return photos.get(i-1);
				}
			}
		}
		return photo;
	}
}
