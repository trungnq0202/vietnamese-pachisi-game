package controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import models.Dice;
import models.Horse;

import java.util.ArrayList;
import java.util.List;

public class DicesController {
    private MainController mainController;
    private GameBoardController gameBoardController;
    private Dice dice1, dice2;
    private Button diceArrow1;
    private Button diceArrow2;
    private Animation diceArrow1Animation;
    private Animation diceArrow2Animation;

    private static List<Image> images = new ArrayList<>(); //array of animation for rolling dices
    @FXML private HBox dices; //HBox to store 2 dices

    //method to load images into array images
    static{
        for (int i = 1; i < 7; i++){
            Image rollAnimationImage = new Image("resources/images/" + i +"a.png");
            images.add(rollAnimationImage);
        }
    }

    public DicesController(){
        System.out.println("DicesController construct");
    }

    //populate HBox with 2 dices
    @FXML public void initialize() {
        System.out.println("DicesController init");

        dice1 = new Dice();
        dice2 = new Dice();
        createDiceArrow1();
        createDiceArrow2();
        setEventHandlerForDiceRoll();
        dices.getChildren().addAll(diceArrow1,dice1, dice2,diceArrow2);
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void injectGameBoardController(GameBoardController gameBoardController){this.gameBoardController = gameBoardController;}

    public void setEventHandlerForDice1Pick(GameBoardController gameBoardController, Horse horse){
//        gameBoardController.displayHorseIdOfPosition();
        if (horse.getPossibleStepsListByIndex(0) == 0) return;
        showSideArrow1();   //Show side arrow of dice 1
        String endPosition = gameBoardController.calculateNextPosition(horse.getPossibleStepsListByIndex(0), horse.getTempPosition(), null);
        StackPane endPositionNodeSP = (StackPane) gameBoardController.getGameBoard().lookup("#" + endPosition);


        //When hovering, highlight end position
        dice1.setOnMouseEntered(event -> {
            endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: yellow");

        });

        //When not hovering, unhighlight end position
        dice1.setOnMouseExited(event -> {
            endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: transparent");
        });

        //When click, execute animation, check possible moves for all other horse again
        dice1.setOnMouseClicked(event -> {
            dice1.setUsable(false); //dice 1 can no longer be chosen for a horse move
            unsetEventHandlerForDices();
            gameBoardController.unhighlightHorsesInsideNest();
            gameBoardController.unhighlightHorseOutsideNest();
            gameBoardController.createHorseMovingAnimation(horse.getTempPosition(), horse.getTempPosition(), endPosition, horse);
        });
    }

    public void setEventHandlerForDice2Pick(GameBoardController gameBoardController, Horse horse){
        if (horse.getPossibleStepsListByIndex(1) == 0) return;

        showSideArrow2();   //Show side arrow of dice 2
        String endPosition = gameBoardController.calculateNextPosition(horse.getPossibleStepsListByIndex(1), horse.getTempPosition(), null);
        StackPane endPositionNodeSP = (StackPane) gameBoardController.getGameBoard().lookup("#" + endPosition);

        //When hovering, highlight end position
        dice2.setOnMouseEntered(event -> {
            endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: yellow");
        });

        //When not hovering, unhighlight end position
        dice2.setOnMouseExited(event -> {
            endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: transparent");
        });

        //When click, execute animation, check possible moves for all other horse again
        dice2.setOnMouseClicked(event -> {
            dice2.setUsable(false); //dice 2 can no longer be chosen for a horse move
            unsetEventHandlerForDices();
            gameBoardController.unhighlightHorsesInsideNest();
            gameBoardController.unhighlightHorseOutsideNest();
            gameBoardController.createHorseMovingAnimation(horse.getTempPosition(), horse.getTempPosition(), endPosition, horse);
        });
    }

    public void unsetEventHandlerForDices(){
        dice1.setOnMouseClicked(null);
        dice2.setOnMouseClicked(null);
        dice1.setOnMouseEntered(null);
        dice2.setOnMouseEntered(null);
        dice1.setOnMouseExited(null);
        dice2.setOnMouseExited(null);
        hideSideArrow1();       //hide dice 1 side arrow animation
        hideSideArrow2();       //hide dice 2 side arrow animation
    }

    public void showSideArrow1(){
        diceArrow1.setVisible(true);
        diceArrow1Animation.play();
    }

    public void hideSideArrow1(){
        diceArrow1Animation.stop();
        diceArrow1.setVisible(false);
    }

    public void showSideArrow2(){
        diceArrow2.setVisible(true);
        diceArrow2Animation.play();
    }

    public void hideSideArrow2(){
        diceArrow2Animation.stop();
        diceArrow2.setVisible(false);
    }

    private void createDiceArrow1(){
        diceArrow1 = new Button();
        diceArrow1.getStyleClass().add("leftArrow");
        diceArrow1.prefWidth(41);
        diceArrow1.prefHeight(25);
        diceArrow1.setVisible(false);
        diceArrow1Animation = new Timeline(new KeyFrame(Duration.millis(50), e -> moveLeftSideArrow()));
        diceArrow1Animation.setCycleCount(Timeline.INDEFINITE);
    }

    private void createDiceArrow2(){
        diceArrow2 = new Button();
        diceArrow2.getStyleClass().add("rightArrow");
        diceArrow2.prefWidth(41);
        diceArrow2.prefHeight(25);
        diceArrow2.setVisible(false);
        diceArrow2Animation = new Timeline(new KeyFrame(Duration.millis(50), e -> moveRightSideArrow()));
        diceArrow2Animation.setCycleCount(Timeline.INDEFINITE);
    }

    private void moveLeftSideArrow(){
        if (diceArrow1.getTranslateX() == 0) diceArrow1.setTranslateX(10);
        else diceArrow1.setTranslateX(0);
    }

    private void moveRightSideArrow(){
        if (diceArrow2.getTranslateX() == 0) diceArrow2.setTranslateX(-10);
        else diceArrow2.setTranslateX(0);
    }

    //create rolling effect
    public void rollWithAnimation(Dice dice) {
        if (gameBoardController.isFreeze() || !gameBoardController.isRollingDiceTurn()) return;
        if (dice == dice2) gameBoardController.setFreeze(true);

        //If the dice rolling is temporarily allowed
            ImageView imageView = new ImageView();
            Transition rollAnimation = new Transition() {
                {
                    setCycleDuration(Duration.millis(500));
                } //duration of the animation

                @Override
                protected void interpolate(double v) {  //the method of creating the animation with v increasing
                    int index = (int) (v * (images.size() - 1));
                    imageView.setFitWidth(80);
                    imageView.setPreserveRatio(true);
                    imageView.setImage(images.get(index));
                    dice.setGraphic(imageView);
                }
            };
            rollAnimation.setOnFinished(event -> { //after finishing the rolling animation, actual roll
                dice.roll();
                int i = dice.getRollNumber();
                dice.setRollImage(i);
                gameBoardController.setFreeze(false);
                if (dice == dice2) gameBoardController.processPostDiceRolling();
            });
            rollAnimation.play();
        }

    //add event handler for each dice, clicking one dice will result in 2 dices being rolled
    public void setEventHandlerForDiceRoll(){
        dice1.setOnMouseClicked(event -> {
            rollWithAnimation(dice1);
            rollWithAnimation(dice2);
        });
        dice2.setOnMouseClicked(event -> {
            rollWithAnimation(dice1);
            rollWithAnimation(dice2);
        });
    }

    public Dice getDice1() {
        return dice1;
    }

    public Dice getDice2() {
        return dice2;
    }
}
