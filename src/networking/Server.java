package networking;

import java.io.*;
import java.net.*;

public class Server extends Thread {
    private ServerActivityController serverActivityController;
    private final int PORT = 62341 ;
    private ServerSocket serverSocket;


    //constructor
    public Server(){
        serverActivityController = ServerActivityController.getServerActivityController();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server started");

            while (true) {
                // waiting for a client to connect
                Socket socket = serverSocket.accept();
                serverActivityController.addSocket(socket);
                serverActivityController.setUpGameForAClient(socket);
                // bingo! we got a connection
                ClientHandler newConnection = new ClientHandler(socket);
                newConnection.start();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



}
