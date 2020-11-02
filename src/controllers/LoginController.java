package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private Button login;
	@FXML
	private TextField username, password;
	
	@FXML
	private void login(ActionEvent event) throws IOException {
		System.out.println("Username is: " + username.getText());
		System.out.println("Password is: " + password.getText());
				
		if(username.getText().equals("admin")) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
			Parent sceneManager = (Parent) fxmlLoader.load();
			Scene adminScene = new Scene(sceneManager);
			Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			appStage.setScene(adminScene);
			appStage.show();
		}
	}
	
}
