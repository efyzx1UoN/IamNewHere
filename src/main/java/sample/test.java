package sample;

import javafx.scene.image.Image;

import java.io.InputStream;

public class test {
    public static void main(String[] args) {
        InputStream temp = test.class.getResourceAsStream("sprites.png");
        System.out.println(temp);
        Image image=new Image(temp);
        System.out.println("hello");
    }
}
