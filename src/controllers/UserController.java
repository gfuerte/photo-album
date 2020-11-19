package controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;
import models.User;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class UserController {
	@FXML
	private ListView<Album> listView;
	@FXML
	private Text header;
	@FXML
	private Button logout, search, createAlbum, deleteAlbum, renameAlbum;

	private User user;
	private ObservableList<Album> albums;

	/**
	 * Initializes listView.
	 * @param mainStage Main Stage of Application.
	 */
	public void start(Stage mainStage) {
		header.setText(user.getUsername() + "'s Albums");
		List<Album> list = user.getAlbums();
		albums = FXCollections.observableArrayList(list);

		listView.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {
			@Override
			public ListCell<Album> call(ListView<Album> album) {
				return new UserCell();
			}
		});

		listView.setItems(albums);
		listView.getSelectionModel().select(0);
	}

	/**
	 * Redirects to search page.
	 * @param event Pressed Search button.
	 * @throws IOException
	 */
	@FXML
	private void search(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
		Parent parent = (Parent) loader.load();
		SearchController controller = loader.getController();
		controller.setUser(user);
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		controller.start(stage);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Given user input, creates a new album with given name.
	 * @param event Pressed Create Album button.
	 * @throws IOException
	 */
	@FXML
	private void createAlbum(ActionEvent event) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Create Album");
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
			user.addAlbum(album);
			albums.add(album);
			listView.getSelectionModel().selectLast();
		}
	}

	/**
	 * Given selected album, deletes album from album list.
	 * @param event Pressed Delete Album button.
	 * @throws IOException
	 */
	@FXML
	private void deleteAlbum(ActionEvent event) throws IOException {
		Alert alert;
		if(albums.size() == 0) {
			alert = new Alert(AlertType.ERROR);
			String content = "No albums to be deleted";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure you want to perform this action?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			Album album = listView.getSelectionModel().getSelectedItem();
			user.deleteAlbum(album.getAlbumName());
			albums.remove(album);
		}
	}

	/**
	 * Given user input, renames album to given input.
	 * @param event Pressed Rename Album button.
	 * @throws IOException
	 */
	@FXML
	private void renameAlbum(ActionEvent event) throws IOException {
		if(albums.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			String content = "No albums to be edited";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		Album album = listView.getSelectionModel().getSelectedItem();
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Rename " + album.getAlbumName());
		dialog.setContentText("Enter New Name: ");
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
			
			int index = listView.getSelectionModel().getSelectedIndex();
			album.setName(dialog.getResult());
			albums.remove(index);
			albums.add(index, album);
			listView.getSelectionModel().select(index);
		}
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
	 * Customized cell for User Page listView
	 * @author Greg Fuerte
	 * @author Aries Regalado
	 */
	private class UserCell extends ListCell<Album> {
		AnchorPane root = new AnchorPane();
		Button select = new Button("Select");
		Text albumName = new Text();
		Text photoCount = new Text();
		Text earliestDate = new Text();
		Text latestDate = new Text();

		public UserCell() {
			super();
			AnchorPane.setLeftAnchor(albumName, 15.0);
			AnchorPane.setTopAnchor(albumName, 17.5);
			
			AnchorPane.setLeftAnchor(photoCount, 95.0);
			AnchorPane.setTopAnchor(photoCount, 2.5);
			
			AnchorPane.setLeftAnchor(earliestDate, 95.0);
			AnchorPane.setTopAnchor(earliestDate, 17.5);
			
			AnchorPane.setLeftAnchor(latestDate, 95.0);
			AnchorPane.setTopAnchor(latestDate, 32.5);
			
			AnchorPane.setRightAnchor(select, 15.0);
			AnchorPane.setTopAnchor(select, 14.0);
			
			Font font = Font.font("Arial", FontWeight.BOLD, 12.0);
			albumName.setFont(font);
			
			root.getChildren().addAll(albumName, photoCount, earliestDate, latestDate, select);
			root.setPrefHeight(50.0);
			setGraphic(root);
		}

		@Override
		public void updateItem(Album album, boolean empty) {
			super.updateItem(album, empty);
			if (album == null) {
				albumName.setText("");
				photoCount.setText("");
				earliestDate.setText("");
				latestDate.setText("");
				select.setVisible(false);
			} else {
				if(album.getAlbumName().length() > 13) {
					albumName.setText(album.getAlbumName().substring(0, 13) + "...");
				} else {
					albumName.setText(album.getAlbumName());
				}
				
				if(album.getPhotoCount() == 1) {
					photoCount.setText(Integer.toString(album.getPhotoCount()) + " Photo");
				} else {
					photoCount.setText(Integer.toString(album.getPhotoCount()) + " Photos");
				}
				
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				
				if(album.getPhotoCount() == 0) {
					earliestDate.setText("(Earliest Date)");
					latestDate.setText("(Latest Date)");
				} else {
					earliestDate.setText("Earliest Date: " + format.format(album.getEarliestDate().getTime()));
					latestDate.setText("Latest Date: " + format.format(album.getLatestDate().getTime()));
				}
				
				select.setVisible(true);
				
				select.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
					        Parent parent = (Parent) loader.load();
					        
					        AlbumController controller = loader.getController();
					        controller.setInfo(user, album);
					        
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
	 * Sets this user to the given input.
	 * @param user Current User.
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
