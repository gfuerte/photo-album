package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import models.Album;
import models.Photo;
import models.Tag;
import models.User;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class PhotoController {
	@FXML
	private ImageView imageView;
	@FXML
	private Button addTag, editTag, deleteTag, copyTo, moveTo, leftArrow, rightArrow, back, logout;
	@FXML
	private Text photoName, photoDate, photoCaption;
	@FXML
	private TableView<Tag> tagTable;
	@FXML
	private TableColumn<Tag, String> keyColumn, valueColumn;
	@FXML 
	private ComboBox<String> copyAlbum, moveAlbum;
	
	private User user;
	private Album album;
	private Photo photo;
	private ObservableList<Tag> tags;
	
	/**
	 * Initializes imageView, tableView, and comboBox
	 * @param mainStage Main Stage of Application.
	 */
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
		photoDate.setText(photo.getDate().getTime().toString());
		
		ObservableList<String> albumNames = FXCollections.observableArrayList();
		for(int i = 0; i < user.getAlbums().size(); i++) {
			albumNames.add(user.getAlbums().get(i).getAlbumName());
		}
		copyAlbum.getItems().addAll(albumNames);
		copyAlbum.getSelectionModel().select(0);
		moveAlbum.getItems().addAll(albumNames);
		moveAlbum.getSelectionModel().select(0);
		
		keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
		valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		
		tags = FXCollections.observableArrayList(photo.getTags());
		tagTable.setItems(tags);
		tagTable.getSelectionModel().selectFirst();
	}
	
	/**
	 * Given user input, adds new tag to the current photo.
	 * @param event Pressed Add Tag button.
	 * @throws IOException
	 */
	@FXML
	private void addTag(ActionEvent event) throws IOException {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
	    dialog.setTitle("Add Tag");

	    ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
	    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

	    GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 20, 10, 10));

	    TextField key = new TextField();
	    TextField value = new TextField();

	    gridPane.add(new Label("Tag Key: "), 0, 0);
	    gridPane.add(key, 1, 0);
	    gridPane.add(new Label("Tag Value: "), 0, 1);
	    gridPane.add(value, 1, 1);

	    dialog.getDialogPane().setContent(gridPane);

	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == loginButtonType) {
	            return new Pair<>(key.getText(), value.getText());
	        }
	        return null;
	    });

	    Optional<Pair<String, String>> result = dialog.showAndWait();

	    result.ifPresent(pair -> {
	        Tag tag = new Tag(pair.getKey().trim(), pair.getValue().trim());
	        if(photo.tagDuplicate(tag)) {
	        	Alert alert = new Alert(AlertType.ERROR);
				String content = "Tag Key with Tag Value Already Exist";
				alert.setContentText(content);
				alert.showAndWait();
				return;
	        } else {
	        	photo.addTag(tag);
	        	tags.add(tag);
	        	tagTable.getSelectionModel().selectLast();
	        }
	    });
	}
	
	/**
	 * Given user input, edits the current tag in the photo.
	 * @param event Pressed Edit Tag button.
	 * @throws IOException
	 */
	@FXML
	private void editTag(ActionEvent event) throws IOException {
		if(tags.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			String content = "No Tags to be Deleted";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		Tag tag = tagTable.getSelectionModel().getSelectedItem();	
		Dialog<Pair<String, String>> dialog = new Dialog<>();
	    dialog.setTitle("Edit Tag");

	    ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
	    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

	    GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 20, 10, 10));

	    TextField key = new TextField();
	    key.setPromptText(tag.getKey());
	    TextField value = new TextField();
	    value.setPromptText(tag.getValue());

	    gridPane.add(new Label("Tag Key: "), 0, 0);
	    gridPane.add(key, 1, 0);
	    gridPane.add(new Label("Tag Value: "), 0, 1);
	    gridPane.add(value, 1, 1);

	    dialog.getDialogPane().setContent(gridPane);

	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == loginButtonType) {
	            return new Pair<>(key.getText(), value.getText());
	        }
	        return null;
	    });

	    Optional<Pair<String, String>> result = dialog.showAndWait();

	    result.ifPresent(pair -> {
	    	Tag temp = new Tag(pair.getKey(), pair.getValue());
	        if(photo.tagDuplicate(temp)) {
	        	Alert alert = new Alert(AlertType.ERROR);
				String content = "Tag Key with Tag Value Already Exist";
				alert.setContentText(content);
				alert.showAndWait();
				return;
	        } else {
	        	tag.setKey(pair.getKey().trim());
	        	tag.setValue(pair.getValue().trim());
	        	tagTable.refresh();
	        }
	    });
	}
	
	/**
	 * Given user input, deletes current selected tag.
	 * @param event Pressed Delete Tag button.
	 * @throws IOException
	 */
	@FXML
	private void deleteTag(ActionEvent event) throws IOException {
		if(tags.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			String content = "No Tags to be Deleted";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		Tag tag = tagTable.getSelectionModel().getSelectedItem();
		photo.deleteTag(tag);
		tags.remove(tagTable.getSelectionModel().getSelectedIndex());
	}
	
	/**
	 * Copies current photo to selected album.
	 * @param event Pressed Copy To button.
	 * @throws IOException
	 */
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
	
	/**
	 * Moves current photo to selected album. Redirects to album page afterwards.
	 * @param event Pressed Move To button.
	 * @throws IOException
	 */
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
	
	/**
	 * Displays the previous photo in the album.
	 * @param event Pressed left arrow button.
	 * @throws IOException
	 */
	@FXML
	private void leftArrow(ActionEvent event) throws IOException {
		this.photo = album.getPrevPhoto(photo);
		photoName.setText(photo.getPhotoName());
		if(photo.getCaption().length() == 0) {
			photoCaption.setText("No Caption");
		} else {
			photoCaption.setText("Caption: " + photo.getCaption());
		}
		photoDate.setText(photo.getDate().getTime().toString());
		tags = FXCollections.observableArrayList(photo.getTags());
		tagTable.setItems(tags);
		imageView.setImage(photo.getImage());
		centerImage(imageView);
	}
	
	/**
	 * Displays the next photo in the album.
	 * @param event Pressed right arrow button.
	 * @throws IOException
	 */
	@FXML
	private void rightArrow(ActionEvent event) throws IOException {
		this.photo = album.getNextPhoto(photo);
		photoName.setText(photo.getPhotoName());
		if(photo.getCaption().length() == 0) {
			photoCaption.setText("No Caption");
		} else {
			photoCaption.setText("Caption: " + photo.getCaption());
		}
		photoDate.setText(photo.getDate().getTime().toString());
		tags = FXCollections.observableArrayList(photo.getTags());
		tagTable.setItems(tags);
		imageView.setImage(photo.getImage());
		centerImage(imageView);
	}
	
	/**
	 * Redirects user to the previous page.
	 * @param event Pressed Back button.
	 * @throws IOException
	 */
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
	 * Centers the current image in the imageView.
	 * @param imageView ImageView in the fxml.
	 */
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
	
	/**
	 * Sets this user, album, photo to the given input.
	 * @param user Current User.
	 * @param album Current Album.
	 * @param photo Current Photo.
	 */
	public void setInfo(User user, Album album, Photo photo) {
		this.user = user;
		this.album = album;
		this.photo = photo;
	}
}
