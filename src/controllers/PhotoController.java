package controllers;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.User;

public class PhotoController {
	@FXML
	ImageView imageView;
	@FXML
	Button addTag, editTag, deleteTag, copyTo, moveTo, leftArrow, rightArrow, back, logout;
	@FXML
	Text photoName, photoDate, photoCaption;
	@FXML
	TableView<String> tags;
	@FXML 
	ComboBox<String> copyAlbum, moveAlbum;
	
	private User user;
	private Album album;
	private Photo photo;
	
	public void start(Stage mainStage) {
		imageView.setFitHeight(175.0);
		imageView.setFitWidth(335.0);
		imageView.setPreserveRatio(true);
		
		imageView.setImage(photo.getImage());
		centerImage(imageView);
		
		photoName.setText(photo.getPhotoName());
		if(photo.getCaption().length() == 0) {
			photoCaption.setText("No Caption");
		} else {
			photoCaption.setText("Caption: " + photo.getCaption());
		}
		//photoDate.setText(photo.getDate());
		
		ObservableList<String> albumNames = FXCollections.observableArrayList();
		for(int i = 0; i < user.getAlbums().size(); i++) {
			albumNames.add(user.getAlbums().get(i).getAlbumName());
		}
		copyAlbum.getItems().addAll(albumNames);
		copyAlbum.getSelectionModel().select(0);
		moveAlbum.getItems().addAll(albumNames);
		moveAlbum.getSelectionModel().select(0);

	}
	
	@FXML
	private void addTag(ActionEvent event) throws IOException {
		System.out.println("addTag");
	}
	
	@FXML
	private void editTag(ActionEvent event) throws IOException {
		System.out.println("editTag");
	}
	
	@FXML
	private void deleteTag(ActionEvent event) throws IOException {
		System.out.println("deleteTag");
	}
	
	@FXML
	private void copyTo(ActionEvent event) throws IOException {
		List<Album> albums = user.getAlbums();
		Alert alert;
		
		if(albums.size() == 1) {
			alert = new Alert(AlertType.ERROR);
			String content = "No Albums to Copy Photo to";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		if(album.getAlbumName().equals(copyAlbum.getValue())) {
			alert = new Alert(AlertType.ERROR);
			String content = "Photo Already Exists in Selected Album";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		for(int i = 0; i < albums.size(); i++) {
			Album curAlbum = albums.get(i);
			if(curAlbum.getAlbumName().equals(copyAlbum.getValue())) {
				List<Photo> photos = curAlbum.getPhotos();
				for(int j = 0; j < curAlbum.getPhotos().size(); j++) {
					if(photos.get(j).getPhotoName().equals(photo.getPhotoName())) {
						alert = new Alert(AlertType.ERROR);
						String content = "Photo Already Exists in Selected Album";
						alert.setContentText(content);
						alert.showAndWait();
						return;
					}
				}
				curAlbum.addPhoto(photo);
				break;
			}
		}
		
		alert = new Alert(AlertType.INFORMATION);
		String content = "Copied " + photo.getPhotoName() + " to " + copyAlbum.getValue();
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	@FXML
	private void moveTo(ActionEvent event) throws IOException {
		List<Album> albums = user.getAlbums();
		Alert alert;
		
		if(albums.size() == 1) {
			alert = new Alert(AlertType.ERROR);
			String content = "No Albums to Move Photo to";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		if(album.getAlbumName().equals(moveAlbum.getValue())) {
			alert = new Alert(AlertType.ERROR);
			String content = "Photo Already Exists in Selected Album";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		for(int i = 0; i < albums.size(); i++) {
			Album curAlbum = albums.get(i);
			if(curAlbum.getAlbumName().equals(moveAlbum.getValue())) {
				System.out.println(album.getAlbumName() + " " + curAlbum.getAlbumName() + " " + moveAlbum.getValue());
				curAlbum.addPhoto(photo);
				album.removePhoto(photo);
				
				alert = new Alert(AlertType.INFORMATION);
				String content = "Successfully Moved " + photo.getPhotoName() + " to " + curAlbum.getAlbumName();
				alert.setContentText(content);
				alert.showAndWait();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
				Parent parent = (Parent) loader.load();
				
				AlbumController controller = loader.getController();
				controller.setInfo(user, album);
				
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				
				controller.start(stage);
				
				stage.setScene(scene);
				stage.show();
				
				return;
			}
		}
	}
	
	@FXML
	private void leftArrow(ActionEvent event) throws IOException {
		this.photo = album.getPrevPhoto(photo);
		photoName.setText(photo.getPhotoName());
		if(photo.getCaption().length() == 0) {
			photoCaption.setText("No Caption");
		} else {
			photoCaption.setText("Caption: " + photo.getCaption());
		}
		//photoDate.setText(photo.getDate());
		imageView.setImage(photo.getImage());
		centerImage(imageView);
	}
	
	@FXML
	private void rightArrow(ActionEvent event) throws IOException {
		this.photo = album.getNextPhoto(photo);
		photoName.setText(photo.getPhotoName());
		if(photo.getCaption().length() == 0) {
			photoCaption.setText("No Caption");
		} else {
			photoCaption.setText("Caption: " + photo.getCaption());
		}
		//photoDate.setText(photo.getDate());
		imageView.setImage(photo.getImage());
		centerImage(imageView);
	}
	
	@FXML
	private void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
		Parent parent = (Parent) loader.load();
		
		AlbumController controller = loader.getController();
		controller.setInfo(user, album);
		
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		
		controller.start(stage);
		
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	private void logout(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		Parent parent = (Parent) loader.load();
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public static void centerImage(ImageView imageView) {
        Image image = imageView.getImage();
        if (image != null) {
            double w = 0;
            double h = 0;
            double ratioX = imageView.getFitWidth() / image.getWidth();
            double ratioY = imageView.getFitHeight() / image.getHeight();
            double reducCoeff = (ratioX >= ratioY) ? ratioY : ratioX;
            w = image.getWidth() * reducCoeff;
            h = image.getHeight() * reducCoeff;
            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);
        }
    }
	
	public void setInfo(User user, Album album, Photo photo) {
		this.user = user;
		this.album = album;
		this.photo = photo;
	}
}
