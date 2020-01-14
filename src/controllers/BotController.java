package controllers;

import javafx.animation.*;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import models.Dice;
import models.Horse;

public class BotController {
    private Dice dice2;
    private Dice dice1;
    Horse horsePick;
    int dicePick;
    int tempMaxScore;
    int tempMinDistanceFromHomeDoorPos;
    private GameBoardController gameBoardController;

    public BotController(Dice dice1, Dice dice2, GameBoardController gameBoardController){
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.gameBoardController = gameBoardController;
        this.horsePick = null;
        dicePick = -1;
    }

    public void autoRollDice(){
        if (gameBoardController.isGameRunning())
        hoverThenClick(dice1);
    }

    public void autoPickRandomHorseGoingOutsideNest(){
        if (gameBoardController.isGameRunning()){
            Horse pickHorse =  gameBoardController.getRandomHorseInsideNest();
            hoverThenClick(pickHorse);
        }
    }

    public void autoPickMostReasonableHorse(){
        tempMinDistanceFromHomeDoorPos = 49;
        tempMaxScore = 0;
        horsePick = null;
        dicePick = -1;

        //Pick horse with the highest score by knock down or going up home
        for (Horse horse : GameBoardController.getHorsesWithValidMovesList()) analyzeProspectiveScore(horse);

        //Go with the horse with move that add the most score
        if (tempMaxScore != 0){
            hoverThenClick(horsePick);
            if (dicePick == 0) hoverThenClick(dice1);
            else hoverThenClick(dice2);
            return;
        }

        //If no horse is going up home, prioritize horse with smallest distance from its home door position
        for (Horse horse : GameBoardController.getHorsesWithValidMovesList()) analyzeProspectiveDistanceFromHomeDoorPos(horse);

        hoverThenClick(horsePick);
        if (dicePick == 0) hoverThenClick(dice1);
        else hoverThenClick(dice2);
    }

    private void hoverThenClick(Node node){

        Event.fireEvent(node, new MouseEvent(MouseEvent.MOUSE_ENTERED, 0,0,0,0, null, 0, false, false, false, false,
                false, false, false, false, false, false, null));

        SequentialTransition sequentialTransition =  new SequentialTransition(
                new PauseTransition(Duration.millis(500))
        );

        sequentialTransition.setOnFinished(actionEvent -> {
            Event.fireEvent(node, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,0,0,0, MouseButton.PRIMARY, 1, false, false, false, false,
                    true, false, false, false, false, false, null));
            Event.fireEvent(node, new MouseEvent(MouseEvent.MOUSE_EXITED, 0,0,0,0, MouseButton.PRIMARY, 0, false, false, false, false,
                    false, false, false, false, false, false, null));
        });
        sequentialTransition.play();
    }

    private int calculateProspectiveScore(Horse horse, int dicePick){
        //If this horse is already in home
        if (horse.isInHome()){
            //If this horse just entered home
            if (horse.isJustEnteredHome()) {
                int nextHomePosIntegerForm = gameBoardController.convertHomePositionToIntegerForm(gameBoardController.calculateNextHomePosition(horse.getPossibleStepsListByIndex(dicePick), horse));
                int tempHomePosIntegerForm = gameBoardController.convertHomePositionToIntegerForm(horse.getTempPosition());
                return nextHomePosIntegerForm - tempHomePosIntegerForm;
            } else {
                //Going upper home only get 1 point
                return 1;
            }
        } else if (horse.isInHomeDoorPosition()) return horse.getPossibleStepsListByIndex(dicePick);
            else {
                int nextPositionIntegerForm = gameBoardController.convertPositionToIntegerForm(gameBoardController.calculateNextPosition(horse.getPossibleStepsListByIndex(dicePick), horse.getTempPosition(), null));
                if (GameBoardController.getHorseIdOfPositionByIndex(nextPositionIntegerForm) != null) return 2;
        }
            return 0;
    }

    private int calculateProspectiveDistanceFromHomeDoorPos(Horse horse, int dicePick){
        String startPos = horse.getTempPosition();
        String nextPos = gameBoardController.calculateNextPosition(horse.getPossibleStepsListByIndex(dicePick), startPos, null);
        int stepsCountToHomeDoorPos = 1;
        String nextPosAfterProspectiveOne = gameBoardController.calculateNextPosition(1, nextPos, null);
        String homeDoorPos = "" + horse.getHorseColor() + 0;
        while (!nextPosAfterProspectiveOne.equals(homeDoorPos)){
            stepsCountToHomeDoorPos++;
            nextPosAfterProspectiveOne = gameBoardController.calculateNextPosition(1, nextPosAfterProspectiveOne, null);
        }
        return stepsCountToHomeDoorPos;
    }

    private void analyzeProspectiveDistanceFromHomeDoorPos(Horse horse){
        if (horse.getPossibleStepsListByIndex(0) != 0) {
            int distanceFromHomeDoorPos1 = calculateProspectiveDistanceFromHomeDoorPos(horse, 0);
            if (distanceFromHomeDoorPos1 < tempMinDistanceFromHomeDoorPos){
                tempMinDistanceFromHomeDoorPos = distanceFromHomeDoorPos1;
                horsePick = horse;
                dicePick = 0;
            }
        }

        if (horse.getPossibleStepsListByIndex(1) != 0) {
            int distanceFromHomeDoorPos2 = calculateProspectiveDistanceFromHomeDoorPos(horse, 1);
            if (distanceFromHomeDoorPos2 < tempMinDistanceFromHomeDoorPos){
                tempMinDistanceFromHomeDoorPos = distanceFromHomeDoorPos2;
                horsePick = horse;
                dicePick = 1;
            }
        }
    }

    private void analyzeProspectiveScore(Horse horse){
        if (horse.getPossibleStepsListByIndex(0) != 0) {
            int prospectiveScore = calculateProspectiveScore(horse, 0);
            if (prospectiveScore > tempMaxScore){
                horsePick = horse;
                tempMaxScore = prospectiveScore;
                dicePick = 0;
            }
        }

        if (horse.getPossibleStepsListByIndex(1) != 0) {
            int prospectiveScore = calculateProspectiveScore(horse, 1);
            if (prospectiveScore > tempMaxScore){
                horsePick = horse;
                tempMaxScore = prospectiveScore;
                dicePick = 1;
            }
        }
    }


}
