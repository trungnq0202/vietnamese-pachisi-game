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

package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import models.Message;
import models.Move;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    // FXML
    @FXML private HBox dices;
    @FXML private StackPane root;
    @FXML private StackPane menu;
    @FXML private VBox gameBoard;
    @FXML private MediaView backgroundVideo;
    @FXML private MenuController menuController;
    @FXML private GameBoardController gameBoardController;

    // fields
    private ClientController clientController;
    private static final String VIDEO_URL = "../resources/videos/loopbackgroundvideo.mp4";

    //Variables for sound controller
    private String playerName;
    private int noOnlinePlayers;

    // constructor
    public MainController(){
        System.out.println("maincontroller construct");
        System.out.println(this);
        System.out.println(I18NController.getLocale().getDisplayCountry());
    }

    // FXML initializing
    @FXML private void initialize(){
        System.out.println("maincontroller init");
        injectMainControllerInNestedControllers();
        setBackgroundVideo();
    }

    // injecting this controller into other controllers
    private void injectMainControllerInNestedControllers(){
        menuController.injectMainController(this);
        gameBoardController.injectMainController(this);
    }

    // injecting client controller
    public void injectClientController(ClientController clientController){
        this.clientController = clientController;
    }

    //Set loop background video when using menu
    private void setBackgroundVideo(){
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(getClass().getResource(VIDEO_URL).toExternalForm()));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);
        backgroundVideo.setMediaPlayer(mediaPlayer);
    }

    // get players name list
    public ArrayList<String> getPlayersNameList(){
        return menuController.getPlayersNameList();
    }

    //Displaying game board, hide background video
    public void displayGameBoard(boolean isDisplayed, boolean isOnline){
        if (isDisplayed) {
            gameBoardController.setOnlineGame(isOnline);
            gameBoardController.showGameBoard(true);
            backgroundVideo.setVisible(false);
        }
        else {
            gameBoardController.showGameBoard(false);
            backgroundVideo.setVisible(true);
        }
    }

    // display end game menu
    public void displayEndGameMenu(int firstFinishId, boolean isOnlineGame){
        menuController.displayEndGameMenu(firstFinishId, isOnlineGame);
    }

    // get number of human players
    public int getNoHumanPlayers() {
        return menuController.getNoHumanPlayers();
    }

    // get number of virtual players
    public int getNoVirtualPlayers() {
        return menuController.getNoVirtualPlayers();
    }

    // get total number of plauer
    public int getTotalNumberOfPlayers(){
        return menuController.getNoHumanPlayers() + menuController.getNoVirtualPlayers();
    }

    // get score list
    public int[] getScoreList(){
        return gameBoardController.getScores();
    }

    // restart game
    public void restartGame(){
        gameBoardController.startGame();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    // send move message to server
    public void sendMessageToServer(Move move) throws IOException {
        Message message = new Message("move",move);
        clientController.sendToServer(message);
    }

    // send leaving message to server
    public void sendLeavingMessageToServer() throws IOException{
        Message message = new Message("leave", "");
        clientController.sendToServer(message);
    }

    // send game over message to server
    public void sendGameOverMessageToServer() throws IOException{
        Message message = new Message("gameOver", "");
        clientController.sendToServer(message);
    }

    // get game board controller
    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    // set number of online players
    public void setNoOnlinePlayers(int noOnlinePlayers) {
        this.noOnlinePlayers = noOnlinePlayers;
    }

    // get number of online players
    public int getNoOnlinePlayers() {
        return noOnlinePlayers;
    }

    // display stop game menu
    public void displayStopGameMenu(boolean isDisplayed, boolean isOnlineGame){
        menuController.displayStopGameMenu(isDisplayed, isOnlineGame);
    }

    // display player disconnected menu
    public void displayPlayerDisconnectedMenu() {
        menuController.displayPlayerDisconnectedMenu();
    }
}
