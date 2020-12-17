package sample.Controller;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zihui xu - Modified
 *  *   @version 1.0
 *  Setting Gui here
 */
public class Controller implements Initializable{
	
	@FXML
	Button m_btnStart;
	@FXML
	ColorPicker m_colorPicker;
	@FXML 
	TextField m_txtName;
	@FXML
	ListView<String> m_scoreList;
	
	
	private Color m_wallColor=Color.BLACK;
	
	private Stage m_stage;

	/**
	 * Start button to start game
	 * @param mouseEvent is MouseEvent Object
	 */
	public void startClick(MouseEvent mouseEvent) {
		Main main=new Main();
		main.setWallColor(m_wallColor);
		try {
			main.start(new Stage());
			main.setPname(m_txtName.getText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_stage=(Stage)m_btnStart.getScene().getWindow();
		m_stage.close();
		
		
	}

	/**
	 * Setting stage
	 * @param stage is Stage Object
	 */
	public void setStage(Stage stage) {
		this.m_stage = stage;
	}

	/**
	 * Getting Color value here
	 * @param actionEvent is Action Object
	 */
	public void pickColor(ActionEvent actionEvent) {
		
		m_wallColor=m_colorPicker.getValue();
	}

	/**
	 * Load Score here
	 * @param location  is URL Object
	 * @param resources is ResourceBundle Object
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		m_colorPicker.setValue(Color.BLACK);
		loadScore();
		
		
	}

	/**
	 * Initial Score here
	 */
	private void loadScore() {
		try {
			BufferedReader bufferedReader=new BufferedReader(
					new FileReader("data/score.txt"));
			String line;
			while ((line=bufferedReader.readLine())!=null) {
				m_scoreList.getItems().add(line);
			}
			
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
