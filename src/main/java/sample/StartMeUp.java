package sample;

import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sample.Game.*;

import javax.swing.JOptionPane;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Zihui xu - Modified
 *    @version 1.0
 *    Setting Interface Character here
 */
public class StartMeUp {
	class Score implements Comparable<Score>{
		private String name;
		private int sc;

        /**
         * Get Name and Score
         * @param name is String object
         * @param sc is integer Object
         */
		public Score(String name,int sc) {
			// TODO Auto-generated constructor stub
			this.name=name;
			this.sc=sc;
		}

        /**
         * Compare Score
         * @param o
         * @return
         */
		@Override
		public int compareTo(Score o) {
			// TODO Auto-generated method stub
			return sc-o.sc;
		}

        /**
         *  ToString Method
         * @return
         */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return name+","+sc;
		}
		
		
		
	}

    public static final String m_GAME_NAME = "BestSokobanEverV6";
    public static GameLogger m_logger;
    private static boolean debug = false;
    private Level m_currentLevel;
    private String m_mapSetName;
    private List<Level> m_levels;
    private boolean m_gameComplete = false;
    private int m_movesCount = 0;
    private MediaPlayer m_player;
    private int m_score=0;
    private int m_level=1;
    private ArrayList<Score> m_scoreList=new ArrayList<Score>();
    
    private String m_pname;

    /**
     * Getting pname
     * @param pname is String Object
     */
    public void setPname(String pname) {
		this.m_pname = pname;
	}


    /**
     * Create Player
     * @param input
     * @param production
     */
    public StartMeUp(InputStream input, boolean production) {
        try {
            m_logger = new GameLogger();
            m_levels = loadGameFile(input);
            m_currentLevel = getNextLevel();

            if (production) {
                createPlayer();
            }
        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            m_logger.warning("Cannot load the default save file: " + e.getStackTrace());
        } 
      
		
		
        
        loadScore();
    }

    /**
     * Debug Active
     * @return
     */
    public static boolean isDebugActive() {
        return debug;
    }

    /**
     *  MovesCount
     * @return
     */
    public int getMovesCount() {
        return m_movesCount;
    }

    /**
     * MapSetName
     * @return
     */
    public String getMapSetName() {
        return m_mapSetName;
    }

    /**
     *  Setting code and print it
     * @param code is keycode Object
     */
    public void handleKey(KeyCode code) {
        switch (code) {
            case UP:
            	GraphicObject.setKeyCode(code);
                move(new Point(-1, 0));
                break;

            case RIGHT:
            	GraphicObject.setKeyCode(code);
                move(new Point(0, 1));
                break;

            case DOWN:
            	GraphicObject.setKeyCode(code);
                move(new Point(1, 0));
                break;

            case LEFT:
            	GraphicObject.setKeyCode(code);
                move(new Point(0, -1));
                break;

            default:
                // TODO: implement something funny.
        }

        if (isDebugActive()) {
            System.out.println(code);
        }
    }

    /**
     * Level point
     * @param delta  is point Object
     */
    public void move(Point delta) {
        if (isGameComplete()) {
            return;
        }
        
        getCurrentLevel().logMove();

        Point keeperPosition = m_currentLevel.getKeeperPosition();
        GameObject keeper = m_currentLevel.getObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = m_currentLevel.getObjectAt(targetObjectPoint);

        if (StartMeUp.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(m_currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget,
                    targetObjectPoint);
        }

        boolean keeperMoved = false;

        switch (keeperTarget) {

            case WALL:
                break;

            case CRATE:

                GameObject crateTarget =
                        m_currentLevel.getTargetObject(targetObjectPoint,
                                delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                m_currentLevel.moveGameObjectBy(keeperTarget,
                        targetObjectPoint, delta);
                m_currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            case FLOOR:
                m_currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                m_logger.severe("The object to be moved was not a recognised " +
                        "GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }

        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            m_movesCount++;
            if (m_currentLevel.isComplete()) {
                if (isDebugActive()) {
                    System.out.println("Level complete!");
                }

                m_currentLevel = getNextLevel();
                m_score+=m_level++;
                scoreDialog();
            }
        }
    }

    /**
     *  scoreDialog
     *  scoreList
     */
    private void scoreDialog() {
    	StringBuilder stringBuilder=new StringBuilder();
    	stringBuilder.append("\nScoreList:\n");
    	for (Score score:m_scoreList) {
			stringBuilder.append(score+"\n");
		}
		JOptionPane.showMessageDialog(null,
                m_pname+" Score:"+m_score+stringBuilder.toString());
	}

    /**
     * saving Score
     */
    public void saveScore() {
    	if (m_score==0) {
			return;
		}
		m_scoreList.add(new Score(m_pname, m_score));
		PrintWriter printWriter;
		Collections.sort(m_scoreList,Collections.reverseOrder());
		try {
			printWriter = new PrintWriter("score.txt");
			for (Score score:m_scoreList) {
				printWriter.println(score);
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

    /**
     *  loadScore
     */
    private void loadScore() {
		try {
			BufferedReader bufferedReader=new BufferedReader
                    (new FileReader("score.txt"));
			String line;
			while ((line=bufferedReader.readLine())!=null) {
				String[] lines=line.split(",");
				Score score=new Score(lines[0], Integer.parseInt(lines[1]));
				m_scoreList.add(score);
			}
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
		
	}

    /**
     *  load Game File
     * @param input is InputStream Object
     * @return
     */
    public List<Level> loadGameFile(InputStream input) {
        List<Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                // Break the loop if EOF is reached
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level
                                (levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("MapSetName")) {
                    m_mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) {
                        Level parsedLevel = new Level
                                (levelName, ++levelIndex, rawLevel);
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
            m_logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            m_logger.severe("Cannot open the requested file: " + e);
        }

        return levels;
    }

    /**
     * undo
     */
    public void undo() {
		getCurrentLevel().undo();
	}

    /**
     * Game Complete
     * @return
     */
    public boolean isGameComplete() {
        return m_gameComplete;
    }

    /**
     * Create Player
     */
    public void createPlayer() {
        //File filePath = new File(getClass().getClassLoader().getResource("music/puzzle_theme.wav").toString());
       try {
    	   Media music = new Media(this.getClass().
                   getResource("puzzle_theme.wav").toString());
           m_player = new MediaPlayer(music);
	} catch (Exception e) {
		// TODO: handle exception
	}
       
        //player.play();
        //player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
    }

    /**
     * play music
     */
    public void playMusic() {
        m_player.play();
    }

    /**
     * stop music
     */
    public void stopMusic() {
        m_player.stop();
    }

    /**
     * check music playing
     * @return
     */
    public boolean isPlayingMusic() {
//        return player.getStatus() == MediaPlayer.Status.PLAYING;
        return false;
    }

    /**
     * get next level
     * @return
     */
    public Level getNextLevel() {
        if (m_currentLevel == null) {
            return m_levels.get(0);
        }

        int currentLevelIndex = m_currentLevel.getIndex();
        if (currentLevelIndex < m_levels.size()) {
            return m_levels.get(currentLevelIndex + 1);
        }

        m_gameComplete = true;
        return null;
    }

    /**
     * get level
     * @return
     */
    public Level getCurrentLevel() {
        return m_currentLevel;
    }

    /**
     * debug
     */
    public void toggleDebug() {
        debug = !debug;
    }

}