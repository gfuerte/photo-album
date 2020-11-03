package models;

public class Album {
	private String name;
	private int numPhotos;
	
	public Album(String name, int numPhotos) {
		this.name = name;
		this.numPhotos = numPhotos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumPhotos() {
		return numPhotos;
	}

	public void setNumPhotos(int numPhotos) {
		this.numPhotos = numPhotos;
	}
}
