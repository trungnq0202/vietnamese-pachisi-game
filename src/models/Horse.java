package models;

import controllers.GameBoardController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.ArrayList;

public class Horse extends HBox {
    private static final String RED_HORSE_IMG_URL = "file:src/resources/images/red_horse.png";
    private static final String GREEN_HORSE_IMG_URL = "file:src/resources/images/green_horse.png";
    private static final String BLUE_HORSE_IMG_URL = "file:src/resources/images/blue_horse.png";
    private static final String YELLOW_HORSE_IMG_URL = "file:src/resources/images/yellow_horse.png";

//    private String horseColor;
    private char horseColor;
    private int nestRow;
    private int nestColumn;
    private boolean isInNest;
    private boolean isMovable;
    private boolean isInHome;
//    private boolean isReachedHomeDoor;
    private String tempPosition;
    private Button sideArrow;
    private Timeline arrowAnimation;
    private VBox moveOptionsContainer;
    private Button moveOptionOfDice1;
    private Button moveOptionOfDice2;
    private Button moveOptionOfDice1AndDice2;
    private ArrayList<Integer> listOfPossibleSteps;

    public Horse(char horseColor, int nestRow, int nestColumn, int horseNo){
        this.nestRow = nestRow;
        this.nestColumn = nestColumn;
        this.horseColor = horseColor;
        this.isInNest = true;
        this.isInHome = false;
        this.isMovable = false;
        this.tempPosition = null;
        listOfPossibleSteps = new ArrayList<>(3);
        setProperties();
        createMoveOptions();                        //Create list of move options for each horse
        createSideArrow();                          //Create side arrow for highlighting this horse object when needed

        this.getStyleClass().add("activeHorse");    //Set css class for horse object
        setHorseIdAndImg(horseColor, horseNo);      //Set background image (horse image) and set this horse fxid

    }

    public char getHorseColor() {
        return horseColor;
    }

    public int getNestColumn() {
        return nestColumn;
    }

    public int getNestRow() {
        return nestRow;
    }

    public void setTempPosition(String tempPosition) {
        this.tempPosition = tempPosition;
    }

