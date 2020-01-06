package models;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class Horse extends Button {
    private static final String RED_HORSE_IMG_URL = "file:src/resources/images/red_horse.png";
    private static final String GREEN_HORSE_IMG_URL = "file:src/resources/images/green_horse.png";
    private static final String BLUE_HORSE_IMG_URL = "file:src/resources/images/blue_horse.png";
    private static final String YELLOW_HORSE_IMG_URL = "file:src/resources/images/yellow_horse.png";

//    private String horseColor;
    private char horseColor;
    private int nestRow;
    private int nestColumn;
    private boolean isInNest;
    private String tempPosition;
    private String homePosition;

    public Horse(String horseColor, int nestRow, int nestColumn, int horseNo){
        this.nestRow = nestRow;
        this.nestColumn = nestColumn;
        this.horseColor = horseColor.charAt(0);
        this.isInNest = true;
        this.tempPosition = null;
        this.homePosition = null;
//        this.setStyle("-fx-background-color: gray");
//        this.setFitHeight(150);
//        this.setFitWidth(150);
        this.setDisable(false);
        this.setMinWidth(USE_PREF_SIZE);
        this.setMinHeight(USE_PREF_SIZE);
        this.setPrefWidth(150);
        this.setPrefHeight(150);
        this.setMaxWidth(USE_PREF_SIZE);
        this.setMaxHeight(USE_PREF_SIZE);
//        this.setOnMouseClicked(event -> {
//            System.out.println("ccccc");
//        });
//        this.setText("concac");
        setHorseIdAndImg(horseColor, horseNo);
    }

    public char getHorseColor() {
        return horseColor;
    }

    public int getNestColumn() {
        return nestColumn;
    }

    public int getNestRow() {
        return nestRow;
    }

    public void setTempPosition(String tempPosition) {
        this.tempPosition = tempPosition;
    }

    private void setHorseIdAndImg(String horseColor, int horseNo){
//        Image horseImg = null;
        //ImageView horseImg = null;
        Background horseImg = null;
        switch (horseColor){
            case "RED":{
                this.setId("RH" + horseNo);
//                horseImg = new Image(RED_HORSE_IMG_URL);
//                horseImg = new ImageView(new Image(RED_HORSE_IMG_URL));
                horseImg = new Background(new BackgroundImage(new Image(RED_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(150, 150, false, false, false, false)));

                break;
            }
            case "GREEN":{
                this.setId("GH" + horseNo);
//                horseImg = new Image(GREEN_HORSE_IMG_URL);
//                horseImg = new ImageView(new Image(GREEN_HORSE_IMG_URL));
                horseImg = new Background(new BackgroundImage(new Image(GREEN_HORSE_IMG_URL), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(this.getWidth(), this.getHeight() , true, true, true, false)));

                break;
            }
            case "BLUE":{
                this.setId("BH" + horseNo);
//                horseImg = new Image(BLUE_HORSE_IMG_URL);
//                horseImg = new ImageView(new Image(BLUE_HORSE_IMG_URL));
                break;
            }
            case "YELLOW":{
                this.setId("YH" + horseNo);
//                horseImg = new Image(YELLOW_HORSE_IMG_URL);
//                horseImg = new ImageView(new Image(YELLOW_HORSE_IMG_URL));
                break;
            }
        }
//        this.setImage(horseImg);
//        horseImg.setFitWidth(150);
//        horseImg.setFitHeight(150);
//        this.setGraphic(horseImg);
        this.setBackground(horseImg);
    }

    //Check if this
    private boolean checkFinalPosition(){
        return horseColor == tempPosition.charAt(0) && tempPosition.charAt(1) == 0;
    }

    //Calculate next position
    public String calculateNextPosition(int steps, String tmpNextPosition){
        //If the position of this horse is in the nest
        if (tempPosition == null) return horseColor + "1";

//        if (checkFinalPosition()) return ... (Handle this later)

        if (tmpNextPosition == null) tmpNextPosition = tempPosition;
        int integerPartOfId = Integer.parseInt(tmpNextPosition.substring(1));  //Get the integer part of the fxid

        //If (integer part) + (steps) > 11 -> move out of the temporary color area
        if ( integerPartOfId != 11 && (integerPartOfId + steps) > 11)
            return calculateNextPosition(steps - (11 - integerPartOfId) ,  tmpNextPosition.substring(0,1) + 11);

        //If the tmpNextPosition is at the final position of a specific color area
        if (integerPartOfId == 11) {
            switch (tmpNextPosition.charAt(0)) {
                case 'R': return calculateNextPosition(steps - 1, "B0");
                case 'G': return calculateNextPosition(steps - 1, "R0");
                case 'B': return calculateNextPosition(steps - 1, "Y0");
                case 'Y': return calculateNextPosition(steps - 1, "G0");
            }
        }

        return tmpNextPosition.charAt(0) + Integer.toString(Integer.parseInt(tmpNextPosition.substring(1)) + steps);
    }

    public boolean isInNest() {
        return isInNest;
    }

    public void setInNest(boolean inNest) {
        isInNest = inNest;
    }

    public void setActiveHorse(){
        System.out.println("cc1");
        this.getStyleClass().add("active");
        System.out.println(this.getStyleClass().get(0));
        this.setOnMouseClicked(event -> {
            System.out.println("cl");
//            this.setTranslateX(-20);
//            this.setStyle("-fx-background-color: gray");
        });

        this.setOnMouseExited(event -> {
//            this.setFitWidth(150);
//            this.setFitHeight(150);
            this.setStyle("-fx-background-color: transparent");
        });
    }

    public void setInactiveHorse(){
        System.out.println("cc");
        this.setOnMouseEntered(null);
        this.setOnMouseExited(null);
    }
}
