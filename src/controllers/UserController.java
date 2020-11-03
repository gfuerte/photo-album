package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;

public class UserController {
	@FXML
	Button logout;
	@FXML
	ListView<Album> listView;
	
	private ObservableList<Album> albums;
	
	public void start(Stage mainStage) {
		//System.out.println("Test");
		List<Album> list = new ArrayList<>();
		list.add(new Album("Test Album 1", 5));
		list.add(new Album("Test Album2", 10));
		albums = FXCollections.observableArrayList(list);
		
		listView.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {
		    @Override
		    public ListCell<Album> call(ListView<Album> album) {
		        return new AlbumCell();
		    }
		});
		
		listView.setItems(albums);
		
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
			AnchorPane.setLeftAnchor(name, 100.0); //double args correspond to how far they are from the designated edge - left edge
			AnchorPane.setTopAnchor(name, 15.0);
			AnchorPane.setRightAnchor(num, 100.0);
			AnchorPane.setTopAnchor(num, 15.0);
			root.getChildren().addAll(name, num);
			root.setPrefHeight(50.0); //height of each cell
			setGraphic(root);
		}
		
		@Override
	    public void updateItem(Album album, boolean empty) {
	        super.updateItem(album, empty);
	        setText(null);
			if(album == null) {
				name.setText("");
				num.setText("");
			} else {
				name.setText(album.getName());
				num.setText(Integer.toString(album.getNumPhotos()));
			}
		}
	}
}
