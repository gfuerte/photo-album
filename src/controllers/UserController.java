package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;
import models.User;

public class UserController {
	@FXML
	Button logout, search, addAlbum, deleteAlbum, renameAlbum;
	@FXML
	Text header;
	@FXML
	ListView<Album> listView;

	private User user;
	private ObservableList<Album> albums;
	Stage stage;

	public void start(Stage mainStage) {
		stage = mainStage;
		header.setText(user.getUsername() + "'s Albums");
		List<Album> list = user.getAlbums();
		albums = FXCollections.observableArrayList(list);

		listView.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {
			@Override
			public ListCell<Album> call(ListView<Album> album) {
				return new AlbumCell();
			}
		});

		listView.setItems(albums);
		listView.getSelectionModel().select(0);
	}

	@FXML
	private void search(ActionEvent event) throws IOException {
		System.out.println("search");
	}

	@FXML
	private void addAlbum(ActionEvent event) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Album");
		dialog.setContentText("Enter New Album Name:");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		Optional<String> result = dialog.showAndWait();
		
		if(result.isPresent()) {
			Album album = new Album(dialog.getResult());
			user.addAlbum(album);
			albums.add(album);
			listView.getSelectionModel().selectLast();
		}
	}

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
			int index = listView.getSelectionModel().getSelectedIndex();
			album.setName(dialog.getResult());
			albums.remove(index);
			albums.add(index, album);
			listView.getSelectionModel().select(index);
		}
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

	private class AlbumCell extends ListCell<Album> {
		AnchorPane root = new AnchorPane();
		Text name = new Text();
		Text num = new Text();

		public AlbumCell() {
			super();
			AnchorPane.setLeftAnchor(name, 100.0); // double args correspond to how far they are from the designated
													// edge - left edge
			AnchorPane.setTopAnchor(name, 15.0);
			AnchorPane.setRightAnchor(num, 100.0);
			AnchorPane.setTopAnchor(num, 15.0);
			root.getChildren().addAll(name, num);
			root.setPrefHeight(50.0); // height of each cell
			setGraphic(root);
		}

		@Override
		public void updateItem(Album album, boolean empty) {
			super.updateItem(album, empty);
			if (album == null) {
				name.setText("");
				num.setText("");
			} else {
				name.setText(album.getAlbumName());
				num.setText(Integer.toString(album.getPhotoCount()));
			}
		}
	}

	public void setUser(User user) {
		this.user = user;
	}
}
