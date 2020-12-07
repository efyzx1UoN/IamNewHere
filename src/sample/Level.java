package sample;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static sample.GameGrid.translatePoint;


public final class Level implements Iterable<GameObject> {

    private final String name;
    private GameGrid objectsGrid;
    private final GameGrid diamondsGrid;
    private final int index;
    private int numberOfDiamonds = 0;
    private Point keeperPosition = new Point(0, 0);

    public Level(String levelName, int levelIndex, List<String> raw_level) {
        if (StartMeUp.isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }



        name = levelName;
        index = levelIndex;

        int rows = raw_level.size();
        int columns = raw_level.get(0).trim().length();

        objectsGrid = new GameGrid(rows, columns);
        diamondsGrid = new GameGrid(rows, columns);

        for (int row = 0; row < raw_level.size(); row++) {

            // Loop over the string one char at a time because it should be the fastest way:
            // http://stackoverflow.com/questions/8894258/fastest-way-to-iterate-over-all-the-chars-in-a-string
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                // The game object is null when the we're adding a floor or a diamond
                GameObject curTile = GameObject.fromChar(raw_level.get(row).charAt(col));

                if (curTile == GameObject.DIAMOND) {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.FLOOR;
                } else if (curTile == GameObject.KEEPER) {
                    keeperPosition = new Point(row, col);
                }

                objectsGrid.putGameObjectAt(curTile, row, col);
                curTile = null;
            }
        }
    }

    boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if (objectsGrid.getGameObjectAt(col, row) == GameObject.CRATE && diamondsGrid.getGameObjectAt(col, row) == GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }

        return cratedDiamondsCount >= numberOfDiamonds;
    }

    public String getName() {
        return name;
    }

    int getIndex() {
        return index;
    }

    Point getKeeperPosition() {
        return keeperPosition;
    }

    GameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    GameObject getObjectAt(Point p) {
        return objectsGrid.getGameObjectAt(p);
    }

    void moveGameObjectBy(GameObject object, Point source, Point delta) {
        moveGameObjectTo(object, source, translatePoint(source, delta));
    }

    public void moveGameObjectTo(GameObject object, Point source, Point destination) {
        objectsGrid.putGameObjectAt(getObjectAt(destination), source);
        objectsGrid.putGameObjectAt(object, destination);

    }

    class Step{
        GameGrid objectGrid;
        Point keeperPoint;
        public Step(GameGrid gameGrid,Point point) {
            // TODO Auto-generated constructor stub
            keeperPoint=new Point(point);
            copyGrid(gameGrid);
        }

        private void copyGrid(GameGrid gameGrid) {
            int columns=gameGrid.COLUMNS;
            int rows=gameGrid.ROWS;
            objectGrid=new GameGrid(columns, rows);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    objectGrid.putGameObjectAt(gameGrid.getGameObjectAt(i, j), i, j);
                }
            }
        }
    }

    private ArrayList<Step> steps=new ArrayList<Level.Step>();

    public void logMove() {
        steps.add(0,new Step(objectsGrid, keeperPosition));
    }

    public void undo() {
        if (steps.isEmpty()) {
            return;
        }
        Step step=steps.remove(0);
        objectsGrid=step.objectGrid;
        keeperPosition=step.keeperPoint;
    }

    @Override
    public String toString() {
        return objectsGrid.toString();
    }

    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    public class LevelIterator implements Iterator<GameObject> {

        int column = 0;
        int row = 0;

        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.ROWS - 1 && column == objectsGrid.COLUMNS);
        }

        @Override
        public GameObject next() {
            if (column >= objectsGrid.COLUMNS) {
                column = 0;
                row++;
            }

            GameObject object = objectsGrid.getGameObjectAt(column, row);
            GameObject diamond = diamondsGrid.getGameObjectAt(column, row);
            GameObject retObj = object;

            column++;

            if (diamond == GameObject.DIAMOND) {
                if (object == GameObject.CRATE) {
                    retObj = GameObject.CRATE_ON_DIAMOND;
                } else if (object == GameObject.FLOOR) {
                    retObj = diamond;
                } else {
                    retObj = object;
                }
            }

            return retObj;
        }

        public Point getCurrentPosition() {
            return new Point(column, row);
        }
    }
}