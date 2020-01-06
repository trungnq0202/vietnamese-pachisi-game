package controllers;

import com.sun.tools.javac.Main;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
import models.Dice;
import models.Horse;
import models.HorseNest;

import java.util.ArrayList;
import java.util.Arrays;

public class GameBoardController {
//    @FXML private GridPane ARROW0;
//    @FXML private GridPane ARROW1;
//    @FXML private GridPane ARROW2;
//    @FXML private GridPane ARROW3;
    @FXML private Button diceArrow;
    private Timeline diceArrowAnimation;
    @FXML private HBox dices;
    @FXML private Label PN0;
    @FXML private Label PN1;
    @FXML private Label PN2;
    @FXML private Label PN3;
//    @FXML private StackPane BNSP;
//    @FXML private StackPane RNSP;
//    @FXML private StackPane YNSP;
//    @FXML private StackPane GNSP;
    @FXML private VBox gameBoard;
    @FXML private Button testMoveGreenHorse;

    private MainController mainController;
    @FXML private DicesController dicesController;
    private static final char[] colors = {'R','B','Y','G'}; //Color of each player according to the order players' id
    private static String[] horseIdOfPosition;  //String array indicating which horse(horseId) is occupying which position
    private static ArrayList<Horse> horsesWithValidMoves;
    private static String[] horseIdOfHomePosition;

    private boolean isRollingDiceTurn;   //Variable indicating that this is the time for the player to roll the dices, no other action can be done
    private boolean isFreeze;
    private int tempPlayerIdTurn;

    public GameBoardController(){
        System.out.println("gameboardcontroller construct");
        horseIdOfPosition = new String[49];
        horseIdOfHomePosition = new String[24];
        horsesWithValidMoves = new ArrayList<>();
        Arrays.fill(horseIdOfPosition,null);
        Arrays.fill(horseIdOfHomePosition,null);
    }

    @FXML private void initialize(){
        System.out.println("gameboardcontroller init");
        diceArrowAnimation = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (diceArrow.getTranslateY() == 0) diceArrow.setTranslateY(-10);
            else diceArrow.setTranslateY(0);
        }));;
        diceArrowAnimation.setCycleCount(Timeline.INDEFINITE);
        dicesController.injectGameBoardController(this);
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;

    }

