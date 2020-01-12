package networking;

import controllers.PlayerController;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerActivityController {
    private static ServerActivityController serverActivityController;
    private ArrayList<Socket> clientSockets;
    private ArrayList<ObjectOutputStream> serverSocketOutputs;
    private ObjectOutputStream messageOutputStream;
    private PlayerController playerController = PlayerController.getPlayerController();
    private ServerInfo serverInfo = ServerInfo.getServerInfo();



    private ServerActivityController(){
        this.clientSockets = new ArrayList<>();
        this.serverSocketOutputs = new ArrayList<>();
    }
    //create an unique instance of this class
    public static ServerActivityController getServerActivityController(){
        if (serverActivityController == null){
            serverActivityController = new ServerActivityController();
        }
        return serverActivityController;
    }

    //add socket thus its outputstream into a list
    public void addSocket(Socket client){
        clientSockets.add(client);
        try{
            serverSocketOutputs.add(new ObjectOutputStream(client.getOutputStream()));
        } catch (IOException e){
            System.out.println("Unable to get client output stream");
        }
    }

    //setup game for a client
    public void setUpGameForAClient(Socket socket) throws IOException {
        int indexOfClient = clientSockets.indexOf(socket);
        try {
            //find according outputstream
            messageOutputStream = serverSocketOutputs.get(indexOfClient);

            //this is for nothing
            messageOutputStream.writeObject("Server: Welcome to server");

            //send instance of player controller
            messageOutputStream.writeObject(playerController);
            //TODO: send game board and set up game for this client!
        } catch(IOException e){
            System.out.println("Unable to send to client!" );
            //remove this non-responding client
            messageOutputStream.close();
            clientSockets.remove(indexOfClient);
        } finally{
            System.out.println("Game set up completed!");
        }
    }

    //send message to all clients
    public synchronized void sendMessage(Object message){
        for (ObjectOutputStream stream: serverSocketOutputs){
            try {
                stream.writeObject(message);
                stream.reset();
            } catch (IOException e){
                System.out.println("Failed to send" + e);
            }
        }
    }

}

