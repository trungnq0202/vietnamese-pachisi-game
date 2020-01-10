package controllers;


import networking.Message;

public class InteractionController {
    private static InteractionController interactionController;

    public static InteractionController getInteractionController(){
        if (interactionController == null){
            interactionController = new InteractionController();
        } return interactionController;
    }

    public void processInput(Object input){
        // if receive a string object
        if (input instanceof Message){
            Message incomingMessage = (Message)input;
            System.out.println("I received a message");
            System.out.printf("[%s] %s said \"%s\"\n", incomingMessage.getTime(),
                    incomingMessage.getSenderName(),
                    incomingMessage.getContent());
        }


    }


}