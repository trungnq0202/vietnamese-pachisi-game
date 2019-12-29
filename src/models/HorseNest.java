package models;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;

public class HorseNest extends GridPane {
    private ArrayList<Integer> horseHomePos;
    private boolean isOutOfNest[];

    public HorseNest(String color){
        setProperties();
        setNestId(color);
        addHorses(color);
    }

    private void addHorses(String color){
        this.horseHomePos = new ArrayList<>(){
            {for (int i = 0; i < 4; i++) add(0);}
        };
        isOutOfNest = new boolean[4];
        Arrays.fill(isOutOfNest, false);
        this.add(new Horse(color, 0, 0, 0), 0, 0);
        this.add(new Horse(color, 0, 1, 1), 1, 0);
        this.add(new Horse(color, 1, 0, 2), 0, 1);
        this.add(new Horse(color, 1, 1, 3), 1, 1);
    }

    private void setNestId(String color){
        switch (color){
            case "RED": {
                this.setId("RN");
                break;
            }
            case "GREEN": {
                this.setId("GN");
                break;
            }
            case "BLUE": {
                this.setId("BN");
                break;
            }
            case "YELLOW":  {
                this.setId("YN");
                break;
            }
        }
    }

    private void setProperties(){
        this.setMinWidth(USE_PREF_SIZE);
        this.setMinHeight(USE_PREF_SIZE);
        this.setPrefWidth(300);
        this.setPrefHeight(300);
        this.maxWidth(USE_PREF_SIZE);
        this.maxHeight(USE_PREF_SIZE);
        this.setAlignment(Pos.CENTER);
    }


}
