package models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

//implement interface Serializable so that object can be sent across streams
//implement interface PropertyChange so that changes made into models may be tracked
public class Player implements Serializable, PropertyChangeListener {
    private String playerColor;
    private int score;
    private String name;
    private boolean isServer;


    //register a tracker for this class
    private PropertyChangeSupport register = new PropertyChangeSupport(this);

    //constructors
    public Player(String name){
        this.name = name;
        this.score = 0;
        register.addPropertyChangeListener(this);
    }

    public boolean isServer() {
        return isServer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //getters
    public int getScore() {
        return score;
    }

    //getters
    public String getPlayerColor() {
        return playerColor;
    }

    //setters
    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    //add reporter in case score is changed
    public void setScore(int score) {
        int oldScore = this.score;
        this.score = score;
        register.firePropertyChange("Change score: ",oldScore,score);
    }

    public String assignColor( int orderOfPlayers){
        switch (orderOfPlayers){
            case 0:
                playerColor = "Green";
                break;
            case 1:
                playerColor = "Red";
                break;
            case 2:
                playerColor = "Yellow";
                break;
            case 3:
                playerColor = "Blue";
                break;
        }
        return playerColor;
    }

    //in case of changes, do this:
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getPropertyName());
        System.out.println(evt.getOldValue());
        System.out.println(evt.getNewValue());
    }

    public String toString(){
        return  this.getPlayerColor();
    }
}
