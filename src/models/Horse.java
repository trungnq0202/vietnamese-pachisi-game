package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Horse extends ImageView {
    private static final String RED_HORSE_IMG_URL = "file:src/resources/images/red_horse.png";
    private static final String GREEN_HORSE_IMG_URL = "file:src/resources/images/green_horse.png";
    private static final String BLUE_HORSE_IMG_URL = "file:src/resources/images/blue_horse.png";
    private static final String YELLOW_HORSE_IMG_URL = "file:src/resources/images/yellow_horse.png";

//    private String horseColor;
    private char horseColor;
    private int nestRow;
    private int nestColumn;
    private boolean isInHome;
    private String tempPosition;
    private String homePosition;

    public Horse(String horseColor, int nestRow, int nestColumn, int horseNo){
        this.nestRow = nestRow;
        this.nestColumn = nestColumn;
        this.horseColor = horseColor.charAt(0);
        this.isInHome = false;
        this.tempPosition = null;
        this.homePosition = null;
        this.setFitHeight(150);
        this.setFitWidth(150);
        Image horseImg;
        switch (horseColor){
            case "RED":{
                this.setId("RH" + Integer.toString(horseNo));
                horseImg = new Image(RED_HORSE_IMG_URL);
                this.setImage(horseImg);
                break;
            }
            case "GREEN":{
                this.setId("GH" + Integer.toString(horseNo));
                horseImg = new Image(GREEN_HORSE_IMG_URL);
                this.setImage(horseImg);
                break;
            }
            case "BLUE":{
                this.setId("BH" + Integer.toString(horseNo));
                horseImg = new Image(BLUE_HORSE_IMG_URL);
                this.setImage(horseImg);
                break;
            }
            case "YELLOW":{
                this.setId("YH" + Integer.toString(horseNo));
                horseImg = new Image(YELLOW_HORSE_IMG_URL);
                this.setImage(horseImg);
                break;
            }
        }
    }

//    public String getHorseColor() {
//        return horseColor;
//    }

    public char getHorseColor() {
        return horseColor;
    }

    public int getNestColumn() {
        return nestColumn;
    }

    public int getNestRow() {
        return nestRow;
    }

    private boolean checkFinalPosition(){
        return horseColor == tempPosition.charAt(0) && tempPosition.charAt(1) == 0;
    }


    //Test calculate next position
    public String getNextPosition(int steps){
        if (tempPosition == null) {
            tempPosition = "G1";
            return tempPosition;
        }

//        if (checkFinalPosition()) return ... (Handle this later)

        if (tempPosition.substring(1).equals("11")) {
            switch (tempPosition.charAt(0)) {
                case 'R':
                    tempPosition = "B0";
                    break;
                case 'G':
                    tempPosition = "R0";
                    break;
                case 'B':
                    tempPosition = "Y0";
                    break;
                case 'Y':
                    tempPosition = "G0";
                    break;
            }
            return tempPosition;
        }

        tempPosition = tempPosition.charAt(0) + Integer.toString(Integer.parseInt(tempPosition.substring(1)) + steps);
        return tempPosition;
    }
}