//    public String calculateNextPosition(int steps, String startPosition, int playerIdTurn, String tmpNextPosition){
//        //If the position of this horse is in the nest
//        if (startPosition == null) return colors[playerIdTurn] + "1";
//
//        if (tmpNextPosition == null) tmpNextPosition = startPosition;
//        int integerPartOfId = Integer.parseInt(tmpNextPosition.substring(1));  //Get the integer part of the position's fxid
//
//        //If (integer part) + (steps) > 11 -> move out of the temporary color area
//        if ( integerPartOfId != 11 && (integerPartOfId + steps) > 11)
//            return calculateNextPosition(steps - (11 - integerPartOfId) ,  tmpNextPosition.substring(0,1) + 11);
//
//        //If the tmpNextPosition is at the final position of a specific color area
//        if (integerPartOfId == 11) {
//            switch (tmpNextPosition.charAt(0)) {
//                case 'R': return calculateNextPosition(steps - 1, "B0");
//                case 'G': return calculateNextPosition(steps - 1, "R0");
//                case 'B': return calculateNextPosition(steps - 1, "Y0");
//                case 'Y': return calculateNextPosition(steps - 1, "G0");
//            }
//        }
//
//        return tmpNextPosition.charAt(0) + Integer.toString(Integer.parseInt(tmpNextPosition.substring(1)) + steps);
//    }
//
//    public String calculateNextHomePosition(){
//
//    }

    //Create all horse nests at the start of the game
    private void createHorseNests(){
        for (int i = 0; i < mainController.getNoHumanPlayers() + mainController.getNoVirtualPlayers(); i++){
            HBox subGameBoard = (HBox) gameBoard.getChildren().get(1);
            subGameBoard.getChildren().add(3 + i, new HorseNest(colors[i]));
        }
    }

    public void onOutsideNestHorseClickedEventHandler(Horse horse, int diceNum){
        /////////Thieu trường hợp horse nhảy tới home door position/////////

        if (horse.isInHome()){
            String nextHomePosition = horse.calculateNextHomePosition(1);
            Rectangle upperHome = (Rectangle) gameBoard.lookup("#" + nextHomePosition);
            horseIdOfHomePosition[horse.convertHomePositionToIntegerForm(tempPlayerIdTurn, horse.getTempPosition())] = null;
            horse.setTempPosition(nextHomePosition);
            horseIdOfHomePosition[horse.convertHomePositionToIntegerForm(tempPlayerIdTurn, nextHomePosition)] = horse.getId();
            createHorseMovingAnimation(upperHome, horse);

        } else if (horse.isInHomeDoorPosition()){
            horse.setInHome(true);
            String nextHomePosition = horse.calculateNextHomePosition(horse.getListOfPossibleSteps(diceNum));
            Rectangle home = (Rectangle) gameBoard.lookup("#" + nextHomePosition);
            horseIdOfPosition[horse.convertTempPositionToIntegerForm(tempPlayerIdTurn)] = null;
            horse.setTempPosition(nextHomePosition);
            horseIdOfHomePosition[horse.convertHomePositionToIntegerForm(tempPlayerIdTurn, nextHomePosition)] = horse.getId();
            createHorseMovingAnimation(home, horse);

        } else{
            horseIdOfPosition[horse.convertTempPositionToIntegerForm(tempPlayerIdTurn)] = null;
            String endPositionId = horse.calculateNextPosition(horse.getListOfPossibleSteps(diceNum), null);
            while (!horse.getTempPosition().equals(endPositionId)){
                String nextPositionId = horse.calculateNextPosition(1,null);
                Circle nextPositionNode = (Circle)gameBoard.lookup("#" + nextPositionId);
                createHorseMovingAnimation(nextPositionNode, horse);
                horse.setTempPosition(nextPositionId);

                //Getting kicked
                if (horseIdOfPosition[horse.convertTempPositionToIntegerForm(tempPlayerIdTurn)] != null){
                    createKickedAnimation((Horse)gameBoard.lookup("#" + horseIdOfPosition[horse.convertTempPositionToIntegerForm(tempPlayerIdTurn)]));
                }
            }
            horseIdOfPosition[horse.convertTempPositionToIntegerForm(tempPlayerIdTurn)] = horse.getId();
        }

        if (diceNum == 0) dicesController.getDice1().setUsable(false);
        else if (diceNum == 1) dicesController.getDice2().setUsable(false);
        else {dicesController.getDice1().setUsable(false); dicesController.getDice2().setUsable(false);}
        horse.hideSideArrow();
        horse.setMovable(false);
        showPossibleHorsesMoves();
    }

    public void onInsideNestHorseClickedEventHandler(Horse horse){
        horse.setInNest(false); //This horse is no longer in the nest
        String startPosition = horse.calculateNextPosition(0, null);    //Get fxid of start position
        horse.setTempPosition(startPosition);   //Set temp position of the horse as the fxid of start position
        //If there is a horse at the start position
        if (horseIdOfPosition[horse.convertTempPositionToIntegerForm(tempPlayerIdTurn)] != null){
            //Create kicked animation for this horse
            createKickedAnimation((Horse)gameBoard.lookup("#" + horseIdOfPosition[horse.convertTempPositionToIntegerForm(tempPlayerIdTurn)]));
        }

        createHorseMovingAnimation(gameBoard.lookup("#" + startPosition), horse);
        horseIdOfPosition[horse.convertTempPositionToIntegerForm(tempPlayerIdTurn)] = horse.getId();
        unhighlightHorsesInNest();
        unhighlightHorseOutsideNest();
        setDicesUnusable();
        updatePlayerTurn();

    }

    private void createHorseMovingAnimation(Node endPosition, Horse horse){
        Bounds boundsInSceneOfHorse = horse.localToScene(horse.getBoundsInLocal());
        Bounds boundsInSceneOfEndPos = endPosition.localToScene(endPosition.getBoundsInLocal());
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), horse);
        translateTransition.setByX(boundsInSceneOfEndPos.getMinX() - boundsInSceneOfHorse.getMinX() - 25);
        translateTransition.setByY(boundsInSceneOfEndPos.getMinY() - boundsInSceneOfHorse.getMinY() - 80 );
        translateTransition.play();

    }

    private void createKickedAnimation(Horse horse){
        horse.setTempPosition(null);
        horse.setInNest(true);
        horse.setTranslateX(0);
        horse.setTranslateY(0);
    }

    private void updatePlayersNameView(){
        ArrayList<String> playersNameList = mainController.getPlayersNameList();
        PN0.setText(playersNameList.get(0));
        PN1.setText(playersNameList.get(1));
        PN2.setText(playersNameList.get(2));
        PN3.setText(playersNameList.get(3));
    }

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

    //Show the game board
    public void showGameBoard(boolean isDisplayed){
        if (isDisplayed) {
            gameBoard.setVisible(true);
            startGame();
        } else {
            gameBoard.setVisible(false);
        }
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

    //Check if the next move of the horse is blocked by other horses
    private boolean checkBlocked(int steps, Horse tempHorse){
        //BUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG

        //Check if there is no horse between start pos to end pos
        for (int tempStep = 1; tempStep < steps; tempStep++){
            int tempPositionInIntegerForm = tempHorse.convertPositionToIntegerForm(tempHorse.calculateNextPosition(tempStep, null), tempPlayerIdTurn);
            if (horseIdOfPosition[tempPositionInIntegerForm] != null) return true;
        }

        int endPositionInIntegerForm = tempHorse.convertPositionToIntegerForm(tempHorse.calculateNextPosition(steps, null), tempPlayerIdTurn);
        //Check if there is a horse at the last pos, and that horse must not be the same type as the one being considered, or else it would be considered blocked
        return horseIdOfPosition[endPositionInIntegerForm] != null && horseIdOfPosition[endPositionInIntegerForm].charAt(0) != colors[tempPlayerIdTurn];
    }

    //Check if the next move of the horse will pass the home position
    private boolean checkOverstepHomeDoorPosition(int steps, Horse tempHorse){
        String tempPosition = tempHorse.getTempPosition();
        String endPosition = tempHorse.calculateNextPosition(steps, null);
        //if the start position is NOT IN the area with same color
        if (tempPosition.charAt(0) != colors[tempPlayerIdTurn]){
            //If the end position is IN the area with same color
            if (endPosition.charAt(0) == colors[tempPlayerIdTurn]){
                //Confirm overstep home door position of the integer part of the end position is not 0
                return endPosition.charAt(1) != '0';
            }
        }
        return false;
    }

    //Check if the home position the horse will move to is being blocked (this case is for those horses that are standing at the home door position)
    private boolean checkEnterHomeBlocked(int steps, Horse tempHorse){
        String nextHomePosition = tempHorse.calculateNextHomePosition(steps);
        if (nextHomePosition != null) return horseIdOfHomePosition[tempHorse.convertHomePositionToIntegerForm(tempPlayerIdTurn, nextHomePosition)] != null;
        else return true;
    }

    //Check if the home position the horse will move to is being blocked (this case is for those horses that are already in home
    private boolean checkInvalidScalingHome(Horse tempHorse){
        String nextHomePosition = tempHorse.calculateNextHomePosition(1);
        if (nextHomePosition != null) return horseIdOfHomePosition[tempHorse.convertHomePositionToIntegerForm(tempPlayerIdTurn, nextHomePosition)] != null;
        else return true;
    }

    //Check that whether or not, with dice 1, dice 2 or both, the horse will be able to move
    private void checkPossibleMoveWithDice(Dice dice1, Dice dice2, Horse tempHorse){
        System.out.println("//////// checkPossibleMoveWithDice //////");
        System.out.println("is in home :" + tempHorse.isInHome());
        System.out.println("is in home door pos : " + tempHorse.isInHomeDoorPosition());

        boolean hasPossibleMove = false;
        tempHorse.resetListOfPossibleSteps();
        //If tempHorse is already in home
        if (tempHorse.isInHome()){
            //If dice1 roll number is 1
            if (dice1 != null && dice1.getRollNumber() == 1 && !checkInvalidScalingHome(tempHorse)){
                hasPossibleMove = true;
                tempHorse.setListOfPossibleSteps(0,1);
            }

            //If dice2 roll number is 2
            if (dice2 != null && dice2.getRollNumber() == 1 && !checkInvalidScalingHome(tempHorse)){
                hasPossibleMove = true;
                tempHorse.setListOfPossibleSteps(1,1);
            }

            //If tempHorse is at home door position
        } else if (tempHorse.isInHomeDoorPosition()){
            if (dice1 != null && !checkEnterHomeBlocked(dice1.getRollNumber(), tempHorse)) {hasPossibleMove = true; tempHorse.setListOfPossibleSteps(0,dice1.getRollNumber());}
            if (dice2 != null && !checkEnterHomeBlocked(dice2.getRollNumber(), tempHorse)) {hasPossibleMove = true; tempHorse.setListOfPossibleSteps(1,dice2.getRollNumber()); }
            if (dice1 != null && dice2 != null && !checkEnterHomeBlocked(dice1.getRollNumber() + dice2.getRollNumber(), tempHorse)) {
                hasPossibleMove = true;
                tempHorse.setListOfPossibleSteps(2, dice1.getRollNumber() + dice2.getRollNumber());
            }
            //If tempHorse is simply outside of nest , not in home and not at home door position
        } else {
            if (dice1 != null && !checkBlocked(dice1.getRollNumber(),tempHorse) && !checkOverstepHomeDoorPosition(dice1.getRollNumber(), tempHorse))
                { hasPossibleMove = true; tempHorse.setListOfPossibleSteps(0,dice1.getRollNumber()); }
            if (dice2 != null && !checkBlocked(dice2.getRollNumber(),tempHorse) && !checkOverstepHomeDoorPosition(dice2.getRollNumber(), tempHorse))
                { hasPossibleMove = true; tempHorse.setListOfPossibleSteps(1,dice2.getRollNumber());}
            if (dice1 != null && dice2 != null && !checkBlocked(dice1.getRollNumber() + dice2.getRollNumber(),tempHorse) && !checkOverstepHomeDoorPosition(dice1.getRollNumber() + dice2.getRollNumber(), tempHorse))
                { hasPossibleMove = true; tempHorse.setListOfPossibleSteps(2,dice1.getRollNumber() + dice2.getRollNumber()); }

            tempHorse.displayListOfPossibleSteps();
        }

        if (hasPossibleMove && !horsesWithValidMoves.contains(tempHorse)) horsesWithValidMoves.add(tempHorse);
    }

    //Highlight the horses which are in the nest
    private void highLightHorsesInNest(){
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (tempHorse.isInNest()) {
                tempHorse.showSideArrow();
                tempHorse.activateEventHandlerForGoingOutOfNest(this);
            }
        }
    }

    //unhighlight the horses which are in the nest
    private void unhighlightHorsesInNest(){
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn]+ "H" + i);
            tempHorse.hideSideArrow();
            tempHorse.deactivateEventHandlerForGoingOutOfNest();
        }
    }

    //Find all the horses of tempPlayerId with valid moves
    private void getHorsesWithValidMoves(){
        horsesWithValidMoves.clear();               //reset the list
        Dice dice1 = dicesController.getDice1();
        Dice dice2 = dicesController.getDice2();

        //Consider every outside-nest horses belonging to the temporary player
        for (int i = 0; i < 4; i++){
            //Debugging
            System.out.println("////////////////////");

            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i); //Get the horse object according to id

            //Debugging
            System.out.println(tempHorse.getId());
            System.out.println(tempHorse.isInNest());
            System.out.println(tempHorse.isMovable());
            System.out.println(dice1.isUsable());
            System.out.println(dice2.isUsable());

            //If this horse is outside the nest
            if (!tempHorse.isInNest()) {
                tempHorse.resetListOfPossibleSteps();       //Reset possible the list of steps can be made
                //If the horse is not moved by the player yet
                if (tempHorse.isMovable()) {
                    if (dice1.isUsable()) checkPossibleMoveWithDice(dice1, null, tempHorse);

                    if (dice2.isUsable()) checkPossibleMoveWithDice(null, dice2, tempHorse);
                    if (dice1.isUsable() && dice2.isUsable()) checkPossibleMoveWithDice(dice1, dice2, tempHorse);
                }
            }
        }
    }

    public void showPossibleMovesOfOutsideNestHorse(Horse tempHorse){
        //If this horse is already in home
        if (tempHorse.isInHome()){
            if (tempHorse.getListOfPossibleSteps(0) != 0) tempHorse.showMoveOptionDice1("Move to upper home (dice 1)", true);
                else tempHorse.showMoveOptionDice1("",false);
            if (tempHorse.getListOfPossibleSteps(1) != 0) tempHorse.showMoveOptionDice2("Move to upper home (dice 2)", true);
                else tempHorse.showMoveOptionDice2("",false);

            //If this horse is in home door position
        } else if (tempHorse.isInHomeDoorPosition()){
            if (tempHorse.getListOfPossibleSteps(0) != 0) tempHorse.showMoveOptionDice1("Move to home" + tempHorse.getListOfPossibleSteps(0) + " (dice 1)", true);
                else tempHorse.showMoveOptionDice1("",false);
            if (tempHorse.getListOfPossibleSteps(1) != 0) tempHorse.showMoveOptionDice2("Move to home" + tempHorse.getListOfPossibleSteps(1) + " (dice 2)", true);
                else tempHorse.showMoveOptionDice2("",false);
            if (tempHorse.getListOfPossibleSteps(2) != 0) tempHorse.showMoveOptionDice1andDice2("Move to home" + tempHorse.getListOfPossibleSteps(2) + " (dice 1 + 2)", true);
                else tempHorse.showMoveOptionDice1andDice2("",false);

            //If this horse outside nest, home and home door position
        } else {
            if (tempHorse.getListOfPossibleSteps(0) != 0) tempHorse.showMoveOptionDice1("Move " + tempHorse.getListOfPossibleSteps(0) + " steps (dice 1)", true);
                else tempHorse.showMoveOptionDice1("",false);
            if (tempHorse.getListOfPossibleSteps(1) != 0) tempHorse.showMoveOptionDice2("Move " + tempHorse.getListOfPossibleSteps(1) + " steps (dice 2)", true);
                else tempHorse.showMoveOptionDice2("",false);
            if (tempHorse.getListOfPossibleSteps(2) != 0) tempHorse.showMoveOptionDice1andDice2("Move " + tempHorse.getListOfPossibleSteps(2) + " steps (dice 1 + 2)", true);
                else tempHorse.showMoveOptionDice1andDice2("",false);
        }
    }

    public void highlightHorseOutsideNest(){
        for (Horse horse:horsesWithValidMoves) {
            horse.showSideArrow();
            showPossibleMovesOfOutsideNestHorse(horse);
            horse.activateShowMoveOptionsOnHover();
            if (horse.isInHome()){
                if (horse.getListOfPossibleSteps(0) != 0) horse.setMoveOptionOfDice1EventHandler(gameBoard.lookup("#" + horse.calculateNextHomePosition(horse.getListOfPossibleSteps(0))), this);
                if (horse.getListOfPossibleSteps(1) != 0) horse.setMoveOptionOfDice2EventHandler(gameBoard.lookup("#" + horse.calculateNextHomePosition(horse.getListOfPossibleSteps(1))), this);
            } else if (horse.isInHomeDoorPosition()){
                if (horse.getListOfPossibleSteps(0) != 0) horse.setMoveOptionOfDice1EventHandler(gameBoard.lookup("#" + horse.calculateNextHomePosition(horse.getListOfPossibleSteps(0))), this);
                if (horse.getListOfPossibleSteps(1) != 0) horse.setMoveOptionOfDice2EventHandler(gameBoard.lookup("#" + horse.calculateNextHomePosition(horse.getListOfPossibleSteps(1))), this);
                if (horse.getListOfPossibleSteps(2) != 0) horse.setMoveOptionOfDice1AndDice2EventHandler(gameBoard.lookup("#" + horse.calculateNextHomePosition(horse.getListOfPossibleSteps(2))), this);
            } else {
                if (horse.getListOfPossibleSteps(0) != 0) horse.setMoveOptionOfDice1EventHandler(gameBoard.lookup("#" + horse.calculateNextPosition(horse.getListOfPossibleSteps(0), null)), this);
                if (horse.getListOfPossibleSteps(1) != 0) horse.setMoveOptionOfDice2EventHandler(gameBoard.lookup("#" + horse.calculateNextPosition(horse.getListOfPossibleSteps(1), null)), this);
                if (horse.getListOfPossibleSteps(2) != 0) horse.setMoveOptionOfDice1AndDice2EventHandler(gameBoard.lookup("#" + horse.calculateNextPosition(horse.getListOfPossibleSteps(2), null)), this);
            }
        }
    }

    public void unhighlightHorseOutsideNest(){
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (!tempHorse.isInNest()) {
                tempHorse.setMovable(false);
                tempHorse.hideSideArrow();
                tempHorse.deactivateShowMoveOptionsOnMouseHover();
            }
        }
    }

    private void updatePlayerTurn(){
        if (dicesController.getDice1().getRollNumber() != dicesController.getDice2().getRollNumber()) {
            gameBoard.lookup("#TURN" + tempPlayerIdTurn).setVisible(false);
            if (tempPlayerIdTurn == 3) tempPlayerIdTurn = 0; else tempPlayerIdTurn++;
            gameBoard.lookup("#TURN" + tempPlayerIdTurn).setVisible(true);
        }
        isRollingDiceTurn = true;
        highLightDices(true);
    }

    public void showPossibleHorsesMoves(){
        getHorsesWithValidMoves();

        //Debugging
//        for (int i = 0; i < horsesWithValidMoves.size(); i++){
//            System.out.println(horsesWithValidMoves.get(i).getId());
//        }

        if ( horsesWithValidMoves.size() != 0 ){
            highlightHorseOutsideNest();

        } else {
            unhighlightHorseOutsideNest();
            unhighlightHorsesInNest();
            updatePlayerTurn();
        }
    }

    //Set all the horses which are outside
    private void setAllOutsideNestHorsesMovable(){
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (!tempHorse.isInNest()) tempHorse.setMovable(true);
        }
    }

    //initialize game process
    public void startGame(){
        createHorseNests();
        updatePlayersNameView();
        highLightDices(true);
        Arrays.fill(horseIdOfPosition, null);
        Arrays.fill(horseIdOfHomePosition, null);
        isRollingDiceTurn = true;
        isFreeze = false;
        tempPlayerIdTurn = 0;
        gameBoard.lookup("#TURN0").setVisible(true);
    }

    //Reset the 2 dices to usable states
    public void setDicesUsable(){
        dicesController.getDice1().setUsable(true);
        dicesController.getDice2().setUsable(true);
    }

    public void setDicesUnusable(){
        dicesController.getDice1().setUsable(false);
        dicesController.getDice2().setUsable(false);
    }

    public void processPostDiceRolling(){
        boolean isHorseGoingOutsideNest = false;
        highLightDices(false);  //Unhighlight the dices
        isRollingDiceTurn = false; //Do not allow the players to roll dice until they've finished their horses moves

        //If the 1 dices contains a 6 and the start position is not occupied by this player's horse
        if ((dicesController.getDice1().getRollNumber() == 6 || dicesController.getDice2().getRollNumber() == 6)){
            String horseIdAtStartPosition = horseIdOfPosition[1 + 11 * tempPlayerIdTurn + tempPlayerIdTurn];
            if ( horseIdAtStartPosition == null || horseIdAtStartPosition.charAt(0) != colors[tempPlayerIdTurn] ){
                highLightHorsesInNest(); //Highlight horses to notify the player that he/she can get a new horse out of the nest
                isHorseGoingOutsideNest = true;
            }
        }

        //Show possible moves for horses which are out of nest

        setDicesUsable();                  //Reset the 2 dices to usable states
        if (checkAvailableOutsideNestHorse()) {
            setAllOutsideNestHorsesMovable();  //Reset all the horses which are outside nests as movable
            showPossibleHorsesMoves();         //Show all possible moves of each horses for the players to pick
        }
        else if (!isHorseGoingOutsideNest) updatePlayerTurn();
    }

    private boolean checkAvailableOutsideNestHorse(){
        for (int i = 0; i < 4; i++){
            Horse tempHorse = (Horse)gameBoard.lookup("#" + colors[tempPlayerIdTurn] + "H" + i);
            if (!tempHorse.isInNest()) return true;
        }
        return false;
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
}
