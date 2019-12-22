package models;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class HorseNest extends GridPane {
//    private ArrayList<Horse> horseNest;

    public HorseNest(String color){
        this.setMinWidth(100);
        this.setMinHeight(100);
        this.maxWidth(100);
        this.maxHeight(100);
        this.setPrefWidth(100);
        this.setPrefHeight(100);
        this.setStyle("-fx-background-color: red");

//        this.color = color;
        switch (color){
            case "RED":     this.setId("RN");
            case "GREEN": {
                this.setId("GN");
                this.getChildren().addAll(new Horse("GREEN",0,0,1));
            }
            case "BLUE":    this.setId("BN");
            case "YELLOW":  this.setId("YN");
        }
    }

    public void addHorse(Horse horse){
//        this.horseNest.add(horse);
        this.add(horse, horse.getNestColumn(), horse.getNestRow());
    }

    public void removeHorse(Horse horse){
//        this.horseNest.remove(horse);
        this.getChildren().remove(horse);
    }

//    public String getColor() {
//        return color;
//    }
}
