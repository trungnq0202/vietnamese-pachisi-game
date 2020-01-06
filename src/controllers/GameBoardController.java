package controllers;

import com.sun.tools.javac.Main;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import models.Dice;
import models.Horse;
import models.HorseNest;

import java.util.ArrayList;

public class GameBoardController {
//    @FXML private GridPane ARROW0;
//    @FXML private GridPane ARROW1;
//    @FXML private GridPane ARROW2;
//    @FXML private GridPane ARROW3;
    @FXML private Button diceArrow;
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
    private static final String[] colors = {"RED","BLUE","YELLOW","GREEN"}; //Color of each player according to the order players' id
//    private static final Character[] colors = {'R','B','Y','G'}; //Color of each player according to the order players' id

    private boolean isRollingDiceTurn;   //Variable indicating that this is the time for the player to roll the dices, no other action can be done
    private int playerId;

    public GameBoardController(){
        System.out.println("gameboardcontroller construct");
    }

    @FXML private void initialize(){
        System.out.println("gameboardcontroller init");
        setMoveEventHorse();
        dicesController.injectGameBoardController(this);
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;

    }

    private void createHorseNests(){
        for (int i = 0; i < mainController.getNoHumanPlayers() + mainController.getNoVirtualPlayers(); i++){
            StackPane tempNestSP = (StackPane)gameBoard.lookup("#" + colors[i].substring(0,1) + "NSP");
//            StackPane tempNestSP = (StackPane)gameBoard.lookup("#" + colors[i] + "NSP");
            tempNestSP.getChildren().add(1, new HorseNest(colors[i]));
        }
    }

    private void setMoveEventHorse(){
        testMoveGreenHorse.setOnMouseClicked(e -> {
            Horse horse = (Horse) gameBoard.lookup("#GH1");
            String tempPos = "#" + horse.calculateNextPosition(1, null);
            System.out.println(tempPos);
            Circle position = (Circle) gameBoard.lookup(tempPos);
            Bounds boundsInScene = position.localToScene(position.getBoundsInLocal());
            System.out.println("position coordinates: " + boundsInScene);
            Bounds boundsInScene1 = horse.localToScene(horse.getBoundsInLocal());
            System.out.println("horse coordinates: " + boundsInScene1);

            System.out.println(horse.getParent());
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), horse);
            translateTransition.setByX(boundsInScene.getMinX() - boundsInScene1.getMinX() - 50);
            translateTransition.setByY(boundsInScene.getMinY() - boundsInScene1.getMinY() - 70 );
            translateTransition.play();
        });
    }

    private void updatePlayersNameView(){
        ArrayList<String> playersNameList = mainController.getPlayersNameList();
        PN0.setText(playersNameList.get(0));
        PN1.setText(playersNameList.get(1));
        PN2.setText(playersNameList.get(2));
        PN3.setText(playersNameList.get(3));
    }

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



    public void showGameBoard(boolean isDisplayed){
        if (isDisplayed) {
            createHorseNests();
//            resetHorseHomes();
            updatePlayersNameView();
            gameBoard.setVisible(true);
            startGame();
        } else {
            gameBoard.setVisible(false);
        }
    }

    private void highLightDices(boolean isDisplayed){
        if (isDisplayed)  diceArrow.setVisible(true);
        else diceArrow.setVisible(false);
    }

    private void highLightHorsesNest(int playerId){
        GridPane tmpGridArrow = (GridPane)gameBoard.lookup("#ARROW" + playerId);
        tmpGridArrow.setVisible(true);
        GridPane tmpHorseNest = (GridPane)gameBoard.lookup("#" + colors[playerId].substring(0,1) + "N");

        for (int i = 0; i < 4; i++){
            System.out.println("#" + colors[playerId].substring(0,1) + "H" + i);
            Horse tmpHorse = (Horse)tmpHorseNest.lookup("#" + colors[playerId].substring(0,1) + "H" + i);
            if (tmpHorse.isInNest()) {
//                tmpHorse.setVisible(true);
//                tmpHorse.getStyleClass().add("activeHorse");
                tmpHorse.setActiveHorse();
            }
            else {
//                tmpHorse.setVisible(false);
//                tmpHorse.getStyleClass().remove(0);
                tmpHorse.setInactiveHorse();
            }
        }
    }

    public void startGame(){
        highLightDices(true);
        isRollingDiceTurn = true;
        playerId = 0;
    }

    public void processPostDiceRolling(){
//        System.out.println(dicesController.getDice1().getRollNumber());
//        System.out.println(dicesController.getDice2().getRollNumber());
//        System.out.println("///////////////////////");
        highLightDices(false);
//        isRollingDiceTurn = false; //Do not allow the players to roll dice until they've finished their horses moves
        //If the 1 dices contains a 6
        if (dicesController.getDice1().getRollNumber() == 6 || dicesController.getDice2().getRollNumber() == 6) highLightHorsesNest(playerId);

    }


    public boolean getIsRollingDiceTurn(){
        return isRollingDiceTurn;
    }

    public void setIsRollingDiceTurn(boolean value){
        isRollingDiceTurn = value;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setDiceArrow(boolean isDisplayed) {
        highLightDices(isDisplayed);
    }
}
