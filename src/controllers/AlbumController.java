package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;
import models.Photo;
import models.User;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class AlbumController {
	@FXML
	private ListView<Photo> listView;
	@FXML
	private Text header;
	@FXML
	private Button addPhoto, deletePhoto, captionPhoto, back, logout;
	
	private User user;
	private Album album;
	private ObservableList<Photo> photos;
	
	private Stage stage;
	
	/**
	 * Initializes listView of Photos.
	 * @param mainStage Main Stage of Application.
	 */
	public void start(Stage mainStage) {
		stage = mainStage;
		header.setText(album.getAlbumName());
		List<Photo> list = album.getPhotos();
		photos = FXCollections.observableArrayList(list);
		
		listView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
			@Override
			public ListCell<Photo> call(ListView<Photo> album) {
				return new AlbumCell();
			}
		});
		
		listView.setItems(photos);
		listView.getSelectionModel().select(0);
		
	}
	
	/**
	 * Given selected image, adds photo to the user's album.
	 * @param event Pressed Add Photo button.
	 * @throws IOException
	 */
	@FXML
	private void addPhoto(ActionEvent event) throws IOException {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Add Photo");
		chooser.getExtensionFilters().addAll( new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		File file = chooser.showOpenDialog(stage);	
		if(file == null) return;
		
		String photoName = file.getName();
		Image image = new Image(file.toURI().toString());
		
		Calendar date = Calendar.getInstance();
		long millis = file.lastModified();
		date.setTimeInMillis(millis);
		date.set(Calendar.MILLISECOND, 0);
				
		Photo photo = new Photo(image, photoName, date);
		album.addPhoto(photo);
		photos.add(photo);
		listView.getSelectionModel().selectLast();
	}
	
	/**
	 * Given selected photo, deletes photo from the user's album.
	 * @param event Pressed Delete Photo button.
	 * @throws IOException
	 */
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
	
	/**
	 * Given user input, captions the current photo.
	 * @param event Pressed Caption Photo button
	 * @throws IOException
	 */
	@FXML
	private void captionPhoto(ActionEvent event) throws IOException {
		Photo photo = listView.getSelectionModel().getSelectedItem();
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Caption Photo");
		dialog.setContentText("Caption " + photo.getPhotoName() + " : ");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			photo.setCaption(dialog.getResult());
		}
	}
	
	/**
	 * Redirects user to the previous page.
	 * @param event Pressed Back button.
	 * @throws IOException
	 */
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
	
	/**
	 * Redirects user to the login page.
	 * @param event Pressed Logout button.
	 * @throws IOException
	 */
	@FXML
	private void logout(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		Parent parent = (Parent) loader.load();
		LoginController controller = loader.getController();
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		controller.start(stage);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Customized cell for Album Page listView
	 * @author Greg Fuerte
	 * @author Aries Regalado
	 */
	private class AlbumCell extends ListCell<Photo> {
		AnchorPane root = new AnchorPane();
		Text photoName = new Text();
		ImageView image = new ImageView();
		Button view = new Button("View");

		public AlbumCell() {
			super();
			AnchorPane.setLeftAnchor(photoName, 60.0);
			AnchorPane.setTopAnchor(photoName, 17.5);
			
			AnchorPane.setLeftAnchor(image, 0.0);
			AnchorPane.setTopAnchor(image, 0.0);
			
			AnchorPane.setRightAnchor(view, 10.0);
			AnchorPane.setTopAnchor(view, 14.0);

			image.setFitHeight(50.0);
			image.setFitWidth(50.0);

			image.setPreserveRatio(false);
			
			
			root.getChildren().addAll(photoName, image, view);
			root.setPrefHeight(50.0);
			setGraphic(root);
		}

		@Override
		public void updateItem(Photo photo, boolean empty) {
			super.updateItem(photo, empty);
			if (photo == null) {
				photoName.setText("");
				image.setImage(null);
				view.setVisible(false);
			} else {
				photoName.setText(photo.getPhotoName());
				image.setImage(photo.getImage());
				view.setVisible(true);
				
				view.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photo.fxml"));
					        Parent parent = (Parent) loader.load();
					        
					        PhotoController controller = loader.getController();
					        controller.setInfo(user, album, photo);
					        
					        Scene scene = new Scene(parent);
					        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					        
					        controller.start(stage);
					        
					        stage.setScene(scene);
					        stage.show();
						} catch (IOException e) { e.printStackTrace(); }
					}
				});
			}
		}
	}
	
	/**
	 * Sets this user and album to the given input.
	 * @param user Current User.
	 * @param album Current Album.
	 */
	public void setInfo(User user, Album album) {
		this.user = user;
		this.album = album;
	}
}
