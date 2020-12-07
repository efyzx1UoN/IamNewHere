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
import javafx.scene.image.ImageView;
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

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



public class Main extends Application {

    private Stage primaryStage;
    private StartMeUp gameEngine;
    private GridPane gameGrid;
    private File saveFile;
    private MenuBar MENU;
    private Color wallColor;

    public void setWallColor(Color wallColor) {
        this.wallColor = wallColor;
        GraphicObject.setWallColor(wallColor);
    }


    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
/*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
*/




        this.primaryStage = primaryStage;

        MENU = new MenuBar();

        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setDisable(true);
        menuItemSaveGame.setOnAction(actionEvent -> saveGame());
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> loadGame());
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(actionEvent -> closeGame());
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame, new SeparatorMenuItem(), menuItemExit);

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
        MENU.getMenus().addAll(menuFile, menuLevel, menuAbout);
        gameGrid = new GridPane();
        GridPane root = new GridPane();
        root.add(MENU, 0, 0);
        root.add(gameGrid, 0, 1);
        primaryStage.setTitle(StartMeUp.GAME_NAME);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->gameEngine.saveScore());
        loadDefaultSaveFile(primaryStage);
    }

    void loadDefaultSaveFile(Stage primaryStage) { this.primaryStage = primaryStage;
        System.out.println("Hi");
        InputStream in = getClass().getClassLoader().getResourceAsStream("sample/SampleGame.skb");
        System.out.println(in);
        initializeGame(in);
        System.out.println("Hi");
        setEventFilter();
        System.out.println("Hi");
    }

    public void initializeGame(InputStream input) {
        gameEngine = new StartMeUp(input, true);
        reloadGrid();
    }

    public void setEventFilter() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            gameEngine.handleKey(event.getCode());
            reloadGrid();
        });}
    public void loadGameFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showOpenDialog(primaryStage);

        if (saveFile != null) {
            if (StartMeUp.isDebugActive()) {
                StartMeUp.logger.info("Loading save file: " + saveFile.getName());
            }
            initializeGame(new FileInputStream(saveFile));
        }}
    private void reloadGrid() {
        if (gameEngine.isGameComplete()) {
            showVictoryMessage();
            return;
        }

        Level currentLevel = gameEngine.getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }
        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    public void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed " + gameEngine.getMapSetName() + " in " + gameEngine.getMovesCount() + " moves!";
        MotionBlur mb = new MotionBlur(2, 3);

        newDialog(dialogTitle, dialogMessage, mb);
    }

    public void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
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

    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);

        gameGrid.add(graphicObject, location.y, location.x);
    }

    public void closeGame() {
        System.exit(0);
    }
    public void saveGame() {
    }
    public void loadGame() {
        try {
            loadGameFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void undo() {
        gameEngine.undo();
        reloadGrid();

    }

    public void resetLevel() {


    }

    public void showAbout() {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";

        newDialog(title, message, null);
    }
    public void toggleMusic(RadioMenuItem radioMenuItemMusic) {
        // TODO

        if (radioMenuItemMusic.isSelected()) {
            gameEngine.playMusic();
        }else {
            gameEngine.stopMusic();
        }

    }
    public void toggleDebug() {
        gameEngine.toggleDebug();
        reloadGrid();
    }








}
