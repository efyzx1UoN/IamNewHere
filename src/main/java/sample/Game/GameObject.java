package sample.Game;

/**
 *  @author Zihui xu - Modified
 *  @version 1.0
 *  Setting GameObject here
 */
public enum GameObject {
    WALL('W'),
    FLOOR(' '),
    CRATE('C'),
    DIAMOND('D'),
    KEEPER('S'),
    CRATE_ON_DIAMOND('O'),
    DEBUG_OBJECT('=');

    private final char symbol;

    /**
     * GameObject Symbol
     * @param symbol
     */
    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     * Setting Symbol
     * @param c
     * @return Object Wall
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
            }
        }

        return WALL;
    }

    /**
     * Getting StringSymbol
     * @return
     */
    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    /**
     * Getting CharSymbol
     * @return
     */
    public char getCharSymbol() {
        return symbol;
    }
}