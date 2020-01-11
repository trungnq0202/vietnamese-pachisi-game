package controllers;

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;
import models.Dice;
import models.Horse;
import resources.view.HorseNest;
import models.Sound;

import java.util.ArrayList;
import java.util.Arrays;

public class GameBoardController {
    @FXML private Button diceArrow;
    @FXML private Label PN0;
    @FXML private Label PN1;
    @FXML private Label PN2;
    @FXML private Label PN3;
    @FXML private StackPane BNSP;
    @FXML private StackPane RNSP;
    @FXML private StackPane YNSP;
    @FXML private StackPane GNSP;
    @FXML private VBox gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label dice1Val;
    @FXML private Label dice2Val;
    @FXML private Label dice1Title;
    @FXML private Label dice2Title;
    @FXML private Label latestMoveTitle;
    @FXML private Label BPointsTitle;
    @FXML private Label RPointsTitle;
    @FXML private Label YPointsTitle;
    @FXML private Label GPointsTitle;
    @FXML private Label TURN0;
    @FXML private Label TURN1;
    @FXML private Label TURN2;
    @FXML private Label TURN3;

    private static final String RED_CODE = "#ff0000";
    private static final String GREEN_CODE = "#0b940b";
    private static final String BLUE_CODE = "#1183ee";
    private static final String YELLOW_CODE = "#ddd31e";

    private MainController mainController;
    @FXML private DicesController dicesController;
    private static final char[] colors = {'R','B','Y','G'}; //Color of each player according to the order players' id
    private static String[] horseIdOfPosition;  //String array indicating which horse(horseId) is occupying which position
    private static ArrayList<Horse> horsesWithValidMoves;
    private static String[] horseIdOfHomePosition;
    private static int[] scores;
    private Timeline diceArrowAnimation;
    private boolean isRollingDiceTurn;   //Variable indicating that this is the time for the player to roll the dices, no other action can be done
    private boolean isFreeze;
    private int tempPlayerIdTurn;

    //Sound
    private Sound horseMoveSound;
    private Sound horseGoingHomeSound;
    private Sound horseAppearSound;
    private Sound horseKickedSound;
    private Sound launchSound;

    public GameBoardController(){
//        System.out.println("gameboardcontroller construct");
        horseIdOfPosition = new String[48];
        horseIdOfHomePosition = new String[25];
        horsesWithValidMoves = new ArrayList<>();
        Arrays.fill(horseIdOfPosition,null);
        Arrays.fill(horseIdOfHomePosition,null);
        horseMoveSound = new Sound(Sound.SoundType.HORSE_MOVE_SFX);
        horseGoingHomeSound = new Sound(Sound.SoundType.HORSE_JUMP_SFX);
        horseAppearSound = new Sound(Sound.SoundType.HORSE_APPEAR_SFX);
        horseKickedSound = new Sound(Sound.SoundType.HORSE_KICKED_SFX);
        launchSound = new Sound(Sound.SoundType.GAME_LAUNCH_SFX);
        scores = new int[4];
    }

    /**************** DEBUG FUNCTIONS **************/
    private void printHorseIdOfPosition(){
        for (int i = 0; i < 48; i++){
            System.out.println(i + ": " + horseIdOfPosition[i]);
        }
    }

    public void displayHorseIdOfPosition(){
        for (int i = 0; i < 48; i++){
            if (horseIdOfPosition[i] != null)
                System.out.println(i + ": " + horseIdOfPosition[i]);
        }
    }

    public void displayHorseIdOfHomePosition(){
        for (int i = 0; i < 24; i++){
            if (horseIdOfHomePosition[i] != null)
                System.out.println(i + ": " + horseIdOfHomePosition[i]);
        }
    }

    /**************** END DEBUG FUNCTIONS **************/

