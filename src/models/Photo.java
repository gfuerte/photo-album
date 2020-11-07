package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String photoName;
	private String caption;
	private List<Tag> tags;
	
	public Photo(String photoName) {
		this.photoName = photoName;
		this.caption = "";
		this.tags = new ArrayList<Tag>();
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	public void deleteTag(Tag tag) {
		for(int i = 0; i < tags.size(); i++) {
			if(tags.get(i).getKey().equals(tag.getKey()) 
					&& tags.get(i).getValue().equals(tag.getValue())) tags.remove(i);
		}
	}
	
}
