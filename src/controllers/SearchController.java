package controllers;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import models.Album;
import models.Photo;
import models.Tag;
import models.User;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class SearchController {
	@FXML
	private ListView<Photo> listView;
	@FXML
	private Button search, create, back, logout;
	@FXML
	private DatePicker fromDate, toDate;
	@FXML
	private TextField tagKey1, tagValue1, tagKey2, tagValue2;
	@FXML
	private ComboBox<String> junction;
	
	private User user;
	private ObservableList<Photo> photos;
	
	/**
	 * Initializes listView and comboBox
	 * @param mainStage Main Stage of Application.
	 */
	public void start(Stage mainStage) {
		junction.getItems().addAll("AND", "OR");
		junction.getSelectionModel().select("AND");
		
		photos = FXCollections.observableArrayList(new ArrayList<Photo>());
		
		listView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
			@Override
			public ListCell<Photo> call(ListView<Photo> album) {
				return new PhotoCell();
			}
		});
		
		listView.setItems(photos);
		listView.getSelectionModel().select(0);
	}
	
	/**
	 * Given different combinations of inputs, returns the photos that fits the criteria of the inputs.
	 * @param event Pressed Search button.
	 * @throws IOException
	 */
	@FXML
	private void search(ActionEvent event) throws IOException {
		String tag1 = tagKey1.getText().trim();
		String value1 = tagValue1.getText().trim();
		String tag2 = tagKey2.getText().trim();
		String value2 = tagValue2.getText().trim();
		
		if(fromDate.getValue() == null && toDate.getValue() == null && tag1.isEmpty() && value1.isEmpty() && tag2.isEmpty() && value2.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			String content = "No Criterias to Search for";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		
		if(fromDate.getValue() != null && toDate.getValue() != null) {
			Date from = Date.from(fromDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date to = Date.from(toDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			if(!tag1.isEmpty() && !value1.isEmpty() && !tag2.isEmpty() && !value2.isEmpty()) {
				photos.clear();
				if(junction.getValue().equals("AND")) {
					for(int i = 0; i < user.getAlbums().size(); i++) {
						Album album = user.getAlbums().get(i);
						for(int j = 0; j < album.getPhotos().size(); j++) {
							Photo photo = album.getPhotos().get(j);
							if(from.before(photo.getDate().getTime()) && to.after(photo.getDate().getTime())) {
								HashMap<Pair<String, String>, Boolean> map = new HashMap<>();
								for(int k = 0; k < photo.getTags().size(); k++) {
									Tag tag = photo.getTags().get(k);
									Pair<String, String> pair = new Pair<>(tag.getKey(), tag.getValue());
									map.put(pair, true);
								}
								if(map.get(new Pair<>(tag1, value1)) != null && map.get(new Pair<>(tag2, value2)) != null) {
									photos.add(photo);
								}
							}
						}
					}
				} else if(junction.getValue().equals("OR")) {
					for(int i = 0; i < user.getAlbums().size(); i++) {
						Album album = user.getAlbums().get(i);
						for(int j = 0; j < album.getPhotos().size(); j++) {
							Photo photo = album.getPhotos().get(j);
							if(from.before(photo.getDate().getTime()) && to.after(photo.getDate().getTime())) {
								for(int k = 0; k < photo.getTags().size(); k++) {
									Tag tag = photo.getTags().get(k);
									if((tag.getKey().equals(tag1) && tag.getValue().equals(value1)) || (tag.getKey().equals(tag2) && tag.getValue().equals(value2))) {
										photos.add(photo);
										break;
									}
								}
							}
						}
					}
				}
			} else if((!tag1.isEmpty() && !value1.isEmpty()) || (!tag2.isEmpty() && !value2.isEmpty())) {
				if((!tag1.isEmpty() && value1.isEmpty()) || (tag1.isEmpty() && !value1.isEmpty()) 
						|| (!tag2.isEmpty() && value2.isEmpty()) || (tag2.isEmpty() && !value2.isEmpty())) {
					Alert alert = new Alert(AlertType.ERROR);
					String content = "Incomplete Tag-Value Pair";
					alert.setContentText(content);
					alert.showAndWait();
					return;
				}
				photos.clear();
				if(!tag1.isEmpty() && !value1.isEmpty()) {
					for(int i = 0; i < user.getAlbums().size(); i++) {
						Album album = user.getAlbums().get(i);
						for(int j = 0; j < album.getPhotos().size(); j++) {
							Photo photo = album.getPhotos().get(j);
							if(from.before(photo.getDate().getTime()) && to.after(photo.getDate().getTime())) {
								for(int k = 0; k < photo.getTags().size(); k++) {
									Tag tag = photo.getTags().get(k);
									if(tag.getKey().equals(tag1) && tag.getValue().equals(value1)) {
										photos.add(photo);
									}
								}
							}
						}
					}
				} else if(!tag2.isEmpty() && !value2.isEmpty()) {
					for(int i = 0; i < user.getAlbums().size(); i++) {
						Album album = user.getAlbums().get(i);
						for(int j = 0; j < album.getPhotos().size(); j++) {
							Photo photo = album.getPhotos().get(j);
							if(from.before(photo.getDate().getTime()) && to.after(photo.getDate().getTime())) {
								for(int k = 0; k < photo.getTags().size(); k++) {
									Tag tag = photo.getTags().get(k);
									if(tag.getKey().equals(tag2) && tag.getValue().equals(value2)) {
										photos.add(photo);
									}
								}
							}
						}
					}
				}
			} else if(!tag1.isEmpty() || !value1.isEmpty() || !tag2.isEmpty() || !value2.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				String content = "Incomplete Tag-Value Pair";
				alert.setContentText(content);
				alert.showAndWait();
				return;
			} else {
				photos.clear();
				for(int i = 0; i < user.getAlbums().size(); i++) {
					Album album = user.getAlbums().get(i);
					for(int j = 0; j < album.getPhotos().size(); j++) {
						Photo photo = album.getPhotos().get(j);
						if(from.before(photo.getDate().getTime()) && to.after(photo.getDate().getTime())) {
							photos.add(photo);
						}
					}
				}
			}					
		} else if(fromDate.getValue() != null || toDate.getValue() != null) {
			Alert alert = new Alert(AlertType.ERROR);
			String content = "Incomplete Date Range";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		} else if(fromDate.getValue() == null || toDate.getValue() == null) {	
			if(!tag1.isEmpty() && !value1.isEmpty() && !tag2.isEmpty() && !value2.isEmpty()) {
				photos.clear();
				if(junction.getValue().equals("AND")) {
					for(int i = 0; i < user.getAlbums().size(); i++) {
						Album album = user.getAlbums().get(i);
						for(int j = 0; j < album.getPhotos().size(); j++) {
							Photo photo = album.getPhotos().get(j);
							HashMap<Pair<String, String>, Boolean> map = new HashMap<>();
							for(int k = 0; k < photo.getTags().size(); k++) {
								Tag tag = photo.getTags().get(k);
								Pair<String, String> pair = new Pair<>(tag.getKey(), tag.getValue());
								map.put(pair, true);
							}
							if(map.get(new Pair<>(tag1, value1)) != null && map.get(new Pair<>(tag2, value2)) != null) {
								photos.add(photo);
							}
						}
					}
				} else if(junction.getValue().equals("OR")) {
					for(int i = 0; i < user.getAlbums().size(); i++) {
						Album album = user.getAlbums().get(i);
						for(int j = 0; j < album.getPhotos().size(); j++) {
							Photo photo = album.getPhotos().get(j);
							for(int k = 0; k < photo.getTags().size(); k++) {
								Tag tag = photo.getTags().get(k);
								if((tag.getKey().equals(tag1) && tag.getValue().equals(value1)) || (tag.getKey().equals(tag2) && tag.getValue().equals(value2))) {
									photos.add(photo);
									break;
								}
							}
						}
					}
				}
			} else if((!tag1.isEmpty() && !value1.isEmpty()) || (!tag2.isEmpty() && !value2.isEmpty())) {
				if((!tag1.isEmpty() && value1.isEmpty()) || (tag1.isEmpty() && !value1.isEmpty()) 
						|| (!tag2.isEmpty() && value2.isEmpty()) || (tag2.isEmpty() && !value2.isEmpty())) {
					Alert alert = new Alert(AlertType.ERROR);
					String content = "Incomplete Tag-Value Pair";
					alert.setContentText(content);
					alert.showAndWait();
					return;
				}
				photos.clear();
				if(!tag1.isEmpty() && !value1.isEmpty()) {
					for(int i = 0; i < user.getAlbums().size(); i++) {
						Album album = user.getAlbums().get(i);
						for(int j = 0; j < album.getPhotos().size(); j++) {
							Photo photo = album.getPhotos().get(j);
							for(int k = 0; k < photo.getTags().size(); k++) {
								Tag tag = photo.getTags().get(k);
								if(tag.getKey().equals(tag1) && tag.getValue().equals(value1)) {
									photos.add(photo);
								}
							}
						}
					}
				} else if(!tag2.isEmpty() && !value2.isEmpty()) {
					for(int i = 0; i < user.getAlbums().size(); i++) {
						Album album = user.getAlbums().get(i);
						for(int j = 0; j < album.getPhotos().size(); j++) {
							Photo photo = album.getPhotos().get(j);
							for(int k = 0; k < photo.getTags().size(); k++) {
								Tag tag = photo.getTags().get(k);
								if(tag.getKey().equals(tag2) && tag.getValue().equals(value2)) {
									photos.add(photo);
								}
							}	
						}
					}
				}
			} else if(!tag1.isEmpty() || !value1.isEmpty() || !tag2.isEmpty() || !value2.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				String content = "Incomplete Tag-Value Pair";
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
		}
	}
	
	/**
	 * Creates a new album from the given search result photos.
	 * @param event Pressed Create Album button.
	 * @throws IOException
	 */
	@FXML
	private void create(ActionEvent event) throws IOException {
		if(photos.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			String content = "No Search Results";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Create Album From Search Results");
		dialog.setContentText("Enter New Album Name:");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		Optional<String> result = dialog.showAndWait();
		
		if(result.isPresent()) {
			if(dialog.getResult().trim().length() == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				String content = "No Name Inputted";
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
			
			for(int i = 0; i < user.getAlbums().size(); i++) {
				if(user.getAlbums().get(i).getAlbumName().equals(dialog.getResult())) {
					Alert alert = new Alert(AlertType.ERROR);
					String content = "Album Already Exist";
					alert.setContentText(content);
					alert.showAndWait();
					return;
				}
			}
			
			Album album = new Album(dialog.getResult());
			for(int i = 0; i < photos.size(); i++) {
				album.addPhoto(photos.get(i));
			}
			user.addAlbum(album);
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Notification");
			alert.setHeaderText("");
			alert.setContentText("Succesfully Created Album");
			alert.showAndWait();
			
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
	 * Customized cell for Search Page listView
	 * @author Greg Fuerte
	 * @author Aries Regalado
	 */
	private class PhotoCell extends ListCell<Photo> {
		AnchorPane root = new AnchorPane();
		Text photoName = new Text();
		ImageView image = new ImageView();

		public PhotoCell() {
			super();
			AnchorPane.setLeftAnchor(photoName, 150.0);
			AnchorPane.setTopAnchor(photoName, 42.5);
			
			AnchorPane.setLeftAnchor(image, 0.0);
			AnchorPane.setTopAnchor(image, 0.0);

			image.setFitHeight(100.0);
			image.setFitWidth(100.0);

			image.setPreserveRatio(false);
			
			root.getChildren().addAll(photoName, image);
			root.setPrefHeight(100.0);
			setGraphic(root);
		}
		
		@Override
		public void updateItem(Photo photo, boolean empty) {
			super.updateItem(photo, empty);
			if (photo == null) {
				photoName.setText("");
				image.setImage(null);
			} else {
				photoName.setText(photo.getPhotoName());
				image.setImage(photo.getImage());
			}
		}
	}
	
	/**
	 * Sets this user to the given input.
	 * @param user Current User.
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
