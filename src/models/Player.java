package models;

import java.io.Serializable;

public class Player implements Serializable {
    private String name = "";
    private String color = "";
    private int score = 0;

    Player(String name, String color){
        this.name = name;
        this.color = color;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
