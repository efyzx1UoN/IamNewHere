package sample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Main;

public class Controller implements Initializable{
	
	@FXML
	Button btnStart;
	@FXML
	ColorPicker colorPicker;
	@FXML 
	TextField txtName;
	@FXML
	ListView<String> scoreList;
	
	
	private Color wallColor=Color.BLACK;
	
	private Stage stage;
	
	public void startClick(MouseEvent mouseEvent) {
		Main main=new Main();
		main.setWallColor(wallColor);
		try {
			main.start(new Stage());
			main.setPname(txtName.getText());
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
		loadScore();
		
		
	}
	
	private void loadScore() {
		try {
			BufferedReader bufferedReader=new BufferedReader(new FileReader("data/score.txt"));
			String line;
			while ((line=bufferedReader.readLine())!=null) {
				scoreList.getItems().add(line);
			}
			
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
