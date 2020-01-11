package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Player;
import models.Sound;
import networking.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerController implements Serializable {
    private static PlayerController playerController;
    public StackPane menu;
    // Recently added
    private GameBoardController gameBoardController;
    public VBox onlinePromptMenu;
    public Button serverBtn;
    public VBox serverPromptMenu;
    public Button serverBackToMenuBtn;
    public Button onlinePlayerBtn;
    public Button backToMessageMenuBtn;

    public VBox onlinePlayMenu;
    public TextField onlinePlayerTextField;
    public Button backToMainMenuBtn;
    public Button onlinePlayBtn;
    public VBox onlinePromptMessage;
    public Button noBtn;
    public Button yesBtn;

    public TextField connectionMessageText;
    public TextField serverConnectionText;
    public StackPane rootMenu;
    public VBox emptyPlayerNameError;
    public Button exitErrorBtn1;
    public StackPane menuView;

    private Sound btnClickSound = new Sound(Sound.SoundType.BUTTON_CLICK_SFX);
    @FXML private Button createNewPlayerButton;
    @FXML private Button createNewSession;
    @FXML private TextField nameField;
    @FXML private TextArea messageField;
    private static InteractionController interactionController = InteractionController.getInteractionController();
    private static ArrayList<Player> playersList = new ArrayList<>();

    public void initialize(
    ){
        setOnlinePlayerButtonsEventHandler();
        System.out.println("player controller init");
    }

    private void setOnlinePlayerButtonsEventHandler(){
        setOnlinePlayBtnEventHandler();
        setPromptMessageEventHandler();
        setServerPromptMenuEventHandler();
        setOnlinePlayerBtnEventHandler();
        setServerBackToMenuBtnEventHandler();
        setBackToMessageMenuBtnEventHandler();
        setExitErrorBtnEventHandler();
    }

    public static PlayerController getPlayerController(){
        if (playerController == null){
            playerController = new PlayerController();
        }
        return  playerController;
    }

    public void setPlayerColor(ArrayList<Player> playersList) {
        for (Player player: playersList){
                assignColor(playersList.indexOf(player));
        }

    }

    public String assignColor(int orderOfPlayers){
        String playerColor = "";
        switch (orderOfPlayers){
            case 0:
                playerColor = "Green";
                break;
            case 1:
                playerColor = "Red";
                break;
            case 2:
                playerColor = "Yellow";
                break;
            case 3:
                playerColor = "Blue";
                break;
        }
        return playerColor;
    }

    public ArrayList<Player> getPlayersList(){
        return playersList;
    }

    public void addPlayer(Player player){
        playersList.add(player);
    }

    public static void setPlayersList(ArrayList<Player> playersList) {
        PlayerController.playersList = playersList;
    }

    public void exchangePlayerControllerInfo(PlayerController playerController){
        PlayerController.playerController.setPlayersList(playerController.getPlayersList());
    }


    private void setServerBackToMenuBtnEventHandler(){
        serverBackToMenuBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            onlinePromptMessage.setVisible(true);
            setYesBtnServerEventHandler();
            setNoBtnServerEventHandler();
        });
    }

    private void setYesBtnServerEventHandler(){
        yesBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            serverPromptMenu.setVisible(false);
            onlinePromptMenu.setVisible(true);
            serverConnectionText.setText("");
            serverConnectionText.setVisible(false);
            onlinePromptMessage.setVisible(false);
        });
    }

    private void setNoBtnServerEventHandler(){
        noBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            onlinePromptMessage.setVisible(false);
        });
    }

    private void setOnlinePlayerBtnEventHandler(){
        onlinePlayerBtn.setOnMouseClicked(event->{
            btnClickSound.play();
            onlinePlayMenu.setVisible(true);
            onlinePromptMenu.setVisible(false);
        });
    }

    private void setServerPromptMenuEventHandler(){
        serverBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            onlinePromptMenu.setVisible(false);
            serverPromptMenu.setVisible(true);
            Server server = new Server();
            new Thread(server).start();
            interactionController.createServer();
            serverConnectionText.setText("[Menu controller] Create server!");            //connection message
            serverConnectionText.setVisible(true);
        });
    }

    private void setBackToMessageMenuBtnEventHandler(){
        backToMessageMenuBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            onlinePromptMenu.setVisible(false);
            menu.setVisible(true);
        });
    }

    private void setOnlinePlayBtnEventHandler(){
        onlinePlayBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            //check if text field is empty
            if(onlinePlayerTextField.getText().equals("")){
                emptyPlayerNameError.setVisible(true);
            }
            else {
                if (onlinePlayerTextField.getText() != null) {
                    Player player = new Player();
                    player.setPlayerColor(onlinePlayerTextField.getText());

                    //send these messages to server so that it will distribute
                    interactionController.createClient();
                    interactionController.sendMessageForClient(player);
                    interactionController.sendMessageForClient(String.valueOf("Player " + player.getPlayerColor() + " has connected!"));
                    interactionController.sendMessageForClient(playerController);
                    connectionMessageText.setText("Client is up");
                }
                connectionMessageText.setText("Player " + onlinePlayerTextField.getText() + " is created!");
                connectionMessageText.setVisible(true);
                onlinePlayBtn.setText("Waiting...");
            }
        });
    }

    private void setExitErrorBtnEventHandler() {
        exitErrorBtn1.setOnMouseClicked(event -> {
            btnClickSound.play();
            emptyPlayerNameError.setVisible(false);
        });
    }

    private void setYesBtnEventHandler(){
        yesBtn.setOnMouseClicked(event1 -> {
            btnClickSound.play();
            onlinePromptMenu.setVisible(true);
            onlinePlayMenu.setVisible(false);
            connectionMessageText.setText("");
            connectionMessageText.setVisible(false);
            onlinePlayBtn.setText("Ready");
            onlinePromptMessage.setVisible(false);
        });
    }

    private void setNoBtnEventHandler(){
        noBtn.setOnMouseClicked(event2->{
            btnClickSound.play();
            onlinePromptMessage.setVisible(false);

        });
    }

    private void setPromptMessageEventHandler() {
        backToMainMenuBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            onlinePromptMessage.setVisible(true);
            setYesBtnEventHandler();
            setNoBtnEventHandler();
        });
    }

    public TextField getConnectionMessageText() {
        return connectionMessageText;
    }

    public TextField getServerConnectionText() {
        return serverConnectionText;
    }

}