    @FXML private void initialize(){
//        System.out.println("gameboardcontroller init");
        diceArrowAnimation = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (diceArrow.getTranslateY() == 0) diceArrow.setTranslateY(-10);
            else diceArrow.setTranslateY(0);
        }));;
        diceArrowAnimation.setCycleCount(Timeline.INDEFINITE);
        dicesController.injectGameBoardController(this);
        setUpLabelBindingText();
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    /**************** SETUP BINDING TEXT **************/
    public void setUpLabelBindingText(){
        I18NController.setUpLabelText(RPointsTitle, "gameBoard.points_title");
        I18NController.setUpLabelText(BPointsTitle, "gameBoard.points_title");
        I18NController.setUpLabelText(YPointsTitle, "gameBoard.points_title");
        I18NController.setUpLabelText(GPointsTitle, "gameBoard.points_title");
        I18NController.setUpLabelText(dice1Title, "gameBoard.dice1_title");
        I18NController.setUpLabelText(dice2Title, "gameBoard.dice2_title");
        I18NController.setUpLabelText(latestMoveTitle, "gameBoard.latest_move");
        I18NController.setUpLabelText(TURN0, "gameBoard.your_turn");
        I18NController.setUpLabelText(TURN1, "gameBoard.your_turn");
        I18NController.setUpLabelText(TURN2, "gameBoard.your_turn");
        I18NController.setUpLabelText(TURN3, "gameBoard.your_turn");

    }

    /**************** Position id calculation and conversion **************/
    //Calculating the fxid of the next position after moving a number of steps
    public String calculateNextPosition(int steps, String startPosition, String tmpNextPosition){
        //If the position of this horse is in the nest
        if (startPosition == null) return colors[tempPlayerIdTurn] + "1";

        if (tmpNextPosition == null) tmpNextPosition = startPosition;
        int integerPartOfId = Integer.parseInt(tmpNextPosition.substring(1));  //Get the integer part of the position's fxid

        //If (integer part) + (steps) > 11 -> move out of the temporary color area
        if ( integerPartOfId != 11 && (integerPartOfId + steps) > 11)
            return calculateNextPosition(steps - (11 - integerPartOfId) , tmpNextPosition,tmpNextPosition.substring(0,1) + 11);

        //If the tmpNextPosition is at the final position of a specific color area
        if (integerPartOfId == 11) {
            switch (tmpNextPosition.charAt(0)) {
                case 'R': return calculateNextPosition(steps - 1, tmpNextPosition,"B0");
                case 'G': return calculateNextPosition(steps - 1, tmpNextPosition,"R0");
                case 'B': return calculateNextPosition(steps - 1, tmpNextPosition,"Y0");
                case 'Y': return calculateNextPosition(steps - 1, tmpNextPosition,"G0");
            }
        }

        return tmpNextPosition.charAt(0) + Integer.toString(Integer.parseInt(tmpNextPosition.substring(1)) + steps);
    }

    //Calculating the fxid of the next home position after moving a number of steps
    public String calculateNextHomePosition(int steps, Horse horse){
        String tempPosition = horse.getTempPosition();
        //If the horse is already in home
        if (horse.isInHome() && !horse.isJustEnteredHome()) {
            if (steps != 1 || tempPosition.charAt(2) == '6') return null;
            else return tempPosition.substring(0, 2) + (Integer.parseInt(tempPosition.substring(2)) + 1);

            //If the horse just entered home in this turn
        } else  if (horse.isJustEnteredHome()){
            int nextHomeInt = Integer.parseInt(horse.getTempPosition().substring(2)) + steps;
            if (nextHomeInt <= 6)
                return tempPosition.substring(0, 2) + nextHomeInt;
            //If the horse is at the home door position
        } else {
            if (steps <= 6)
                return "H" + horse.getHorseColor() + steps;
        }
        return null;
    }

    //Converting the fxid of the home position to integer
    public int convertHomePositionToIntegerForm(String homePosition){
//        return Integer.parseInt(homePosition.substring(2) + 5 * tempPlayerIdTurn + tempPlayerIdTurn);
        int integerPartOfPos = Integer.parseInt(homePosition.substring(2));
        switch (homePosition.charAt(1)){
            case 'R' : return integerPartOfPos;
            case 'B' : return integerPartOfPos + 5 + 1;
            case 'Y' : return integerPartOfPos + 5 * 2 + 2;
            case 'G' : return integerPartOfPos + 5 * 3 + 3;
        }
        return 0;
    }

    //Converting the fxid of the position to integer
    public int convertPositionToIntegerForm(String position){
        int integerPartOfPos = Integer.parseInt(position.substring(1));
        switch (position.charAt(0)){
            case 'R' : return integerPartOfPos ;
            case 'B' : return integerPartOfPos + 11  + 1;
            case 'Y' : return integerPartOfPos + 11 * 2 + 2;
            case 'G' : return integerPartOfPos + 11 * 3 + 3;
        }
        return 0;
    }

    /**************** End Position id calculation and conversion **************/

    /****************  Game Initialization **************/
    //Create all horse nests at the start of the game
    private void createHorseNests(){
        RNSP.getChildren().add(3,new HorseNest(colors[0]));
        BNSP.getChildren().add(3,new HorseNest(colors[1]));
        YNSP.getChildren().add(3,new HorseNest(colors[2]));
        GNSP.getChildren().add(3,new HorseNest(colors[3]));
    }

    private void updatePlayersNameView(){
        ArrayList<String> playersNameList = mainController.getPlayersNameList();
        PN0.setText(playersNameList.get(0));
        PN1.setText(playersNameList.get(1));
        PN2.setText(playersNameList.get(2));
        PN3.setText(playersNameList.get(3));
    }

    //initialize game process
    public void startGame(){
        createHorseNests();                                 //Create all horse nest
        updatePlayersNameView();                            //Update players'name
        highLightDices(true);                     //show dices'arrows so that the player know it's time to roll the dices
        Arrays.fill(horseIdOfPosition, null);           //no position is occupied yet
        Arrays.fill(horseIdOfHomePosition, null);       //no home position is occupied yet
        Arrays.fill(scores, 0);
        isRollingDiceTurn = true;                           //set rolling dice turn state yo true
        isFreeze = false;                                   //unfreeze rolling dices
        tempPlayerIdTurn = 0;                               //First turn belongs to player "GREEN"
        gameBoard.lookup("#TURN0").setVisible(true);
//        debug();
    }

    //Show the game board
    public void showGameBoard(boolean isDisplayed){
        if (isDisplayed) {
            launchSound.play();
            gameBoard.setVisible(true);
            startGame();
        } else {
            gameBoard.setVisible(false);
        }
    }

    /**************** End  Game Initialization **************/

    /*************** Horse Animation **************/

    //Horse moving animation
    public void createHorseMovingAnimation(String startPosition, String tempPosition, String endPosition, Horse horse, int steps){
        String nextPosition = calculateNextPosition(1, tempPosition , null);
        int nextPositionInt = convertPositionToIntegerForm(nextPosition);
        //If there is a horse in the end position => get kicked

        //Moving animation
        StackPane nextPositionNode = (StackPane)gameBoard.lookup("#" + nextPosition);
        nextPositionNode.getChildren().add(1, horse);
        horseMoveSound.play();

        //If this horse has yet to be moved to its final position
        if (!nextPosition.equals(endPosition)) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(400), e -> {
                createHorseMovingAnimation(startPosition, nextPosition, endPosition, horse, steps);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        } else {
            printMoveStatus(steps);
            if (horseIdOfPosition[nextPositionInt] != null) {
                Horse horseGetKicked = (Horse)gameBoard.lookup("#" + horseIdOfPosition[nextPositionInt]);
                updateScore(tempPlayerIdTurn, 2);
                updateScore(getPlayerIdByColor(horseIdOfPosition[nextPositionInt].charAt(0)),-2);
                createKickedAnimation(horseGetKicked);
            }
            resetFillColorOfPosition(nextPositionNode, horse);
            horseIdOfPosition[convertPositionToIntegerForm(startPosition)] = null;
            horseIdOfPosition[nextPositionInt] = horse.getId();
            horse.setTempPosition(nextPosition);
            showPossibleHorsesMoves();
        }
    }

    //Horse going outside nest animation
    private void createHorseGoingOutsideNestAnimation(String startPosition, Horse horse){
        int startPositionInt = convertPositionToIntegerForm(startPosition);
        printMoveOutsideNestStatus();
        if (horseIdOfPosition[startPositionInt] != null) {
            Horse horseGetKicked = (Horse)gameBoard.lookup("#" + horseIdOfPosition[startPositionInt]);
            updateScore(tempPlayerIdTurn, 2);
            updateScore(getPlayerIdByColor(horseIdOfPosition[startPositionInt].charAt(0)),-2);
            createKickedAnimation(horseGetKicked);
            horseIdOfPosition[startPositionInt] = null;
        }
        StackPane startPositionNode = (StackPane)gameBoard.lookup("#" + startPosition);
        horseIdOfPosition[startPositionInt] = horse.getId(); //Set the state of next position to be occupied
        horse.setTempPosition(startPosition);
        startPositionNode.getChildren().add(1, horse);
        horseAppearSound.play();
    }

    //Horse going inside home animation
    public void createHorseMovingInsideHomeAnimation(String startPosition, String endPosition, Horse horse, int steps){
        if (horse.isInHome()) {
            horseIdOfHomePosition[convertHomePositionToIntegerForm(startPosition)] = null;
            updateScore(tempPlayerIdTurn, 1);
        } else {
            horse.setInHome(true);
            horseIdOfPosition[convertPositionToIntegerForm(startPosition)] = null;
            updateScore(tempPlayerIdTurn, steps);
        }
        horseIdOfHomePosition[convertHomePositionToIntegerForm(endPosition)] = horse.getId();
        horse.setTempPosition(endPosition);
        StackPane endPositionNode = (StackPane)gameBoard.lookup("#" + endPosition);
        endPositionNode.getChildren().add(1, horse);
        printMoveInsideHomeStatus(Integer.parseInt(endPosition.substring(2)));
        horseGoingHomeSound.play();
        resetFillColorOfPosition(endPositionNode, horse);
        resetFillColorOfPosition(endPositionNode, horse);
        showPossibleHorsesMoves();
    }

    //Horse being kicked animation
    private void createKickedAnimation(Horse horse){
        horse.setTempPosition(null);
        horse.setInNest(true);
        GridPane nestSP = (GridPane)gameBoard.lookup("#" + horse.getHorseColor() + "N");
        nestSP.add(horse,horse.getColumnIndex(), horse.getRowIndex());
        printKickStatus(getPlayerIdByColor(horse.getHorseColor()));
        horseKickedSound.play();
    }

    public void resetFillColorOfPosition(StackPane endPositionNodeSP, Horse horse){
        if (horse.isInHome() || horse.isInHomeDoorPosition()){
            switch (horse.getHorseColor()){
                case 'R' : endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: " + RED_CODE); break;
                case 'B' : endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: " + BLUE_CODE); break;
                case 'Y' : endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: " + YELLOW_CODE); break;
                case 'G' : endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: " + GREEN_CODE); break;
            }
        }
        else {
            endPositionNodeSP.setStyle("-fx-background-color: transparent");
            endPositionNodeSP.getChildren().get(0).setStyle("-fx-fill: transparent");
        }
    }

    /*************** End Horse Animation **************/

    /*************** Event Handlers for horse OnClick event **************/
    private void activateEventHandlerForHorseGoingOutOfNest(Horse horse){
        String startPosition = calculateNextPosition(0, null, null);    //Get fxid of start position
        StackPane startPositionSP = (StackPane)gameBoard.lookup("#" + startPosition);;

        horse.setOnMouseClicked(event -> {
            horse.setInNest(false); //This horse is no longer in the nest
            createHorseGoingOutsideNestAnimation(startPosition, horse);
            unhighlightHorsesInsideNest();
            dicesController.unsetEventHandlerForDices();
            unhighlightHorseOutsideNest(false);
            setDicesUnusable();
            updatePlayerTurn();
        });

        horse.setOnMouseEntered(event -> {
            if (horseIdOfPosition[convertPositionToIntegerForm(startPosition)] != null)
                startPositionSP.setStyle("-fx-background-color: red");
        });

        horse.setOnMouseExited(event -> {
            startPositionSP.setStyle("-fx-background-color: transparent");
        });
    }

    private void deactivateEventHandlerForHorseGoingOutOfNest(Horse horse){
        horse.setOnMouseClicked(null);
        horse.setOnMouseEntered(null);
        horse.setOnMouseExited(null);
    }

    private void activateEventHandlerForHorseOutsideNest(Horse horse){
//        horse.displayListOfPossibleSteps();
        horse.setOnMouseClicked(event -> {
            horse.pauseArrowAnimation();
//            System.out.println(horse.getTempPosition());
            //Shutdown other horses, which are also outside nest
            for (Horse subHorse : horsesWithValidMoves) if (subHorse != horse) deactivateShowPossibleMovesForHorseOutsideNest(subHorse);
            if (dicesController.getDice1().isUsable()) dicesController.setEventHandlerForDice1Pick(this, horse);
            if (dicesController.getDice2().isUsable()) dicesController.setEventHandlerForDice2Pick(this, horse);
        });
    }

    public void deactivateShowPossibleMovesForHorseOutsideNest(Horse horse){
        horse.resumeArrowAnimation();
        dicesController.unsetEventHandlerForDices();
    }

    /********************************************** Game Flow ****************************************************/
    //Check for the winner of the game
    private int checkEndGame(){
        int winnerId = 0;
        for(int playerId = 1; playerId <= 4; playerId++){ //rightHome = homeId * player + (player - 1)
            for (int homeId = 6; homeId >= 4; homeId--) {
                if (mainController.getHorseHomes().get(homeId * playerId + (playerId - 1)) != null) winnerId = playerId;
                else { winnerId = 0; break; }
            }
            if (winnerId != 0) return winnerId;
        }
        return 0;
    }

    private void updatePlayerTurn(){
        //Check double
        if (dicesController.getDice1().getRollNumber() != dicesController.getDice2().getRollNumber()) {
            gameBoard.lookup("#TURN" + tempPlayerIdTurn).setVisible(false);
            if (tempPlayerIdTurn == 3) tempPlayerIdTurn = 0; else tempPlayerIdTurn++;
            gameBoard.lookup("#TURN" + tempPlayerIdTurn).setVisible(true);
        }
        isRollingDiceTurn = true;
        highLightDices(true);
        dicesController.setEventHandlerForDiceRoll();
    }

    /*************** Inside Nest Horses control **************/

    //unhighlight the horses which are in the nest
    public void unhighlightHorsesInsideNest(){
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn]+ "H" + i);
            tempHorse.hideSideArrow();
            deactivateEventHandlerForHorseGoingOutOfNest(tempHorse);
        }
    }

    //Highlight the horses which are in the nest
    private void highLightHorsesInsideNest(){
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (tempHorse.isInNest()) {
                tempHorse.showSideArrow();
                activateEventHandlerForHorseGoingOutOfNest(tempHorse);
            }
        }
    }

    /*************** End Inside Nest Horses control **************/

    /*************** Outside Nest Horses control **************/
    public void unhighlightHorseOutsideNest(boolean existHorseJustEnteredHome) {
        for (int i = 0; i < 4; i++) {
            Horse horse = (Horse) gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (!horse.isInNest()) {
                horse.hideSideArrow();
                horse.setOnMouseClicked(null);
                if (horse.isJustEnteredHome() && !existHorseJustEnteredHome) horse.setJustEnteredHome(false);
            }
        }
    }

    public void highlightHorseOutsideNest(){
        for (Horse horse:horsesWithValidMoves) {
            horse.showSideArrow();
            activateEventHandlerForHorseOutsideNest(horse);
        }
    }

    private boolean checkAvailableOutsideNestHorse(){
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (!tempHorse.isInNest()) return true;
        }
        return false;
    }

    /*************** End Outside Nest Horses control **************/

    /*************** Check possible moves and display **************/
    private void getHorsesWithValidMoves(){
        horsesWithValidMoves.clear();               //reset the list
        Dice dice1 = dicesController.getDice1();
        Dice dice2 = dicesController.getDice2();

        //Consider every outside-nest horses belonging to the temporary player
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i); //Get the horse object according to id

            //Debugging
