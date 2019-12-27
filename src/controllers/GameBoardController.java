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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import models.Horse;
import models.HorseNest;

import java.util.ArrayList;

public class GameBoardController {
    @FXML private Label PN0;
    @FXML private Label PN1;
    @FXML private Label PN2;
    @FXML private Label PN3;
    @FXML private VBox gameBoard;
    @FXML private GridPane GN;
    @FXML private Button testMoveGreenHorse;

    private MainController mainController;
    private static final String[] colors = {"BLUE","RED","YELLOW","GREEN"};

    public GameBoardController(){
    }

    @FXML private void initialize(){
        createHorses();
        setMoveEventHorse();
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    private void createHorses(){
        //Create 4 green horses
        GN.add(new Horse("GREEN", 0, 0, 1), 0, 0);
        GN.add(new Horse("GREEN", 0, 1, 2), 1, 0);
        GN.add(new Horse("GREEN", 1, 0, 3), 0, 1);
        GN.add(new Horse("GREEN", 1, 1, 4), 1, 1);

    }

    private void setMoveEventHorse(){
        testMoveGreenHorse.setOnMouseClicked(e -> {
            Horse horse = (Horse) gameBoard.lookup("#GH1");
            String tempPos = "#"+horse.getNextPosition(1);
            System.out.println(tempPos);
            Circle position = (Circle) gameBoard.lookup(tempPos);
            Bounds boundsInScene = position.localToScene(position.getBoundsInLocal());
            System.out.println("position coordinates: "+ boundsInScene);
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

    public void showGameBoard(boolean isDisplayed){
        if (isDisplayed) {
            updatePlayersNameView();
            gameBoard.setVisible(true);
        } else {
            gameBoard.setVisible(false);
        }
    }
}
