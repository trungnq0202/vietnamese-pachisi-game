package networking;

import controllers.InteractionController;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class Client extends Thread{
    private int port;
    private String ip;
    private String name;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final int PORT = 62341 ;
    private final String HOST_NAME = "localhost";
    private InteractionController interactionController = InteractionController.getInteractionController();

    public Client(String ip,int port) {
        this.ip = ip;
        this.port = port;
    }

    //initialize in/out stream for this client
    private void establishConnectionToServer() throws IOException {
        this.socket = new Socket(HOST_NAME,PORT);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }


    @Override
    public void run() {
        try {
            establishConnectionToServer();
        } catch (IOException e) {
            System.out.println("unable to connect to the server!");
            System.exit(69);
        }
        startListeningToTheServer();
    }

    //create a separate active thread to listen to server at all time
    private void startListeningToTheServer() {
        new Thread(() -> {
            Object input;
            try {
                while (true) {

                    if ((input = inputStream.readObject())!= null) {
                        interactionController.processInput(input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    private void disconnect() {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //send a message for this client
    public void sendMessage(Object message){
        try {
            outputStream.writeObject(message);
            outputStream.reset();
        } catch (IOException e){
            System.out.println("Can't send message from this client" + e);
        }
    }

    public boolean isServer() {
        return false;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }
}