    private void setHorseIdAndImg(char horseColor, int horseNo){
        Background horseImg = null;
        switch (horseColor){
            case 'R':{
                this.setId("RH" + horseNo);
                horseImg = new Background(new BackgroundImage(new Image(RED_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(150, 150, false, false,  false, false)));
                break;
            }
            case 'G':{
                this.setId("GH" + horseNo);
                horseImg = new Background(new BackgroundImage(new Image(GREEN_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(150, 150, false, false,  false, false)));
                break;
            }
            case 'B':{
                this.setId("BH" + horseNo);
                horseImg = new Background(new BackgroundImage(new Image(BLUE_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(150, 150, false, false,  false, false)));
                break;
            }
            case 'Y':{
                this.setId("YH" + horseNo);
                horseImg = new Background(new BackgroundImage(new Image(YELLOW_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(150, 150, false, false,  false, false)));
                break;
            }
        }
        this.setBackground(horseImg);
    }

    //Check if this
    private boolean checkFinalPosition(){
        return horseColor == tempPosition.charAt(0) && tempPosition.charAt(1) == 0;
    }

    //Calculate next position
    public String calculateNextPosition(int steps, String tmpNextPosition){
        //If the position of this horse is in the nest
        if (tempPosition == null) return horseColor + "1";

        if (tmpNextPosition == null) tmpNextPosition = tempPosition;
        int integerPartOfId = Integer.parseInt(tmpNextPosition.substring(1));  //Get the integer part of the position's fxid

        //If (integer part) + (steps) > 11 -> move out of the temporary color area
        if ( integerPartOfId != 11 && (integerPartOfId + steps) > 11)
            return calculateNextPosition(steps - (11 - integerPartOfId) ,  tmpNextPosition.substring(0,1) + 11);

        //If the tmpNextPosition is at the final position of a specific color area
        if (integerPartOfId == 11) {
            switch (tmpNextPosition.charAt(0)) {
                case 'R': return calculateNextPosition(steps - 1, "B0");
                case 'G': return calculateNextPosition(steps - 1, "R0");
                case 'B': return calculateNextPosition(steps - 1, "Y0");
                case 'Y': return calculateNextPosition(steps - 1, "G0");
            }
        }

        return tmpNextPosition.charAt(0) + Integer.toString(Integer.parseInt(tmpNextPosition.substring(1)) + steps);
    }

    public String calculateNextHomePosition(int steps){
        //If the horse is already in home
        if (isInHome){
            if (steps != 1 || tempPosition.charAt(2) == '6') return null;
            else return tempPosition.substring(0,2) + (Integer.parseInt(tempPosition.substring(2)) + 1);

            //If the horse is at the home door position
        } else {
            if (steps <= 6)
               return "H" + horseColor + steps;
        }
        return null;
    }

    public boolean isInNest() {
        return isInNest;
    }

    public void setInNest(boolean inNest) {
        isInNest = inNest;
    }

    private void setProperties(){
        this.setMinHeight(USE_PREF_SIZE);
        this.setMinWidth(USE_PREF_SIZE);
        this.setPrefHeight(150);
        this.setPrefWidth(150);
        this.setMaxHeight(USE_PREF_SIZE);
        this.setMaxWidth(USE_PREF_SIZE);
    }

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

    private void createMoveOptions(){
        moveOptionsContainer = new VBox();
        moveOptionsContainer.setPrefWidth(120);
        moveOptionsContainer.setPrefHeight(50);
        moveOptionsContainer.setStyle("-fx-background-color: transparent");
//        moveOptionsContainer.setTranslateX(-90);
        moveOptionsContainer.setSpacing(5);
        moveOptionsContainer.setVisible(false);
        createMoveOptionButton();
        moveOptionsContainer.getChildren().addAll(moveOptionOfDice1,moveOptionOfDice2,moveOptionOfDice1AndDice2);
        this.getChildren().add(moveOptionsContainer);
    }

    private void createMoveOptionButton(){
        moveOptionOfDice1 = new Button();
        moveOptionOfDice1.setPrefWidth(120);
        moveOptionOfDice1.setPrefHeight(20);
        moveOptionOfDice1.setText("Dice 1 move");
        moveOptionOfDice1.setStyle("-fx-font-size: 8");
//        moveOptionOfDice1.setVisible(false);

        moveOptionOfDice2 = new Button();
        moveOptionOfDice2.setPrefWidth(120);
        moveOptionOfDice2.setPrefHeight(20);
        moveOptionOfDice2.setText("Dice 2 move");
        moveOptionOfDice2.setStyle("-fx-font-size: 8");
//        moveOptionOfDice2.setVisible(false);

        moveOptionOfDice1AndDice2 = new Button();
        moveOptionOfDice1AndDice2.setPrefWidth(120);
        moveOptionOfDice1AndDice2.setPrefHeight(20);
        moveOptionOfDice1AndDice2.setText("Dice 1 and 2 move");
        moveOptionOfDice1AndDice2.setStyle("-fx-font-size: 8");
//        moveOptionOfDice1AndDice2.setVisible(false);
    }

    public void showSideArrow(){
        sideArrow.setVisible(true);
        arrowAnimation.play();
    }

    public void hideSideArrow(){
        arrowAnimation.stop();
        sideArrow.setVisible(false);

    }

    private void moveSideArrow(){
        if (sideArrow.getTranslateX() == 0) sideArrow.setTranslateX(-10);
        else sideArrow.setTranslateX(0);
    }

    public int convertTempPositionToIntegerForm(int tempPlayerId){
        return Integer.parseInt(tempPosition.substring(1)) + 11 * tempPlayerId + tempPlayerId;
    }

    public int convertHomePositionToIntegerForm(int tempPlayerId, String homePosition){
        return Integer.parseInt(homePosition.substring(1) + 5 * tempPlayerId + tempPlayerId);
    }

    public int getListOfPossibleSteps(int index) {
        return listOfPossibleSteps.get(index);
    }

    public void setListOfPossibleSteps(int index, int steps) {
         listOfPossibleSteps.add(steps);
    }

    public void resetListOfPossibleSteps(){
        for (int i = 0; i < listOfPossibleSteps.size(); i++){
            listOfPossibleSteps.set(i,0);
        }
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public String getTempPosition() {
        return tempPosition;
    }

    public boolean isInHome() {
        return isInHome;
    }

    public void setInHome(boolean inHome) {
        isInHome = inHome;
    }

    public boolean isInHomeDoorPosition(){
        return tempPosition.equals("H" + horseColor + 0);
    }

    public void showMoveOptionDice1(String text, boolean isValid){
        moveOptionOfDice1.setText(text);
        moveOptionOfDice1.setVisible(isValid);
    }

    public void showMoveOptionDice2(String text, boolean isValid){
        moveOptionOfDice2.setText(text);
        moveOptionOfDice2.setVisible(isValid);
    }

    public void showMoveOptionDice1andDice2(String text, boolean isValid){
        moveOptionOfDice1AndDice2.setText(text);
        moveOptionOfDice1AndDice2.setVisible(isValid);
    }

    public void activateShowMoveOptionsOnHover(){
        this.setOnMouseEntered(mouseEvent -> {
            moveOptionsContainer.setVisible(true);
        });

        this.setOnMouseExited(mouseEvent -> {
            moveOptionsContainer.setVisible(false);
        });
    }

    public void deactivateShowMoveOptionsOnMouseHover(){
        this.setOnMouseEntered(null);
        this.setOnMouseExited(null);
    }



    public void setMoveOptionOfDice1EventHandler(Node endPosition, GameBoardController gameBoardController){
        moveOptionOfDice1.setOnMouseEntered(mouseEvent -> {
            endPosition.setStyle("-fx-fill: yellow");

        });

        moveOptionOfDice1.setOnMouseExited(mouseEvent -> {
            endPosition.setStyle("-fx-fill: transparent");
        });

        moveOptionOfDice1.setOnMouseClicked(mouseEvent -> {
            gameBoardController.onOutsideNestHorseClickedEventHandler(this, 0);
        });
    }

    public void setMoveOptionOfDice2EventHandler(Node endPosition, GameBoardController gameBoardController){
        moveOptionOfDice2.setOnMouseEntered(mouseEvent -> {
            endPosition.setStyle("-fx-fill: yellow");
        });

        moveOptionOfDice2.setOnMouseExited(mouseEvent -> {
            endPosition.setStyle("-fx-fill: transparent");
        });

        moveOptionOfDice2.setOnMouseClicked(mouseEvent -> {
            gameBoardController.onOutsideNestHorseClickedEventHandler(this, 1);
        });
    }

    public void setMoveOptionOfDice1AndDice2EventHandler(Node endPosition, GameBoardController gameBoardController){
        moveOptionOfDice1AndDice2.setOnMouseEntered(mouseEvent -> {
            endPosition.setStyle("-fx-fill: yellow");

        });

        moveOptionOfDice1AndDice2.setOnMouseExited(mouseEvent -> {
            endPosition.setStyle("-fx-fill: transparent");
        });

        moveOptionOfDice1AndDice2.setOnMouseClicked(mouseEvent -> {
            gameBoardController.onOutsideNestHorseClickedEventHandler(this, 2);
        });
    }

    public void activateEventHandlerForGoingOutOfNest(GameBoardController gameBoardController){
        this.setOnMouseClicked(mouseEvent -> {
            gameBoardController.onInsideNestHorseClickedEventHandler(this);
        });
    }

    public void deactivateEventHandlerForGoingOutOfNest(){
        this.setOnMouseClicked(null);
    }

//    public void showMoveOptions(){
//        moveOptionsContainer.setVisible(true);
//    }
//
//    public void hideMoveOptions(){
//        moveOptionsContainer.setVisible(false);
//    }
}
