package models;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Dice extends Button {
    private int rollNumber= 6;
    private Image rollImage;
    private ImageView imageView;
    private static final Image defaultImage = new Image("resources/dice/6.png");

    //constructor
    public Dice (){
        rollImage = defaultImage;
        imageView = new ImageView(rollImage);
        setGraphic(imageView);
    }
    //methods
    public void roll(){
        rollNumber = new Random().nextInt(6) + 1;
        setRollNumber(rollNumber);
    }

    public int getRollNumber(){
        return rollNumber;
    }

    public void setRollNumber(int rollNumber){
        this.rollNumber = rollNumber;
    }

    public void setRollImage(Dice dice,int i){
        rollImage = new Image("resources/dice/" + i +".png");
        imageView = new ImageView(rollImage);
        dice.setGraphic(imageView);
    }

}

