package controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import models.Dice;
import models.Horse;
import models.HorseNest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameBoardController {
    @FXML private HBox dices;
    @FXML private Button soundBtn;
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
    private static ArrayList<Horse> horsesWithValidMovesList;
    private static String[] horseIdOfHomePosition;
    private static int[] scores;
    private Timeline diceArrowAnimation;
    private boolean isRollingDiceTurn;   //Variable indicating that this is the time for the player to roll the dices, no other action can be done
    private boolean isFreeze;
    private int tempPlayerIdTurn;
    private boolean isHorseGoingOutsideNest;
    private BotController botController;

    public GameBoardController(){
//        System.out.println("gameboardcontroller construct");
        horseIdOfPosition = new String[48];
        horseIdOfHomePosition = new String[25];
        horsesWithValidMovesList = new ArrayList<>();
        Arrays.fill(horseIdOfPosition,null);
        Arrays.fill(horseIdOfHomePosition,null);
        scores = new int[4];
        isHorseGoingOutsideNest = false;
    }

    @FXML private void initialize(){
        diceArrowAnimation = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (diceArrow.getTranslateY() == 0) diceArrow.setTranslateY(-10);
            else diceArrow.setTranslateY(0);
        }));;
        diceArrowAnimation.setCycleCount(Timeline.INDEFINITE);
        dicesController.injectGameBoardController(this);
        setUpLabelBindingText();
        botController = new BotController(dicesController.getDice1(), dicesController.getDice2() , this);
        setSoundEnableBtnEventHandler();
        this.soundBtn.setBackground(SoundController.bgSoundOnImg);
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
    private void clearHorseNestsAndScore(){
        for (int i = 0; i < mainController.getTotalNumberOfPlayers(); i++) {
            StackPane nestStackPane = (StackPane)gameBoard.lookup("#" + colors[i] + "NSP");
            if (nestStackPane.getChildren().size() > 3){
                GridPane nestSP = (GridPane)nestStackPane.getChildren().get(3);
                for (int j = 0; j < 4; j++){
                    Horse horse = (Horse)gameBoard.lookup("#" + colors[i] + "H" + j);
                    if (!horse.isInNest()) nestSP.add(horse,horse.getColumnIndex(), horse.getRowIndex());
                }
                nestStackPane.getChildren().remove(3);
                Label pointsLabel = (Label)gameBoard.lookup("#" + colors[i] + "Points");
                pointsLabel.setText("0");
            }
        }
    }

    //Create all horse nests at the start of the game
    private void createHorseNests(){
        for (int i = 0; i < mainController.getTotalNumberOfPlayers(); i++) {
            StackPane nestStackPane = (StackPane)gameBoard.lookup("#" + colors[i] + "NSP");
            HBox pointHBox = (HBox)gameBoard.lookup("#" + colors[i] + "PHB");
            nestStackPane.getChildren().add(3, new HorseNest(colors[i]));
            pointHBox.setVisible(true);
        }
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
        clearHorseNestsAndScore();
        createHorseNests();                                 //Create all horse nest
        updatePlayersNameView();                            //Update players'name
        highLightDices(true);                     //show dices'arrows so that the player know it's time to roll the dices
        Arrays.fill(horseIdOfPosition, null);           //no position is occupied yet
        Arrays.fill(horseIdOfHomePosition, null);       //no home position is occupied yet
        Arrays.fill(scores, 0);
        isRollingDiceTurn = true;                           //set rolling dice turn state yo true
        isFreeze = false;                                   //unfreeze rolling dices
        tempPlayerIdTurn = 0;                               //First turn belongs to player "GREEN"
        isHorseGoingOutsideNest = false;
        gameBoard.lookup("#TURN0").setVisible(true);
        dicesController.setEventHandlerForDiceRoll();
        if (checkBotPlayerTurn()) botController.autoRollDice();
//        debug();
    }

    //Show the game board
    public void showGameBoard(boolean isDisplayed){
        if (isDisplayed) {
            SoundController.playGameLaunchSound();
            gameBoard.setVisible(true);
            SoundController.playBackgroundSound();
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
        SoundController.playHorseMoveSound();

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
        resetFillColorOfPosition(startPositionNode, horse);
        SoundController.playHorseAppearSound();
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
        SoundController.playHorseJumpSound();
        resetFillColorOfPosition(endPositionNode, horse);
        int firstFinishId = checkEndGame();
        if (firstFinishId != -1) {
            // TODO: 1/13/2020 add UI end game
            mainController.displayEndGameMenu(firstFinishId);
        }
        else showPossibleHorsesMoves();
    }

    //Horse being kicked animation
    private void createKickedAnimation(Horse horse){
        horse.setTempPosition(null);
        horse.setInNest(true);
        GridPane nestSP = (GridPane)gameBoard.lookup("#" + horse.getHorseColor() + "N");
        nestSP.add(horse,horse.getColumnIndex(), horse.getRowIndex());
        printKickStatus(getPlayerIdByColor(horse.getHorseColor()));
        SoundController.playHorseKickedSound();
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

    /*************** Event handlers for buttons *********************/
    public void setSoundEnableBtnEventHandler() {
        this.soundBtn.setOnMouseClicked((MouseEvent e) -> {
            //If the system's sound is enabled
            if (SoundController.isSoundEnabled()) {
                SoundController.toggleSound();                  //Set the system's sound to mute state
                this.soundBtn.setBackground(SoundController.bgSoundOffImg);   //Set "sound off" background image for soundEnabledBtn
            } else { //If the system's sound is being muted
                SoundController.playButtonClickSound();         //Make "button sound" when clicked
                SoundController.toggleSound();                          //Set the system's sound to on state
                this.soundBtn.setBackground(SoundController.bgSoundOnImg);    //Set "sound on" background image for soundEnabledBtn
            }
        });
    }

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
        horse.setOnMouseClicked(event -> {
            horse.pauseArrowAnimation();
            //Shutdown other horses, which are also outside nest
            for (Horse subHorse : horsesWithValidMovesList) if (subHorse != horse) deactivateShowPossibleMovesForHorseOutsideNest(subHorse);
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
        for (int playerId = 0; playerId < 4; playerId++){ //rightHome = homeId * player + (player - 1)
            if (    horseIdOfHomePosition[convertHomePositionToIntegerForm("H" + colors[playerId] + 6)] != null
                    && horseIdOfHomePosition[convertHomePositionToIntegerForm("H" + colors[playerId] + 5)] != null
                    && horseIdOfHomePosition[convertHomePositionToIntegerForm("H" + colors[playerId] + 4)] != null
                    && horseIdOfHomePosition[convertHomePositionToIntegerForm("H" + colors[playerId] + 3)] != null
            ) return playerId;
        }
        return -1;
    }

    private void updatePlayerTurn(){
        //Check double
        if (dicesController.getDice1().getRollNumber() != dicesController.getDice2().getRollNumber()) {
            gameBoard.lookup("#TURN" + tempPlayerIdTurn).setVisible(false);
            if (tempPlayerIdTurn == (mainController.getTotalNumberOfPlayers() - 1)) tempPlayerIdTurn = 0; else tempPlayerIdTurn++;
            gameBoard.lookup("#TURN" + tempPlayerIdTurn).setVisible(true);
        }
        isRollingDiceTurn = true;
        highLightDices(true);
        dicesController.setEventHandlerForDiceRoll();
        if (checkBotPlayerTurn()) botController.autoRollDice();
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
        for (Horse horse:horsesWithValidMovesList) {
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
        horsesWithValidMovesList.clear();               //reset the list
        Dice dice1 = dicesController.getDice1();
        Dice dice2 = dicesController.getDice2();
        //Consider every outside-nest horses belonging to the temporary player
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i); //Get the horse object according to id
            //If this horse is outside the nest
            if (!tempHorse.isInNest()) {
                //If the horse is not moved by the player yet
                if (dice1.isUsable()) checkPossibleMoveWithDice(dice1, null, tempHorse);
                if (dice2.isUsable()) checkPossibleMoveWithDice(null, dice2, tempHorse);
                if (dice1.isUsable() && dice2.isUsable()) checkPossibleMoveWithDice(dice1, dice2, tempHorse);
            }
        }
    }

    //Check that whether or not, with dice 1, dice 2 or both, the horse will be able to move
    private void checkPossibleMoveWithDice(Dice dice1, Dice dice2, Horse horse){
        boolean hasPossibleMove = false;
        horse.resetListOfPossibleSteps();
        //If temp Horse is already in home and not just entered the home in this turn
        if (horse.isInHome() && !horse.isJustEnteredHome()){
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
            if (dice1 != null && checkEnterHomeBlocked(dice1.getRollNumber(), horse)) {hasPossibleMove = true; horse.setPossibleStepsListByIndex(0,dice1.getRollNumber());}
            if (dice2 != null && checkEnterHomeBlocked(dice2.getRollNumber(), horse)) {hasPossibleMove = true; horse.setPossibleStepsListByIndex(1,dice2.getRollNumber()); }

            //If tempHorse is simply outside of nest , not in home and not at home door position
        } else {
            if (dice1 != null && checkBlocked(dice1.getRollNumber(), horse) && checkOverstepHomeDoorPosition(dice1.getRollNumber(), horse))
            { hasPossibleMove = true; horse.setPossibleStepsListByIndex(0,dice1.getRollNumber()); }

            if (dice2 != null && checkBlocked(dice2.getRollNumber(), horse) && checkOverstepHomeDoorPosition(dice2.getRollNumber(), horse))
            { hasPossibleMove = true; horse.setPossibleStepsListByIndex(1,dice2.getRollNumber());}
        }

        if (hasPossibleMove && !horsesWithValidMovesList.contains(horse)) horsesWithValidMovesList.add(horse);
    }

    //Check if the next move of the horse is blocked by other horses
    private boolean checkBlocked(int steps, Horse horse){
        String tempPosition = horse.getTempPosition();
        //Check if there is no horse between start pos to end pos
        for (int tempStep = 1; tempStep < steps; tempStep++){
            String nextPosition = calculateNextPosition(tempStep, tempPosition, null);
            int nextPositionInInt = convertPositionToIntegerForm(nextPosition);
            if (horseIdOfPosition[nextPositionInInt] != null) return false;
        }

        int endPositionInt = convertPositionToIntegerForm(calculateNextPosition(steps,tempPosition, null));
        //Check if there is a horse at the last pos, and that horse must not be the same type as the one being considered, or else it would be considered blocked
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
        String nextHomePosition;
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
        if ( horsesWithValidMovesList.size() != 0 ){
            highlightHorseOutsideNest();
            if (checkBotPlayerTurn()) {
                if (isHorseGoingOutsideNest) botController.autoPickRandomHorseGoingOutsideNest();
                else botController.autoPickMostReasonableHorse();
            }
        } else {
            unhighlightHorseOutsideNest(false);
            if (isHorseGoingOutsideNest){
                if (checkBotPlayerTurn()) botController.autoPickRandomHorseGoingOutsideNest();
            }
            unhighlightHorsesInsideNest();
            updatePlayerTurn();
        }
    }

    //process after rolling dices
    public void processPostDiceRolling(){
        isHorseGoingOutsideNest = false;
        highLightDices(false);  //Unhighlight the dices
        isRollingDiceTurn = false; //Do not allow the players to roll dice until they've finished their horses moves

        //If the 1 dices contains a 6 and the start position is not occupied by this player's horse
        if (existHorseInsideNest() && (dicesController.getDice1().getRollNumber() == 6 || dicesController.getDice2().getRollNumber() == 6)){
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
        else if (checkBotPlayerTurn()) botController.autoPickRandomHorseGoingOutsideNest();
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

    /*************** Update and display score **************/

    public void updateDiceNumView(){
        dice1Val.setText(String.valueOf(dicesController.getDice1().getRollNumber()));
        dice2Val.setText(String.valueOf(dicesController.getDice2().getRollNumber()));
    }

    public void updateScore(int playerId, int deltaScore){
        scores[playerId] += deltaScore;
        updateScoreView(playerId);
    }

    public void updateScoreView(int playerId){
        Label scoreLabel = (Label)gameBoard.lookup("#" + colors[playerId] + "Points");
        scoreLabel.setText(String.valueOf(scores[playerId]));
    }

    /*************** End Update and display score **************/

    public int getPlayerIdByColor(char color){
        switch (color){
            case 'R': return 0;
            case 'B': return 1;
            case 'Y': return 2;
            case 'G': return 3;
        }
        return 0;
    }

    /*************** Update and display latest move status **************/
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

    /*************** End Update and display latest move status **************/

    public boolean checkBotPlayerTurn(){
        return tempPlayerIdTurn > (mainController.getNoHumanPlayers() - 1);
    }

    public Horse getRandomHorseInsideNest(){
        for (int i = 0; i < 4; i++){
            Horse horse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (horse.isInNest()) return horse;
        }
        return null;
    }

    public boolean existHorseInsideNest(){
        for (int i = 0; i < 4; i++){
            Horse horse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (horse.isInNest()) return true;
        }
        return false;
    }

    /*************** Getter and Setter **************/
    public static ArrayList<Horse> getHorsesWithValidMovesList() {
        return horsesWithValidMovesList;
    }
//
    public void setHorseGoingOutsideNest(boolean horseGoingOutsideNest) {
        isHorseGoingOutsideNest = horseGoingOutsideNest;
    }

    public static int[] getScores() {
        return scores;
    }

    private void debug(){
////        Horse horse = (Horse)gameBoard.lookup("#BH0");
////        horse.setTempPosition("B0");
////        horseIdOfPosition[convertPositionToIntegerForm("B0")] = "BH0";
////        horse.setInNest(false);
////        StackPane nextPositionNode = (StackPane)gameBoard.lookup("#B0");
////        nextPositionNode.getChildren().add(1, horse);
//
        Horse horse = (Horse)gameBoard.lookup("#RH0");
        horse.setTempPosition("HR6");
        horseIdOfHomePosition[convertHomePositionToIntegerForm("HR6")] = "RH0";
        horse.setInNest(false);
        horse.setInHome(true);
        horse.setJustEnteredHome(false);
        StackPane nextPositionNode = (StackPane)gameBoard.lookup("#HR6");
        nextPositionNode.getChildren().add(1, horse);


        Horse horse1 = (Horse)gameBoard.lookup("#RH1");
        horse1.setTempPosition("HR5");
        horseIdOfHomePosition[convertHomePositionToIntegerForm("HR5")] = "RH1";
        horse1.setInNest(false);
        horse1.setInHome(true);
        horse1.setJustEnteredHome(false);
        StackPane nextPositionNode1 = (StackPane)gameBoard.lookup("#HR5");
        nextPositionNode1.getChildren().add(1, horse1);

        Horse horse2 = (Horse)gameBoard.lookup("#RH2");
        horse2.setTempPosition("HR4");
        horseIdOfHomePosition[convertHomePositionToIntegerForm("HR4")] = "RH2";
        horse2.setInNest(false);
        horse2.setInHome(true);
        horse2.setJustEnteredHome(false);
        StackPane nextPositionNode2 = (StackPane)gameBoard.lookup("#HR4");
        nextPositionNode2.getChildren().add(1, horse2);

        Horse horse3 = (Horse)gameBoard.lookup("#RH3");
        horse3.setTempPosition("R0");
        horseIdOfPosition[convertPositionToIntegerForm("R0")] = "RH3";
        horse3.setInNest(false);
        horse3.setInHome(false);
        horse3.setJustEnteredHome(false);
        StackPane nextPositionNode3 = (StackPane)gameBoard.lookup("#R0");
        nextPositionNode3.getChildren().add(1, horse3);

    }
}
