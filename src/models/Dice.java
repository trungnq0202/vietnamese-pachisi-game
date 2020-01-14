/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2019C
  Assessment: Final Project
  Created date: 01/01/2020
  By: Group 10 (3426353,3791159,3742774,3748575,3695662)
  Last modified: 14/01/2020
  By: Group 10 (3426353,3791159,3742774,3748575,3695662)
  Acknowledgement: none.
*/

package models;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Dice extends Button {
    // fields
    private int rollNumber; //roll number for dice
    private Image rollImage; //roll image for dice
    private ImageView imageView; //ImageView for rollImage
    private static String DEFAULT_IMAGE_URL = "resources/images/6.png";
    private static final Image defaultImage = new Image(DEFAULT_IMAGE_URL); //default roll image
    private boolean isUsable;

    //constructor
    public Dice (){
        rollNumber = 0;
        rollImage = defaultImage;
        imageView = new ImageView(rollImage);
        imageView.setFitWidth(80);
        imageView.setFitHeight(70);
        imageView.setPreserveRatio(true);
        setGraphic(imageView);
    }

    //methods
    //method to roll dice then set roll number of dice
    public void roll(){
        rollNumber = new Random().nextInt(6) + 1;
        setRollNumber(rollNumber);
    }

    //method to get roll number of a dice
    public int getRollNumber(){
        return rollNumber;
    }

    //method to set roll number of dice
    public void setRollNumber(int rollNumber){
        this.rollNumber = rollNumber;
    }

    //method to set roll image according to input roll number
    public void setRollImage(int i){
        rollImage = new Image("resources/images/" + i +".png");
        imageView = new ImageView(rollImage);
        imageView.setFitWidth(80);
        imageView.setFitHeight(70);
        imageView.setPreserveRatio(true);
        setGraphic(imageView);
    }

    // check if dice is usable
    public boolean isUsable() {
        return isUsable;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }

}