//            System.out.println(tempHorse.getId());
//            System.out.println(tempHorse.isInNest());
//            System.out.println(dice1.isUsable());
//            System.out.println(dice2.isUsable());

            //If this horse is outside the nest
            if (!tempHorse.isInNest()) {
//                tempHorse.resetListOfPossibleSteps();       //Reset possible the list of steps can be made
                //If the horse is not moved by the player yet
                if (dice1.isUsable()) checkPossibleMoveWithDice(dice1, null, tempHorse);
                if (dice2.isUsable()) checkPossibleMoveWithDice(null, dice2, tempHorse);
                if (dice1.isUsable() && dice2.isUsable()) checkPossibleMoveWithDice(dice1, dice2, tempHorse);
            }
        }
    }

    //Check that whether or not, with dice 1, dice 2 or both, the horse will be able to move
    private void checkPossibleMoveWithDice(Dice dice1, Dice dice2, Horse horse){
//        System.out.println("//////// checkPossibleMoveWithDice //////");
//        displayHorseIdOfPosition();
//        System.out.println("is in home :" + horse.isInHome());
//        System.out.println(horse);
//        System.out.println("is in home door pos : " + horse.isInHomeDoorPosition());
        boolean hasPossibleMove = false;
        horse.resetListOfPossibleSteps();
        //If temp Horse is already in home and not just entered the home in this turn
        if (horse.isInHome() && !horse.isJustEnteredHome()){
            System.out.println("cc");
//            System.out.println("th1");
            //If dice1 roll number is 1         sou
            if (dice1 != null && (dice1.getRollNumber() == Integer.parseInt(horse.getTempPosition().substring(2)) + 1) && checkInvalidScalingHome(horse)){
                hasPossibleMove = true;
                horse.setPossibleStepsListByIndex(0,1);
            }

            //If dice2 roll number is 1
            if (dice2 != null && (dice2.getRollNumber() == Integer.parseInt(horse.getTempPosition().substring(2)) + 1) && checkInvalidScalingHome(horse)){
                hasPossibleMove = true;
                horse.setPossibleStepsListByIndex(1,1);
            }

            //If tempHorse is at home door position
        } else if (horse.isInHomeDoorPosition() || horse.isJustEnteredHome()){
//            System.out.println("th2");
            if (dice1 != null && checkEnterHomeBlocked(dice1.getRollNumber(), horse)) {hasPossibleMove = true; horse.setPossibleStepsListByIndex(0,dice1.getRollNumber());}
            if (dice2 != null && checkEnterHomeBlocked(dice2.getRollNumber(), horse)) {hasPossibleMove = true; horse.setPossibleStepsListByIndex(1,dice2.getRollNumber()); }

            //If tempHorse is simply outside of nest , not in home and not at home door position
        } else {
//            System.out.println("th3");
            if (dice1 != null && checkBlocked(dice1.getRollNumber(), horse) && checkOverstepHomeDoorPosition(dice1.getRollNumber(), horse))
            { hasPossibleMove = true; horse.setPossibleStepsListByIndex(0,dice1.getRollNumber()); }

            if (dice2 != null && checkBlocked(dice2.getRollNumber(), horse) && checkOverstepHomeDoorPosition(dice2.getRollNumber(), horse))
            { hasPossibleMove = true; horse.setPossibleStepsListByIndex(1,dice2.getRollNumber());}
        }

        if (hasPossibleMove && !horsesWithValidMoves.contains(horse)) horsesWithValidMoves.add(horse);
    }

    //Check if the next move of the horse is blocked by other horses
    private boolean checkBlocked(int steps, Horse horse){
        String tempPosition = horse.getTempPosition();
//        System.out.println("CHECKBLOCKED");
        //Check if there is no horse between start pos to end pos
        for (int tempStep = 1; tempStep < steps; tempStep++){
            String nextPosition = calculateNextPosition(tempStep, tempPosition, null);
//            System.out.println("next position - checkblocked:" + nextPosition);
            int nextPositionInInt = convertPositionToIntegerForm(nextPosition);
//            System.out.println("next position int - checkblocked:" + nextPositionInInt);
//            System.out.println("horseIdOfPosition[nextPositionInInt] = " + horseIdOfPosition[nextPositionInInt] );
            if (horseIdOfPosition[nextPositionInInt] != null) return false;
        }

        int endPositionInt = convertPositionToIntegerForm(calculateNextPosition(steps,tempPosition, null));
//        System.out.println(calculateNextPosition(steps,tempPosition, null));
//        System.out.println("end position int - checkblocked:" + endPositionInt);
//        System.out.println("horseIdOfPosition[endPositionInt] = " + horseIdOfPosition[endPositionInt] );
        //Check if there is a horse at the last pos, and that horse must not be the same type as the one being considered, or else it would be considered blocked
//        System.out.println(horseIdOfPosition[endPositionInt] == null || horseIdOfPosition[endPositionInt].charAt(0) != colors[tempPlayerIdTurn]);
        return horseIdOfPosition[endPositionInt] == null || horseIdOfPosition[endPositionInt].charAt(0) != colors[tempPlayerIdTurn];
    }

    //Check if the next move of the horse will pass the home position
    private boolean checkOverstepHomeDoorPosition(int steps, Horse horse){
        String tempPosition = horse.getTempPosition();
        String endPosition = calculateNextPosition(steps, tempPosition, null);
        //if the start position is NOT IN the area with same color
        if (tempPosition.charAt(0) != colors[tempPlayerIdTurn]){
            //If the end position is IN the area with same color
            if (endPosition.charAt(0) == colors[tempPlayerIdTurn]){
                //Confirm overstep home door position of the integer part of the end position is not 0
                return endPosition.charAt(1) == '0';
            }
        }
        return true;
    }

    //Check if the home position the horse will move to is being blocked (this case is for those horses that are standing at the home door position)
    private boolean checkEnterHomeBlocked(int steps, Horse horse){
//        System.out.println("checkEnterHomeBlocked");
        String nextHomePosition;
//        = calculateNextHomePosition(steps, horse);
//        if (nextHomePosition != null) return horseIdOfHomePosition[convertHomePositionToIntegerForm(nextHomePosition)] == null;
//        else return false;
        for (int tempStep = 1; tempStep <= steps; tempStep++){
            nextHomePosition = calculateNextHomePosition(tempStep, horse);
            if (nextHomePosition == null || horseIdOfHomePosition[convertHomePositionToIntegerForm(nextHomePosition)] != null) return false;
        }
        return true;
    }

    //Check if the home position the horse will move to is being blocked (this case is for those horses that are already in home
    private boolean checkInvalidScalingHome(Horse horse){
        String nextHomePosition = calculateNextHomePosition(1, horse);
        if (nextHomePosition != null) return horseIdOfHomePosition[convertHomePositionToIntegerForm(nextHomePosition)] == null;
        else return false;
    }

    /*************** End Check possible moves and display **************/
    public void showPossibleHorsesMoves(){
        getHorsesWithValidMoves();
        if ( horsesWithValidMoves.size() != 0 ){
            highlightHorseOutsideNest();
        } else {
            unhighlightHorseOutsideNest(false);
            if (!dicesController.getDice1().isUsable() || !dicesController.getDice2().isUsable()
                    || !(dicesController.getDice1().getRollNumber() == 6 || dicesController.getDice2().getRollNumber() == 6))
            {
                unhighlightHorsesInsideNest();
                updatePlayerTurn();
            }

        }
    }

    //process after rolling dices
    public void processPostDiceRolling(){
        boolean isHorseGoingOutsideNest = false;
        highLightDices(false);  //Unhighlight the dices
        isRollingDiceTurn = false; //Do not allow the players to roll dice until they've finished their horses moves

        //If the 1 dices contains a 6 and the start position is not occupied by this player's horse
        if ((dicesController.getDice1().getRollNumber() == 6 || dicesController.getDice2().getRollNumber() == 6)){
            String horseIdAtStartPosition = horseIdOfPosition[1 + 11 * tempPlayerIdTurn + tempPlayerIdTurn];
            if ( horseIdAtStartPosition == null || horseIdAtStartPosition.charAt(0) != colors[tempPlayerIdTurn] ){
                highLightHorsesInsideNest(); //Highlight horses to notify the player that he/she can get a new horse out of the nest
                isHorseGoingOutsideNest = true;
            }
        }

        //Show possible moves for horses which are out of nest
        setDicesUsable();                       //Reset the 2 dices to usable states
        if (checkAvailableOutsideNestHorse())  showPossibleHorsesMoves(); //Show all possible moves of each horses for the players to pick
        else if (!isHorseGoingOutsideNest) updatePlayerTurn();

    }

    //Highlight the dices in order to notify the players that it is their turn to roll
    private void highLightDices(boolean isDisplayed){
        if (isDisplayed)  {
            diceArrow.setVisible(true);
            diceArrowAnimation.play();
        }
        else {
            diceArrow.setVisible(false);
            diceArrowAnimation.stop();
        }
    }

    //Reset the 2 dices to usable states
    public void setDicesUsable(){
        dicesController.getDice1().setUsable(true);
        dicesController.getDice2().setUsable(true);
    }

    //Set the 2 dices to usable states
    public void setDicesUnusable(){
        dicesController.getDice1().setUsable(false);
        dicesController.getDice2().setUsable(false);
    }

    public boolean isRollingDiceTurn() {
        return isRollingDiceTurn;
    }

    public boolean isFreeze() {
        return isFreeze;
    }

    public void setFreeze(boolean freeze) {
        isFreeze = freeze;
    }

    public VBox getGameBoard() {
        return gameBoard;
    }

    public static String getHorseIdOfPositionByIndex(int index) {
        return horseIdOfPosition[index];
    }

    public void updateDiceNumView(){
        dice1Val.setText(String.valueOf(dicesController.getDice1().getRollNumber()));
        dice2Val.setText(String.valueOf(dicesController.getDice2().getRollNumber()));
    }

    public void updateScore(int playerId, int deltaScore){
//        System.out.println("Player id: " + playerId);
//        System.out.println("Delta score: " + deltaScore);
        for (int i = 0; i < 4; i++) System.out.println(i + " " + scores[i]);
        scores[playerId] += deltaScore;
        updateScoreView(playerId);
    }

    public void updateScoreView(int playerId){
        Label scoreLabel = (Label)gameBoard.lookup("#" + colors[playerId] + "Points");
        scoreLabel.setText(String.valueOf(scores[playerId]));
    }

    public int getPlayerIdByColor(char color){
        switch (color){
            case 'R': return 0;
            case 'B': return 1;
            case 'Y': return 2;
            case 'G': return 3;
        }
        return 0;
    }

    //Print description of the latest horse on the status label
    public void printMoveStatus(int steps){
        String moveStatus = "";
        if (I18NController.isEnglish()) {
            moveStatus += mainController.getPlayersNameList().get(tempPlayerIdTurn);
            moveStatus += "'s " + I18NController.get("gameBoard.horse") + " ";        //'s horse
            moveStatus += I18NController.get("gameBoard.moved") + " ";        //moved
            moveStatus += steps + " " + I18NController.get("gameBoard.space");
            if (steps > 1) moveStatus += "s";
        } else {
            moveStatus += I18NController.get("gameBoard.horse") + " ";        //'s horse
            moveStatus += mainController.getPlayersNameList().get(tempPlayerIdTurn) + " ";
            moveStatus += I18NController.get("gameBoard.moved") + " ";        //moved
            moveStatus += steps + " " + I18NController.get("gameBoard.space");
        }
        statusLabel.setText(moveStatus);
    }

    //Print description of the horse kicking movement on the status label
    public void printKickStatus(int playerId){
        String kickStatus = statusLabel.getText();
        kickStatus += I18NController.get("gameBoard.kicked") + " ";
        if (I18NController.isEnglish()) kickStatus += mainController.getPlayersNameList().get(playerId) + "'s " + I18NController.get("gameBoard.horse") + " " ;
        else kickStatus += I18NController.get("gameBoard.horse_2") + " " + mainController.getPlayersNameList().get(playerId) + " " ;
        kickStatus += I18NController.get("gameBoard.sub_kicked");
        statusLabel.setText(kickStatus);
    }

    //Print description of horse moving outside nest on the status label
    public void printMoveOutsideNestStatus(){
        String moveOutsideNestStatus = "";
        if (I18NController.isEnglish()){
            moveOutsideNestStatus += mainController.getPlayersNameList().get(tempPlayerIdTurn);
            moveOutsideNestStatus += "'s " + I18NController.get("gameBoard.horse") + " ";        //'s horse
            moveOutsideNestStatus += I18NController.get("gameBoard.moved") + " ";        //moved
            moveOutsideNestStatus += I18NController.get("gameBoard.move_outside_nest");
        } else {
            moveOutsideNestStatus += I18NController.get("gameBoard.horse") + " ";        //'s horse
            moveOutsideNestStatus += mainController.getPlayersNameList().get(tempPlayerIdTurn) + " ";
            moveOutsideNestStatus += I18NController.get("gameBoard.move_outside_nest");
        }
        statusLabel.setText(moveOutsideNestStatus);
    }

    //Print description of horse moving in home on the status label
    public void printMoveInsideHomeStatus(int homeNum){
        String moveInsideHomeStatus = "";
        if (I18NController.isEnglish()){
            moveInsideHomeStatus += mainController.getPlayersNameList().get(tempPlayerIdTurn);
            moveInsideHomeStatus += "'s " + I18NController.get("gameBoard.horse") + " ";        //'s horse
            moveInsideHomeStatus += I18NController.get("gameBoard.moved") + " ";        //moved
        } else {
            moveInsideHomeStatus += I18NController.get("gameBoard.horse") + " ";        //'s horse
            moveInsideHomeStatus += mainController.getPlayersNameList().get(tempPlayerIdTurn) + " ";
        }
        moveInsideHomeStatus += I18NController.get("gameBoard.move_inside_home") + " " + homeNum;
        statusLabel.setText(moveInsideHomeStatus);
    }

