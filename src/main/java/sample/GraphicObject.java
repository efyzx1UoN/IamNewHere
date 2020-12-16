package sample;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import sample.Game.GameObject;

/**
 * @author Zihui xu - Modified
 * @version 1.0
 * render elements of the game
 */
public class GraphicObject extends Rectangle {

    private static Color wallColor = Color.BLACK;
    private static Image image = new Image(GraphicObject.class.
            getResourceAsStream("sprites.png"));


    private static KeyCode keyCode = KeyCode.DOWN;


    /**
     * the method is setting background color
     *
     * @param wallColor is Color Object
     */
    public static void setWallColor(Color wallColor) {
        GraphicObject.wallColor = wallColor;
    }

    /** the method is setting elements
     * @param obj is GameObject
     */
    public GraphicObject(GameObject obj) {
        Paint color;
        switch (obj) {
            case WALL:
                color = wallColor;
                break;

            case CRATE:
                color = Color.ORANGE;
                break;

            case DIAMOND:
                color = Color.DEEPSKYBLUE;

                // TODO: fix memory leak.
                if (StartMeUp.isDebugActive()) {
                    FadeTransition ft = new FadeTransition(Duration.millis(1000), this);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.2);
                    ft.setCycleCount(Timeline.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                }

                break;

            case KEEPER:
                color = Color.RED;
                break;

            case FLOOR:
                color = Color.WHITE;
                break;

            case CRATE_ON_DIAMOND:
                color = Color.DARKCYAN;
                break;

            default:
                String message = "Error in Level constructor. Object not recognized.";
                StartMeUp.logger.severe(message);
                throw new AssertionError(message);
        }

        this.setFill(color);
        this.setHeight(30);
        this.setWidth(30);
        if (obj == GameObject.CRATE) {

            ImagePattern imagePattern = new ImagePattern(image,
                    -30 * 3 - 1, 0, 200, 200, false);

            this.setFill(imagePattern);
        } else if (obj == GameObject.KEEPER) {

            this.setFill(keeperImagePattern());
        } else if (obj != GameObject.WALL) {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }

//        if (StartMeUp.isDebugActive()) {
//            this.setStroke(Color.RED);
//            this.setStrokeWidth(0.25);
//        }
    }

    /**
     * the method is setting pattern elements
     * @return  ImagePattern
     */
    private ImagePattern keeperImagePattern() {
        ImagePattern imagePattern = new ImagePattern(image,
                -30 * 12 - 1, 0, 200, 200, false);
        switch (keyCode) {
            case UP:
                imagePattern = new ImagePattern(image,
                        -30 * 12 - 5, 0, 400, 200, false);
                break;
            case DOWN:
                imagePattern = new ImagePattern(image,
                        -30 * 11 + 20, -30 * 4, 360, 200, false);
                break;
            case LEFT:
                imagePattern = new ImagePattern(image,
                        -30 * 10 + 20, -30 * 3, 360, 200, false);
                break;
            case RIGHT:
                imagePattern = new ImagePattern(image,
                        -30 * 10 + 20, -30 * 4, 360, 200, false);
                break;

            default:
                break;
        }

        return imagePattern;

    }

    public static void setKeyCode(KeyCode keyCode) {
        GraphicObject.keyCode = keyCode;
    }

}
