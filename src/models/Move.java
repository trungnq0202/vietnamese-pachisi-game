package models;

import java.io.Serializable;

public class Move implements Serializable {
    private boolean sendDicesValue;
    private boolean sendHorsePick;
    private boolean sendNextTurn;
    private int dice1;
    private int dice2;
    private String nextPlayerName;

    public Move(boolean sendDicesValue, int dice1, int dice2){
        this.sendDicesValue = sendDicesValue;
        this.sendHorsePick = false;
        this.sendNextTurn = false;
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.nextPlayerName = null;

    }

    public Move(boolean sendNextTurn, String nextPlayerName){
        this.sendDicesValue = false;
        this.sendHorsePick = false;
        this.sendNextTurn = sendNextTurn;
        this.nextPlayerName = nextPlayerName;
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public boolean isSendDicesValue() {
        return sendDicesValue;
    }

    public boolean isSendHorsePick() {
        return sendHorsePick;
    }

    public boolean isSendNextTurn() {
        return sendNextTurn;
    }

    public String getNextPlayerName() {
        return nextPlayerName;
    }
}
