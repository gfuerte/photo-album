package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.User;

public class AdminController {
    @FXML
    private ListView<User> listView;
    @FXML
    private TextField addUsernameTF, addUserPasswordTF, deleteUsernameTF, deleteUserPasswordTF;
    @FXML
    private Button addUserButton, deleteUserButton;

    List<User> userList = Photos.userList;
	HashMap<String, User> userMap = Photos.userMap;

	private ObservableList<User> allUsers;
    
	public void start(Stage mainStage) {
		allUsers = FXCollections.observableArrayList(userList);
		
		listView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
			@Override
			public ListCell<User> call(ListView<User> user) {
				return new UserCell();
			}
		});

		listView.setItems(allUsers);
		listView.getSelectionModel().select(0);
    }

    @FXML
    void addUserButtonAction(ActionEvent event) {
    	String userName = addUsernameTF.getText();
    	String userPassword = addUserPasswordTF.getText();
    	
    	if(userName.isEmpty()) {
    		Alert alert = new Alert(AlertType.ERROR);
    		String s = "Please make sure username field is not empty when adding user";
    		alert.setContentText(s);
    		alert.showAndWait();
    		return;
    	}
    	
    	if(userMap.get(userName) != null) {
    		Alert alert = new Alert(AlertType.ERROR);
    		String t = "This username already exists please try again";
    		alert.setContentText(t);
    		alert.showAndWait();
    	} else {
	    	User user = new User(userName, userPassword);
	    	userList.add(user);
	    	allUsers.add(user);
	    	userMap.put(userName, user);
	    	Alert alert = new Alert(AlertType.INFORMATION);
			String s = "Add Succesful";
			alert.setContentText(s);
			alert.showAndWait();
    	}
    	addUsernameTF.setText("");
		addUserPasswordTF.setText("");
    }

    @FXML
    void deleteUserButtonAction(ActionEvent event) {
    	String userName = deleteUsernameTF.getText();
    	String userPassword = deleteUserPasswordTF.getText();
    	
    	if(userName.isEmpty()) {
    		Alert alert = new Alert(AlertType.ERROR);
    		String s = "Please make sure username field is not empty when deleteing user";
    		alert.setContentText(s);
    		alert.showAndWait();
    		return;
    	}
    	if(userMap.get(userName) == null) {
    		Alert alert = new Alert(AlertType.ERROR);
    		String t = "The username you want to delete does not exist please try again";
    		alert.setContentText(t);
    		alert.showAndWait();
    		deleteUsernameTF.setText("");
        	deleteUserPasswordTF.setText("");
    		return;
    	}
    	    	
    	for(int i = 0; i < userList.size(); i++) {
    		User user = userList.get(i);
    		if(user.getUsername().equals(userName) && user.getPassword().equals(userPassword)) {
    			userMap.remove(userName);
    			userList.remove(i);
    			allUsers.remove(user);
    			
    			deleteUsernameTF.setText("");
    	    	deleteUserPasswordTF.setText("");
    	    	
    	    	Alert alert = new Alert(AlertType.INFORMATION);
    			String s = "Delete Succesful";
    			alert.setContentText(s);
    			alert.showAndWait();
    	    	
    	    	return;
    		}
    	}

    }
    
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
    
    private class UserCell extends ListCell<User> {
		AnchorPane root = new AnchorPane();
		Text text = new Text();

		public UserCell() {
			super();
			AnchorPane.setLeftAnchor(text, 10.0);
			AnchorPane.setTopAnchor(text, 0.0);
			
			root.getChildren().addAll(text);
			setGraphic(root);
		}

		@Override
		public void updateItem(User user, boolean empty) {
			super.updateItem(user, empty);
			if (user == null) {
				text.setText("");
			} else {
				text.setText("Username: " + user.getUsername() + "\n" + "Password: " + user.getPassword());
			}
		}
	}
}
