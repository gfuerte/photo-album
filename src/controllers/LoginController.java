package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import app.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;

public class LoginController {
	@FXML
	private Button login;
	@FXML
	private TextField username, password;
	
	List<User> userList = Photos.userList;
	HashMap<String, User> userMap = Photos.userMap;
	
	@FXML
	private void login(ActionEvent event) throws IOException {		
		if(username.getText().equals("admin")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
			Parent parent = (Parent) loader.load();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} else if(userMap.get(username.getText()) != null){
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
			Parent parent = (Parent) loader.load();
			
			UserController controller = loader.getController();
			controller.setUser(userMap.get(username.getText()));
			
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
			
			controller.start(stage);
			
			stage.setScene(scene);
			stage.show();
		} else {
			System.out.println("User doesn't exist");
		}
	}
	
}
