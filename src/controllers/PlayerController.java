package controllers;

import models.Player;

import java.io.Serializable;
import java.util.ArrayList;


public class PlayerController implements Serializable {
    private static PlayerController playerController;
    private static ArrayList<Player> playersList = new ArrayList<>();

    //constructor
    private PlayerController(){
    }

    //return an unique instance of interaction controller
    public static PlayerController getPlayerController() {
        if (playerController == null){
            playerController = new PlayerController();
        } return playerController;
    }

    //getters
    public ArrayList<Player> getPlayersList(){
        return playersList;
    }

    //add a player to list
    public void addPlayer(Player player){
        playersList.add(player);

    }

    //setters
    public void setPlayersList(ArrayList<Player> playersList) {
        this.playersList = playersList;
    }

    //update player list
    public void exchangePlayerControllerInfo(PlayerController playerController){
        PlayerController.playerController.setPlayersList(playerController.getPlayersList());
    }


}

