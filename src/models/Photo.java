package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class Photo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String photoName;
	private String caption;
	private Calendar date;
	private List<Tag> tags;
	
	private int width, height;
    private int[][] data;

	public Photo(Image image, String photoName, Calendar date) {
		this.photoName = photoName;
		this.date = date;
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
	
	/**
	 * Returns this Photo's Image.
	 * @return Image value of this Photo's image.
	 */
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

	/**
	 * Returns this photo's name.
	 * @return String value of photo name.
	 */
	public String getPhotoName() {
		return photoName;
	}

	/**
	 * Returns this photo's caption.
	 * @return String value of photo caption.
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Set this photo's caption to parameter.
	 * @param caption Desired new caption of photo.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Returns this photo's current date.
	 * @return Calendar value of photo date.
	 */
	public Calendar getDate() {
		return this.date;
	}
	
	/**
	 * Returns this photo's list of tags.
	 * @return Arraylist value of tags.
	 */
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * Adds a tag to this photo's list of tags.
	 * @param tag Desired tag to be added.
	 */
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	/**
	 * Removes a tag to this photo's list of tags.
	 * @param tag Desired tag to be removed.
	 */
	public void deleteTag(Tag tag) {
		for(int i = 0; i < tags.size(); i++) {
			if(tags.get(i).getKey().equals(tag.getKey()) 
					&& tags.get(i).getValue().equals(tag.getValue())) {
				tags.remove(i);
				return;
			}
			
		}
	}
	
	/**
	 * Checks if a tag is already present in this photo's list of tags.
	 * @param tag Tag that is to be checked in the list.
	 * @return Returns true if the tag is already present, false otherwise.
	 */
	public boolean tagDuplicate(Tag tag) {
		for(int i = 0; i < tags.size(); i++) {
			if(tags.get(i).getKey().equals(tag.getKey()) 
					&& tags.get(i).getValue().equals(tag.getValue())) {
				System.out.println("Duplicate at: " + i);
				return true;
			}
		}
		
		return false;
	}
	
}
