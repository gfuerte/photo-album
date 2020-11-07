package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Photo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String photoName;
	private String caption;
	private List<Tag> tags;
	
	private int width, height;
    private int[][] data;
	
	public Photo(Image image, String photoName) {
		this.photoName = photoName;
		this.caption = "";
		this.tags = new ArrayList<Tag>();
		
		width = ((int) image.getWidth());
        height = ((int) image.getHeight());
        data = new int[width][height];

        PixelReader reader = image.getPixelReader();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data[i][j] = reader.getArgb(i, j);
            }
        }
	}
	
	public Image getImage() {
		WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
            	writer.setArgb(i, j, data[i][j]);
            }
        }
        return image;
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
