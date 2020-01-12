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


    public void setColorForPlayerList(ArrayList<Player> playersList) {
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


}

