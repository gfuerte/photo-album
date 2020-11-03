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
		//System.out.println("Username is: " + username.getText());
		//System.out.println("Password is: " + password.getText());
				
		if(username.getText().equals("admin")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
			Parent parent = (Parent) loader.load();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
			Parent parent = (Parent) loader.load();
			UserController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
			controller.start(stage);
			stage.setScene(scene);
			stage.show();
		}
	}
	
}
