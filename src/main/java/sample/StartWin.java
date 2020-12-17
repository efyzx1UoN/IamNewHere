package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Zihui xu - Modified
 *   @version 1.0
 *   Start the initialization Interface
 */
public class StartWin extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		//FXMLLoader fxmlLoader = new FXMLLoader();
		//String viewerFxml = "start.fxml";
		System.out.println(getClass().getResource("/fxml/start.fxml"));
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));
		//AnchorPane page = (AnchorPane) fxmlLoader.load(this.getClass().getResource(viewerFxml).openStream());
		//Controller controller=fxmlLoader.getController(); 
		//controller.setStage(primaryStage);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
