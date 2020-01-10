package controllers;


import networking.Client;
import networking.Message;

public class InteractionController {
    private static InteractionController interactionController;
    private Client client;




    public static InteractionController getInteractionController(){
        if (interactionController == null){
            interactionController = new InteractionController();
        } return interactionController;
    }

    public void sendMessageForClient(Object message){
        client.sendMessage(message);
    }

    public void processInput(Object input){

        if (input instanceof Message){
            Message incomingMessage = (Message)input;
            System.out.println("I received a message");
            System.out.printf("[%s] %s said \"%s\"\n", incomingMessage.getTime(),
                    incomingMessage.getSenderName(),
                    incomingMessage.getContent());
        }

    }


}