//        private void debug(){
////        Horse horse = (Horse)gameBoard.lookup("#BH0");
////        horse.setTempPosition("B0");
////        horseIdOfPosition[convertPositionToIntegerForm("B0")] = "BH0";
////        horse.setInNest(false);
////        StackPane nextPositionNode = (StackPane)gameBoard.lookup("#B0");
////        nextPositionNode.getChildren().add(1, horse);
//
//        Horse horse = (Horse)gameBoard.lookup("#GH0");
//        horse.setTempPosition("HG6");
//        horseIdOfHomePosition[convertHomePositionToIntegerForm("HG6")] = "GH0";
//        horse.setInNest(false);
//        horse.setInHome(true);
//        StackPane nextPositionNode = (StackPane)gameBoard.lookup("#HG6");
//        nextPositionNode.getChildren().add(1, horse);
//
//        Horse horse1 = (Horse)gameBoard.lookup("#GH1");
//        horse1.setTempPosition("G0");
//        horseIdOfPosition[convertPositionToIntegerForm("G0")] = "GH1";
//        horse1.setInNest(false);
//        horse1.setInHome(false);
//        horse1.setJustEnteredHome(false);
//        StackPane nextPositionNode1 = (StackPane)gameBoard.lookup("#G0");
//        nextPositionNode1.getChildren().add(1, horse1);
//    }
}
