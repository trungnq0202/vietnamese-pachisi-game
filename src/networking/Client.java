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
    private final int PORT = 8888 ;
    private final String HOST_NAME = "localhost";
    private InteractionController interactionController = InteractionController.getInteractionController();

    public Client(String ip,int port) {
        this.ip = ip;
        this.port = port;
    }


    private void establishConnectionToServer() throws IOException {
        this.socket = new Socket(HOST_NAME,PORT);
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            establishConnectionToServer();
        } catch (IOException e) {
            // you probably forgot to turn on the server, doofus!!!
            System.out.println("unable to connect to the server!");
            System.exit(69);
        }
        startListeningToTheServer();
        sendMessage();
        System.out.println("Hi player, you can start chatting now!\n");
    }

    private void sendMessage()  {
        new Thread(() -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String message ;
            while (true) {
                try {
                    message = br.readLine();
                    if (message.equals("stop")) {
                        disconnect();
                        break;
                    }
                    Message new_message = new Message(message);
                    outputStream.writeObject(new_message);
                    outputStream.reset();

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
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

    private void startListeningToTheServer() {

        new Thread(() -> {
            Object input;
            try {
                while (true) {
                    input = inputStream.readObject();
                    interactionController.processInput(input);
                }
            } catch (IOException e) {
                // server probably dissed me, fuck!
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                // shits gone wild, idk what happened
                System.out.println(e.getMessage());
            }
        }).start();
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

}

