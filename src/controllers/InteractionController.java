package controllers;


import models.Player;
import networking.Client;
import networking.Server;
import networking.ServerActivityController;

import java.util.ArrayList;


public class InteractionController {
    private static InteractionController interactionController;

    private PlayerController playerController = PlayerController.getPlayerController();
    private ServerActivityController serverActivityController = ServerActivityController.getServerActivityController();
    private String ip = "localhost";
    private int port = 62341;
    private Client client;

    private InteractionController(){
    }

    //return an unique instance of interaction controller
    public static InteractionController getInteractionController(){
        if (interactionController == null){
            interactionController = new InteractionController();
        }
         return interactionController;
    }
    //create a new server
    public void createServer(){
        Server server = new Server();
        new Thread(server).start();
    }

    //create a new client
    public void createClient(){
        client = new Client(ip,port);
        new Thread(client).run();
    }

    //send a message in name of this client
    public void sendMessageForClient(Object message){
        client.sendMessage(message);
    }

    //process object input for input stream
    public void processInput(Object input){

        // if receive a player, add this player into list
        if (input instanceof Player){
            Player incomingPlayer = (Player) input;
            playerController.addPlayer(incomingPlayer);
            incomingPlayer.assignColor(playerController.getPlayersList().indexOf(incomingPlayer));
            serverActivityController.sendMessage(playerController.getPlayersList());
        }

        //if receive a String, print to the message field
        if (input instanceof String){
            String message = (String) input;
            System.out.println(message);
        }

        // if receive a controller, update player list
        if (input instanceof PlayerController){
            PlayerController playerController = (PlayerController) input;
            this.playerController.exchangePlayerControllerInfo(playerController);
        }

        if (input instanceof ArrayList ){
            ArrayList<Player> array = (ArrayList<Player>) input;
            playerController.setPlayersList(array);
            System.out.println(playerController.getPlayersList().size());
        }


    }


}