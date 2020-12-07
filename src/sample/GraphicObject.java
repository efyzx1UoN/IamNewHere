package sample;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class GraphicObject extends Rectangle {

    private static Color wallColor=Color.BLACK;
    private static Image image=new Image("sample/sprites.png");



    public static void setWallColor(Color wallColor) {
        GraphicObject.wallColor = wallColor;
    }

    GraphicObject(GameObject obj) {
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
        if (obj==GameObject.CRATE) {

            ImagePattern imagePattern=new ImagePattern(image, -30*3-1, 0, 200, 200,false);

            this.setFill(imagePattern);;
        }else if (obj != GameObject.WALL) {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }

        if (StartMeUp.isDebugActive()) {
            this.setStroke(Color.RED);
            this.setStrokeWidth(0.25);
        }
    }

}
