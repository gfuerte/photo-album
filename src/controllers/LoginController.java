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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class LoginController {
	@FXML
	private ImageView logo;
	@FXML
	private Button login;
	@FXML
	private TextField username, password;
	
	List<User> userList = Photos.userList;
	HashMap<String, User> userMap = Photos.userMap;
	
	/**
	 * Initializes image view with logo.png
	 * @param mainStage Main Stage of Application.
	 */
	public void start(Stage mainStage) {
		Image image = new Image("/images/logo.png");
		logo.setImage(image);
	}
	
	/**
	 * Redirects user to either admin page or login page based on inputted username and password.
	 * @param event Pressed login button.
	 * @throws IOException
	 */
	@FXML
	private void login(ActionEvent event) throws IOException {		
		if(username.getText().equals("admin")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
			Parent parent = (Parent) loader.load();
			
			AdminController controller = loader.getController();
			
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			
			controller.start(stage);
			
			stage.setScene(scene);
			stage.show();
		} else if(userMap.get(username.getText()) != null){
			if(!userMap.get(username.getText()).getPassword().equals(password.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				String content = "Incorrect Password";
				alert.setContentText(content);
				alert.showAndWait();
				password.setText("");
				return;
			}
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
			Alert alert = new Alert(AlertType.ERROR);
			String content = "User Does Not Exist";
			alert.setContentText(content);
			alert.showAndWait();
			username.setText("");
			password.setText("");
			return;
		}
	}
	
}
