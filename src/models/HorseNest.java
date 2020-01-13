package models;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import models.Horse;

public class HorseNest extends GridPane {
    public HorseNest(char color){
        setProperties();
        setNestId(color);
        addHorses(color);
    }

    private void addHorses(char color){
        this.add(new Horse(color,0, 0,0), 0, 0);
        this.add(new Horse(color,1,0,1), 1, 0);
        this.add(new Horse(color,2,1,0), 0, 1);
        this.add(new Horse(color,3,1,1), 1, 1);
    }

    private void setNestId(char color){
        switch (color){
            case 'R': {
                this.setId("RN");
                break;
            }
            case 'G': {
                this.setId("GN");
                break;
            }
            case 'B': {
                this.setId("BN");
                break;
            }
            case 'Y':  {
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
        this.setHgap(60);
        this.setVgap(60);
    }


}
