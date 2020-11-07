package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;
import models.Photo;
import models.User;

public class AlbumController {
	@FXML
	ListView<Photo> listView;
	@FXML
	Text header;
	@FXML
	Button addPhoto, deletePhoto, captionPhoto, back, logout;
	
	private User user;
	private Album album;
	private ObservableList<Photo> photos;
	
	public void start(Stage mainStage) {
		header.setText(album.getAlbumName());
		List<Photo> list = album.getPhotos();
		photos = FXCollections.observableArrayList(list);
		
		listView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
			@Override
			public ListCell<Photo> call(ListView<Photo> album) {
				return new PhotoCell();
			}
		});
		
		listView.setItems(photos);
		listView.getSelectionModel().select(0);
	}
	
	@FXML
	private void addPhoto(ActionEvent event) throws IOException {
		System.out.println("add");
	}
	
	@FXML
	private void deletePhoto(ActionEvent event) throws IOException {
		Alert alert;
		if(photos.size() == 0) {
			alert = new Alert(AlertType.ERROR);
			String content = "No photos to be deleted";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure you want to perform this action?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			Photo photo = listView.getSelectionModel().getSelectedItem();
			album.removePhoto(photo);
			photos.remove(photo);
		}
	}
	
	@FXML
	private void captionPhoto(ActionEvent event) throws IOException {
		System.out.println("caption");
	}
	
	@FXML
	private void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
		Parent parent = (Parent) loader.load();
		
		UserController controller = loader.getController();
		controller.setUser(user);
		
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
	
	private class PhotoCell extends ListCell<Photo> {
		AnchorPane root = new AnchorPane();
		Text photoName = new Text();

		public PhotoCell() {
			super();
			AnchorPane.setLeftAnchor(photoName, 75.0);
			AnchorPane.setTopAnchor(photoName, 17.5);

			root.getChildren().addAll(photoName);
			root.setPrefHeight(50.0);
			setGraphic(root);
		}

		@Override
		public void updateItem(Photo photo, boolean empty) {
			super.updateItem(photo, empty);
			if (photo == null) {
				photoName.setText("");
			} else {
				photoName.setText(photo.getPhotoName());
			}
		}
	}
	
	public void setInfo(User user, Album album) {
		this.user = user;
		this.album = album;
	}
}
