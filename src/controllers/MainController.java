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
//    @FXML private DicesController dicesController;
    private static final String VIDEO_URL = "../resources/videos/loopbackgroundvideo.mp4";

    //Variables for sound controler
    private boolean isSoundEnabled;

    //Variables for game playing process
    private ArrayList<Horse> horseHomes;
//    private boolean isRollingDiceTurn;   //Variable indicating that this is the time for the player to roll the dices, no other action can be done
//    private int playerId;

    public MainController(){
        System.out.println("maincontroller construct");
        isSoundEnabled = true;
//        isRollingDiceTurn = true;
//        playerId = 1;
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
//        dicesController.injectMainController(this);

    }


    private void resetHorseHomes(){
        horseHomes.clear();
        for (int i = 1; i <= 24; i++){
            horseHomes.set(i,null);
        }
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


//    public boolean getIsRollingDiceTurn(){
//        return isRollingDiceTurn;
//    }
//
//    public void setIsRollingDiceTurn(boolean value){
//        isRollingDiceTurn = value;
//    }

    public ArrayList<Horse> getHorseHomes() {
        return horseHomes;
    }

//    public int getPlayerId() {
//        return playerId;
//    }
//
//    public void setPlayerId(int playerId) {
//        this.playerId = playerId;
//    }
}
