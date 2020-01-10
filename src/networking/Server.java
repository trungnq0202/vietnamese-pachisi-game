package networking;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread {
    private ActivityController activityController;
    private final int PORT = 8888 ;
    private ServerSocket serverSocket;


    //constructor
    public Server(){
        activityController = ActivityController.getActivityController();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server started");

            while (true) {
                // waiting for a client to connect
                Socket socket = serverSocket.accept();
                activityController.addSocket(socket);
                activityController.setUpGameForAClient(socket);
                // bingo! we got a connection
                ClientHandler newConnection = new ClientHandler(socket);
                newConnection.start();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



}
