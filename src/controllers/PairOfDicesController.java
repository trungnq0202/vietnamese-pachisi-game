package controllers;

import javafx.animation.RotateTransition;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import models.Dice;

public class PairOfDicesController {
    public HBox pairOfDices;

    public void initialize() {
        Dice dice1 = new Dice();
        Dice dice2 = new Dice();
        eventHandlerForDice(dice1,dice2);
        pairOfDices.getChildren().addAll(dice1, dice2);
    }

    public static void rollAnimation(Dice dice) {
        RotateTransition rt = new RotateTransition(Duration.seconds(1), dice);
        rt.setAxis(Rotate.Z_AXIS);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.play();
    }

    public static void rollWithAnimation(Dice dice) {
        dice.roll();
        rollAnimation(dice);
        int i = dice.getRollNumber();
        dice.setRollImage(dice, i);
    }

    public static void eventHandlerForDice(Dice dice1, Dice dice2){
        dice1.setOnMouseClicked(event -> {
           rollWithAnimation(dice1);
           rollWithAnimation(dice2);
            System.out.println(dice1.getRollNumber());
            System.out.println(dice2.getRollNumber());
        });
        dice2.setOnMouseClicked(event -> {
            rollWithAnimation(dice1);
            rollWithAnimation(dice2);
            System.out.println(dice1.getRollNumber());
            System.out.println(dice2.getRollNumber());
        });
    }

}
