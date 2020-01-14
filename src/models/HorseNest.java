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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class HorseNest extends GridPane {
    // constructor
    public HorseNest(char color){
        setProperties();
        setNestId(color);
        addHorses(color);
    }

    // add horse to nest
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
        this.setPadding(new Insets(60,0,0,0));
        this.setHgap(60);
        this.setVgap(60);
    }
}
