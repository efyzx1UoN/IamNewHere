package sample;

import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JOptionPane;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class StartMeUp {

    public static final String GAME_NAME = "BestSokobanEverV6";
    public static GameLogger logger;
    private static boolean debug = false;
    private Level currentLevel;
    private String mapSetName;
    private List<Level> levels;
    private boolean gameComplete = false;
    private int movesCount = 0;
    private MediaPlayer player;
    private int score=0;
    private int level=1;
    private ArrayList<Integer> scoreList=new ArrayList<Integer>();

    public StartMeUp(InputStream input, boolean production) {
        try {
            logger = new GameLogger();
            levels = loadGameFile(input);
            currentLevel = getNextLevel();

            if (production) {
                createPlayer();
            }
        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: " + e.getStackTrace());
        }




        loadScore();
    }

    public static boolean isDebugActive() {
        return debug;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public String getMapSetName() {
        return mapSetName;
    }

    public void handleKey(KeyCode code) {
        switch (code) {
            case UP:
                move(new Point(-1, 0));
                break;

            case RIGHT:
                move(new Point(0, 1));
                break;

            case DOWN:
                move(new Point(1, 0));
                break;

            case LEFT:
                move(new Point(0, -1));
                break;

            default:
                // TODO: implement something funny.
        }

        if (isDebugActive()) {
            System.out.println(code);
        }
    }

    public void move(Point delta) {
        if (isGameComplete()) {
            return;
        }

        getCurrentLevel().logMove();

        Point keeperPosition = currentLevel.getKeeperPosition();
        GameObject keeper = currentLevel.getObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = currentLevel.getObjectAt(targetObjectPoint);

        if (StartMeUp.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget, targetObjectPoint);
        }

        boolean keeperMoved = false;

        switch (keeperTarget) {

            case WALL:
                break;

            case CRATE:

                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                currentLevel.moveGameObjectBy(keeperTarget, targetObjectPoint, delta);
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            case FLOOR:
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                logger.severe("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }

        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            movesCount++;
            if (currentLevel.isComplete()) {
                if (isDebugActive()) {
                    System.out.println("Level complete!");
                }

                currentLevel = getNextLevel();
                score+=level++;
                scoreDialog();
            }
        }
    }

    private void scoreDialog() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("\nScoreList:\n");
        for (Integer score:scoreList) {
            stringBuilder.append(score+"\n");
        }
        JOptionPane.showMessageDialog(null, "Score:"+score+stringBuilder.toString());
    }

    public void saveScore() {
        if (score==0) {
            return;
        }
        scoreList.add(score);
        PrintWriter printWriter;
        Collections.sort(scoreList,Collections.reverseOrder());
        try {
            printWriter = new PrintWriter("score.txt");
            for (Integer score:scoreList) {
                printWriter.println(score);
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private void loadScore() {
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader("score.txt"));
            String line;
            while ((line=bufferedReader.readLine())!=null) {
                scoreList.add(Integer.parseInt(line));
            }
            bufferedReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block

        }

    }

    public List<Level> loadGameFile(InputStream input) {
        List<Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                // Break the loop if EOF is reached
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("MapSetName")) {
                    mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) {
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }

                    levelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                // If the line contains at least 2 WALLS, add it to the list
                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }

        } catch (IOException e) {
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }

        return levels;
    }

    public void undo() {
        getCurrentLevel().undo();
    }

    public boolean isGameComplete() {
        return gameComplete;
    }

    public void createPlayer() {
        //File filePath = new File(getClass().getClassLoader().getResource("music/puzzle_theme.wav").toString());
        try {
            Media music = new Media(getClass().getResource("./")+"puzzle_theme.wav");
            player = new MediaPlayer(music);
        } catch (Exception e) {
            // TODO: handle exception
        }

        //player.play();
        //player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
    }

    public void playMusic() {
        player.play();
    }

    public void stopMusic() {
        player.stop();
    }

    public boolean isPlayingMusic() {
//        return player.getStatus() == MediaPlayer.Status.PLAYING;
        return false;
    }

    public Level getNextLevel() {
        if (currentLevel == null) {
            return levels.get(0);
        }

        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex + 1);
        }

        gameComplete = true;
        return null;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void toggleDebug() {

        debug = !debug;
    }

}