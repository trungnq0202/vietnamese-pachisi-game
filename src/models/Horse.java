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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.Serializable;
import java.util.ArrayList;

public class Horse extends HBox implements Serializable {
    // constant fields
    private static final String RED_HORSE_IMG_URL = "file:src/resources/images/red_horse.png";
    private static final String GREEN_HORSE_IMG_URL = "file:src/resources/images/green_horse.png";
    private static final String BLUE_HORSE_IMG_URL = "file:src/resources/images/blue_horse.png";
    private static final String YELLOW_HORSE_IMG_URL = "file:src/resources/images/yellow_horse.png";

    // fields
    private char horseColor;
    private boolean isInNest;
    private boolean isInHome;
    private int rowIndex;
    private int columnIndex;
    private String tempPosition;
    private Button sideArrow;
    private Timeline arrowAnimation;
    private ArrayList<Integer> listOfPossibleSteps;

    // constructor
    public Horse(char horseColor, int horseNo, int rowIndex, int columnIndex){
        this.horseColor = horseColor;
        this.isInNest = true;
        this.isInHome = false;
        this.tempPosition = null;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        listOfPossibleSteps = new ArrayList<>(2){
            {
                add(0); add(0);
            }
        };
        setProperties();
        createSideArrow();                          //Create side arrow for highlighting this horse object when needed
        this.getStyleClass().add("activeHorse");    //Set css class for horse object
        setHorseIdAndImg(horseColor, horseNo);      //Set background image (horse image) and set this horse fxid
    }

    // set properties of horse views
    private void setProperties(){
        this.setMinHeight(USE_PREF_SIZE);
        this.setMinWidth(USE_PREF_SIZE);
        this.setPrefHeight(55);
        this.setPrefWidth(70);
        this.setMaxHeight(USE_PREF_SIZE);
        this.setMaxWidth(USE_PREF_SIZE);
    }

    // set horse id and image
    private void setHorseIdAndImg(char horseColor, int horseNo){
        Background horseImg = null;
        switch (horseColor){
            case 'R':{
                this.setId("RH" + horseNo);
                horseImg = new Background(new BackgroundImage(new Image(RED_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(120, 70, false, false,  false, false)));
                break;
            }
            case 'G':{
                this.setId("GH" + horseNo);
                horseImg = new Background(new BackgroundImage(new Image(GREEN_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(120, 70, false, false,  false, false)));
                break;
            }
            case 'B':{
                this.setId("BH" + horseNo);
                horseImg = new Background(new BackgroundImage(new Image(BLUE_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(120, 70, false, false,  false, false)));
                break;
            }
            case 'Y':{
                this.setId("YH" + horseNo);
                horseImg = new Background(new BackgroundImage(new Image(YELLOW_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(120, 70, false, false,  false, false)));
                break;
            }
        }
        this.setBackground(horseImg);
    }

    // check if horse is in nest
    public boolean isInNest() {
        return isInNest;
    }

    public void setInNest(boolean inNest) {
        isInNest = inNest;
    }

    // create side arrow
    private void createSideArrow(){
        sideArrow = new Button();
        sideArrow.getStyleClass().add("arrow");
        sideArrow.prefWidth(41);
        sideArrow.prefHeight(25);
        sideArrow.setVisible(false);
        this.setAlignment(Pos.CENTER_RIGHT);
        this.getChildren().add(sideArrow);
        arrowAnimation = new Timeline(new KeyFrame(Duration.millis(50), e -> moveSideArrow()));
        arrowAnimation.setCycleCount(Timeline.INDEFINITE);
    }

    // show side arrow
    public void showSideArrow(){
        sideArrow.setVisible(true);
        arrowAnimation.play();
    }

    // hide side arrow
    public void hideSideArrow(){
        sideArrow.setStyle("-fx-background-color: red");
        arrowAnimation.stop();
        sideArrow.setVisible(false);
    }

    // move side arrow
    private void moveSideArrow(){
        if (sideArrow.getTranslateX() == 0) sideArrow.setTranslateX(10);
        else sideArrow.setTranslateX(0);
    }

    // pause arrow animation
    public void pauseArrowAnimation(){
        sideArrow.setStyle("-fx-background-color: orange");
        arrowAnimation.pause();
    }

    // resume arrow animation
    public void resumeArrowAnimation(){
        sideArrow.setStyle("-fx-background-color: red");
        arrowAnimation.play();
    }

    //Getters and setters
    public char getHorseColor() {
        return horseColor;
    }

    public String getTempPosition() {
        return tempPosition;
    }

    public void setTempPosition(String tempPosition) {
        this.tempPosition = tempPosition;
    }

    public boolean isInHome() {
        return isInHome;
    }

    public void setInHome(boolean inHome) {
        isInHome = inHome;
    }

    // reset list of possible steps
    public void resetListOfPossibleSteps(){
        for (int i = 0; i < listOfPossibleSteps.size(); i++){
            listOfPossibleSteps.set(i,0);
        }
    }

    // set possible steps list by index
    public void setPossibleStepsListByIndex(int index, int steps) {
        listOfPossibleSteps.set(index, steps);
    }

    // get possible steps list by index
    public int getPossibleStepsListByIndex(int index) {
        return listOfPossibleSteps.get(index);
    }

    // check if horse is in home door position
    public boolean isInHomeDoorPosition(){
        if (tempPosition == null) return false;
        return tempPosition.equals(horseColor + "0");
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }
}
