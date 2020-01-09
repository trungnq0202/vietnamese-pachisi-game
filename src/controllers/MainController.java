package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import models.Horse;

import java.util.ArrayList;

public class MainController {
    @FXML private HBox dices;
    @FXML private StackPane root;
    @FXML private StackPane menu;
    @FXML private VBox gameBoard;
    @FXML private MediaView backgroundVideo;
    @FXML private MenuController menuController;
    @FXML private GameBoardController gameBoardController;
    private static final String VIDEO_URL = "../resources/videos/loopbackgroundvideo.mp4";

    //Variables for sound controller
    private boolean isSoundEnabled;

    //Variables for game playing process
    private ArrayList<Horse> horseHomes;

    public MainController(){
        System.out.println("maincontroller construct");
        isSoundEnabled = true;
        horseHomes = new ArrayList<>(){
            {
                for (int i = 1; i <= 24; i ++) add(null);
            }
        };
    }

    @FXML private void initialize(){
        System.out.println("maincontroller init");
        injectMainControllerInNestedControllers();
        setBackgroundVideo();
    }

    private void injectMainControllerInNestedControllers(){
        menuController.injectMainController(this);
        gameBoardController.injectMainController(this);
    }


    //Set loop background video when using menu
    private void setBackgroundVideo(){
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(getClass().getResource(VIDEO_URL).toExternalForm()));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);
        backgroundVideo.setMediaPlayer(mediaPlayer);
    }

    public ArrayList<String> getPlayersNameList(){
        return menuController.getPlayersNameList();
    }

    //Displaying game board, hide background video
    public void displayGameBoard(boolean isDisplayed){
        if (isDisplayed) {
            gameBoardController.showGameBoard(true);
            backgroundVideo.setVisible(false);
        }
        else {
            gameBoardController.showGameBoard(false);
            backgroundVideo.setVisible(true);
        }
    }

    public int getNoHumanPlayers() {
        return menuController.getNoHumanPlayers();
    }

    public int getNoVirtualPlayers() {
        return menuController.getNoVirtualPlayers();
    }

    public ArrayList<Horse> getHorseHomes() {
        return horseHomes;
    }

}
