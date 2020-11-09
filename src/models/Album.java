package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	public String getAlbumName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addPhoto(Photo photo) {
		this.photos.add(photo);
		this.photoCount++;
	}
	
	public void removePhoto(Photo photo) {
		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).getPhotoName().equals(photo.getPhotoName())) {
				photos.remove(i);
				break;
			}
		}
		this.photoCount--;
	}

	public int getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}
	
	public List<Photo> getPhotos() {
		return this.photos;
	}
	
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
