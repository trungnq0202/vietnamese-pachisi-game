package controllers;

import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import models.Dice;

import java.util.ArrayList;
import java.util.List;

public class DicesController {
    private MainController mainController;
    private GameBoardController gameBoardController;
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

        Dice dice1 = new Dice();
        Dice dice2 = new Dice();
        eventHandlerForDice(dice1,dice2);
        dices.getChildren().addAll(dice1, dice2);
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void injectGameBoardController(GameBoardController gameBoardController){this.gameBoardController = gameBoardController;}

    //create rolling effect
    public void rollWithAnimation(Dice dice) {
        //If the dice rolling is temporarily allowed
        if (gameBoardController.getIsRollingDiceTurn()) {
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
            rollAnimation.play();
            rollAnimation.setOnFinished(event -> { //after finishing the rolling animation, actual roll
                dice.roll();
                int i = dice.getRollNumber();
                dice.setRollImage(i);
            });
        }
    }

    //add event handler for each dice, clicking one dice will result in 2 dices being rolled
    public void eventHandlerForDice(Dice dice1, Dice dice2){
        dice1.setOnMouseClicked(event -> {
            rollDices(dice1, dice2);
        });
        dice2.setOnMouseClicked(event -> {
            rollDices(dice1, dice2);
        });
    }

    private void rollDices(Dice dice1, Dice dice2){
        rollWithAnimation(dice1);
        rollWithAnimation(dice2);
        gameBoardController.setIsRollingDiceTurn(false); //Do not allow the players to roll dice until they've finished their horses moves
        dices.setStyle("-fx-background-color: transparent");
    }
}
