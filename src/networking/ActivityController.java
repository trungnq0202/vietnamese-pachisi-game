package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ActivityController {
    private static ActivityController activityController;
    private ArrayList<Socket> clientSockets;
    private ArrayList<ObjectOutputStream> serverSocketOutputs;
    private ObjectOutputStream messageOutputStream;
    private ServerInfo serverInfo = ServerInfo.getServerInfo();



    private ActivityController(){
        this.clientSockets = new ArrayList<>();
        this.serverSocketOutputs = new ArrayList<>();
    }

    public static ActivityController getActivityController(){
        if (activityController == null){
            activityController = new ActivityController();
        }
        return activityController;
    }

    public void addSocket(Socket client){
        clientSockets.add(client);
        try{
            serverSocketOutputs.add(new ObjectOutputStream(client.getOutputStream()));
        } catch (IOException e){
            System.out.println("Unable to get client output stream");
        }
    }

    public void setUpGameForAClient(Socket socket) throws IOException {
        int indexOfClient = clientSockets.indexOf(socket);
        try {
            messageOutputStream = serverSocketOutputs.get(indexOfClient);
            List<String> clientList = serverInfo.getClientList();
            messageOutputStream.writeObject(clientList);
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
    public synchronized void sendMessage(Message message){
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

