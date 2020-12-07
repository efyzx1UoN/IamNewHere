package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller implements Initializable{

	@FXML
	Button btnStart;
	@FXML
	ColorPicker colorPicker;

	private Color wallColor=Color.BLACK;

	private Stage stage;
     @FXML
	public void startClick(MouseEvent mouseEvent) {
		Main main=new Main();
		main.setWallColor(wallColor);
		try {
			main.start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stage=(Stage)btnStart.getScene().getWindow();
		stage.close();

	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void pickColor(ActionEvent actionEvent) {

		wallColor=colorPicker.getValue();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		colorPicker.setValue(Color.BLACK);


	}
}
