package sample;

import sample.Game.GameObject;
import sample.StartMeUp;

import java.awt.*;
import java.util.Iterator;

/**
 *  @author Zihui xu - Modified
 *  @version 1.0
 *  Setting GameGrid
 *  Setting COLUMNS,ROWS
  */
public class GameGrid implements Iterable {

    final int COLUMNS;
    final int ROWS;
    private GameObject[][] m_gameObjects;

    /**
     * Create Variables of GameGrid
     * @param columns is integer object
     * @param rows is integer object
     */
    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;

        m_gameObjects = new GameObject[COLUMNS][ROWS];
    }

    /**
     * get point location
     * @param sourceLocation is point Object
     * @param delta is point Object
     * @return
     */
    static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     *
     * @return
     */
    public Dimension getDimension() {
        return new Dimension(COLUMNS, ROWS);
    }

    /**
     *  getTargetFromSource
     * @param source is point Object
     * @param delta is point Object
     * @return
     */
    GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * return game Objects
     * @param col is integer object
     * @param row  is point object
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    public GameObject getGameObjectAt(int col, int row)
            throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            if (StartMeUp.isDebugActive()) {
                System.out.printf("Trying to get null GameObject from COL: " +
                        "%d  ROW: %d", col, row);
            }
            throw new ArrayIndexOutOfBoundsException("The point " +
                    "[" + col + ":" + row + "] is outside the map.");
        }

        return m_gameObjects[col][row];
    }

    /**
     * return GameObject
     * @param p is point object
     * @return
     */
    public GameObject getGameObjectAt(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    /**
     * return GameObject
     * @param position is point object
     * @return
     */
    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }

    /**
     * setting GameObject
     * @param gameObject
     * @param x is point object
     * @param y is point object
     * @return
     */
    public boolean putGameObjectAt(GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        m_gameObjects[x][y] = gameObject;
        return m_gameObjects[x][y] == gameObject;
    }

    /**
     * return GameObject
     * @param gameObject
     * @param p is point object
     * @return
     */
    public boolean putGameObjectAt(GameObject gameObject, Point p) {
        return p != null && putGameObjectAt(gameObject,
                (int) p.getX(), (int) p.getY());
    }

    /**
     *  check out of Bounds
     * @param x is integer object
     * @param y is integer object
     * @return
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     *
     * @param p
     * @return
     */
    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }

    /**
     * ToString Method
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(m_gameObjects.length);

        for (GameObject[] gameObject : m_gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = GameObject.DEBUG_OBJECT;
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Iterator of GameObject
     * @return
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }

    /**
     * GridIterator
     */
    public class GridIterator implements Iterator<GameObject> {
        int row = 0;
        int column = 0;

        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

        @Override
        public GameObject next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}