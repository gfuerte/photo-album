package app;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Serial;
import models.User;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class Photos extends Application {

	Stage mainStage;
	public static List<User> userList;
	public static HashMap<String, User> userMap = new HashMap<>();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		mainStage = primaryStage;
		
		/* Restart data.dat
		Serial serial = new Serial();
		serial.getUserList().add(new User("stock", ""));
		Serial.serialize(serial);		
		*/

		Serial serial = Serial.deserialize();
		userList = serial.getUserList();
		for(User user : userList) userMap.put(user.getUsername(), user);
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.fxml"));
			AnchorPane root = loader.load();
			
			LoginController controller = loader.getController();
			controller.start(primaryStage);
			
			Scene scene = new Scene(root, 350, 500);
			
			mainStage.setScene(scene);
			mainStage.setTitle("Photo Album");
			mainStage.setResizable(false);
			mainStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		mainStage.setOnCloseRequest(event -> {
			try {
				Serial.serialize(serial);
			} catch (IOException e) {}
		});
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
