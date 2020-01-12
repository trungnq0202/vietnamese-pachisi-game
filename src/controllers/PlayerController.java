package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Player;

import java.io.Serializable;
import java.util.ArrayList;


public class PlayerController implements Serializable {
    private static PlayerController playerController;
    private static ArrayList<Player> playersList = new ArrayList<>();

    @FXML private Button createNewPlayerButton;
    @FXML private Button createNewSession;
    @FXML private TextField nameField;
    @FXML private TextArea messageField;



    public PlayerController(){
    }

    public static PlayerController getPlayerController() {
        if (playerController == null){
            playerController = new PlayerController();
        } return playerController;
    }





    public ArrayList<Player> getPlayersList(){
        return playersList;
    }

    public void addPlayer(Player player){
        playersList.add(player);

    }

    public void setPlayersList(ArrayList<Player> playersList) {
        PlayerController.playersList = playersList;
    }

    public void exchangePlayerControllerInfo(PlayerController playerController){
        PlayerController.playerController.setPlayersList(playerController.getPlayersList());
    }


}

