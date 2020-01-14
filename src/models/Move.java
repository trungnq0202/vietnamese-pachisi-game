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

import java.io.Serializable;

public class Move implements Serializable {
    // fields
     public enum type{
         DICES_VALUE,
         NEXT_TURN,
         HORSE_GOING_OUTSIDE_NEST,
         HORSE_MOVING,
         HORSE_MOVING_INSIDE_HOME,
    }
    private type moveType;
    private int dice1;
    private int dice2;
    private String nextPlayerName;
    private String startPosition;
    private String horseId;
    private String endPosition;
    private int playerIdTurnAtThisTime;
    private int steps;

    //Sending dice value to other players
    public Move(type moveType, int dice1, int dice2){
        this.moveType = moveType;
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.nextPlayerName = null;
    }

    //update next player turn for other online players
    public Move(type moveType, String nextPlayerName){
        this.moveType = moveType;
        this.nextPlayerName = nextPlayerName;
    }

    //Sending horse going outside nest move to other online players
    public Move(type moveType, String startPosition, String horseId){
        this.moveType = moveType;
        this.startPosition = startPosition;
        this.horseId = horseId;
    }

    //Sending horse moving normally to other online players
    public Move(type moveType, String endPosition, String horseId, int steps, int playerIdTurnAtThisTime){
        this.moveType = moveType;
        this.endPosition = endPosition;
        this.steps = steps;
        this.horseId = horseId;
        this.playerIdTurnAtThisTime = playerIdTurnAtThisTime;
    }

    //Sending horse moving inside home to other online players
    public Move(type moveType, String startPosition, String endPosition, String horseId, int steps, int playerIdTurnAtThisTime){
        this.moveType = moveType;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.steps = steps;
        this.horseId = horseId;
        this.playerIdTurnAtThisTime = playerIdTurnAtThisTime;
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public String getNextPlayerName() {
        return nextPlayerName;
    }

    public String getHorseId() {
        return horseId;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public int getSteps() {
        return steps;
    }

    public int getPlayerIdTurnAtThisTime() {
        return playerIdTurnAtThisTime;
    }

    public type getMoveType() {
        return moveType;
    }
}
