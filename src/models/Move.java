package models;

import java.io.Serializable;

public class Move implements Serializable {
     public enum type{
        DICESVALUE,
        NEXTTURN,
        HORSEGOINGOUTSIDENEST,
        HORSEMOVING,
        HORSEMOVINGINSIDEHOME
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
    private boolean gameOver = false;

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

    public boolean isGameOver() {
        return (this.gameOver);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
