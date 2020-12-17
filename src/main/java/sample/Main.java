package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Game.GameObject;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Zihui xu - Modified
 *  *   @version 1.0
 *  Start the game here
 */

public class Main extends Application {

    private Stage m_primaryStage;
    private StartMeUp m_gameEngine;
    private GridPane m_gameGrid;
    private File m_saveFile;
    private MenuBar m_MENU;
    private Color m_wallColor;

    /**
     *  SettingPname Here
      * @param pString is String Object
     */
    public void setPname(String pString) {
		m_gameEngine.setPname(pString);
	}

    /***
     * Setting color here
     * @param wallColor is Color Object
     */
   public void setWallColor(Color wallColor) {
	this.m_wallColor = wallColor;
	GraphicObject.setWallColor(wallColor);
}

    /***
     * Main Method
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    /***
     * Initialize game here
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
/*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
*/




        this.m_primaryStage = primaryStage;

        m_MENU = new MenuBar();

        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setDisable(true);
        menuItemSaveGame.setOnAction(actionEvent -> saveGame());
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> loadGame());
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(actionEvent -> closeGame());
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame,
                new SeparatorMenuItem(), menuItemExit);

        MenuItem menuItemUndo = new MenuItem("Undo");
        //menuItemUndo.setDisable(true);
        menuItemUndo.setOnAction(actionEvent -> undo());
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> toggleMusic(radioMenuItemMusic));
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> toggleDebug());
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        Menu menuLevel = new Menu("Level");
        menuLevel.setOnAction(actionEvent -> resetLevel());
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemResetLevel);

        MenuItem menuItemGame = new MenuItem("About This Game");
        Menu menuAbout = new Menu("About");
        menuAbout.setOnAction(actionEvent -> showAbout());
        menuAbout.getItems().addAll(menuItemGame);
        m_MENU.getMenus().addAll(menuFile, menuLevel, menuAbout);
        m_gameGrid = new GridPane();
        GridPane root = new GridPane();
        root.add(m_MENU, 0, 0);
        root.add(m_gameGrid, 0, 1);
        primaryStage.setTitle(StartMeUp.m_GAME_NAME);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->m_gameEngine.saveScore());
        loadDefaultSaveFile(primaryStage);
    }

    void loadDefaultSaveFile(Stage primaryStage) { this.m_primaryStage =
            primaryStage;
        System.out.println("Hi");
        System.out.println(getClass().getClassLoader().
                getResource("SampleGame.skb"));
        InputStream in = getClass().getClassLoader().
                getResourceAsStream("SampleGame.skb");
        System.out.println(in);
        initializeGame(in);
        System.out.println("Hi");
        setEventFilter();
        System.out.println("Hi");
    }

    /***
     * initializeGame
     * @param input
     */
    public void initializeGame(InputStream input) {
        m_gameEngine = new StartMeUp(input, true);
        reloadGrid();
    }

    /**
     * Setting Event here
     */
    public void setEventFilter() {
        m_primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            m_gameEngine.handleKey(event.getCode());
            reloadGrid();
        });
    }

    /**
     * Loading Game here
     * @throws FileNotFoundException
     */
    public void loadGameFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.
                ExtensionFilter("Sokoban save file", "*.skb"));
        m_saveFile = fileChooser.showOpenDialog(m_primaryStage);

        if (m_saveFile != null) {
            if (StartMeUp.isDebugActive()) {
                StartMeUp.m_logger.info("Loading save file: " +
                        m_saveFile.getName());
            }
            initializeGame(new FileInputStream(m_saveFile));
        }}

    /***
     * Showing Victory Message
     * Getting Level Message
     * Go to Next Level
     */
    private void reloadGrid() {
        if (m_gameEngine.isGameComplete()) {
            showVictoryMessage();
            return;
        }

        Level currentLevel = m_gameEngine.getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator)
                currentLevel.iterator();
        m_gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(),
                    levelGridIterator.getCurrentPosition());
        }
        m_gameGrid.autosize();
        m_primaryStage.sizeToScene();
    }

    /**
     * Showing Message
     */
    public void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage =
                "You completed " + m_gameEngine.getMapSetName()
                        + " in " + m_gameEngine.getMovesCount() + " moves!";
        MotionBlur mb = new MotionBlur(2, 3);

        newDialog(dialogTitle, dialogMessage, mb);
    }

    /**
     * Setting Scene here
     * @param dialogTitle is String Object
     * @param dialogMessage is String Object
     * @param dialogMessageEffect is Effect Object
     */
    public void newDialog(String dialogTitle, String dialogMessage,
                          Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(m_primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(14));

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Setting location
     * @param gameObject is gameObject Object
     * @param location is Point Object
     */
    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        
        m_gameGrid.add(graphicObject, location.y, location.x);
    }

    /**
     *
     * close game
     */
    public void closeGame() {
        System.exit(0);
    }
    public void saveGame() {
    }

    /**
     * load game
     */
    public void loadGame() {
        try {
            loadGameFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * undo,back to last step
     */
    public void undo() { 
    	m_gameEngine.undo();
    	reloadGrid();
    
    }
    
    public void resetLevel() {
    	
    	
    }

    /**
     * showing about message
     */
    public void showAbout() {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";

        newDialog(title, message, null);
    }

    /**
     * play music
     * @param radioMenuItemMusic  is RadioMenuItem Object
     */
    public void toggleMusic(RadioMenuItem radioMenuItemMusic) {
        // TODO
    	
    	if (radioMenuItemMusic.isSelected()) {
			m_gameEngine.playMusic();
		}else {
			m_gameEngine.stopMusic();
		}
    	
    }

    /**
     * Debug here
     */
    public void toggleDebug() {
        m_gameEngine.toggleDebug();
        reloadGrid();
    }








}
