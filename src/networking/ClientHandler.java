package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

class ClientHandler extends Thread {

    private Server server;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private InetAddress inetAddress;
    private String threadName;
    private ActivityController activityController;


    public ClientHandler(Socket socket) {
        this.socket  = socket;
        this.inetAddress = socket.getInetAddress();
        this.activityController = ActivityController.getActivityController();
    }

    @Override
    public void run() {
        Message message;
        message = new Message("welcome to server!");
        activityController.sendMessage(message);
        System.out.printf("%s just connected...\n", inetAddress.getHostAddress());

        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                message = (Message) inputStream.readObject();
                //if ((message = (Message) inputStream.readObject()) != null){
                System.out.printf("[%s] %s said \"%s\"\n", message.getTime(),
                        message.getSenderName(),
                        message.getContent());
                //}


                this.activityController.sendMessage(message);


            }
        } catch (IOException e) {
            // client probably disconnected
            System.out.printf("%s disconnected.\n", this.inetAddress.getHostAddress());
        } catch (ClassNotFoundException e) {
            // shits gone wild, idk what happened
            System.out.println(e.getMessage());
        }
    }

    public String assignName(int orderOfPlayers){
        switch (orderOfPlayers){
            case 1:
                threadName = "Green";
                break;
            case 2:
                threadName = "Red";
                break;
            case 3:
                threadName = "Yellow";
                break;
            case 4:
                threadName = "Blue";
                break;
        }
        return threadName;
    }

    public String getThreadName() {
        return threadName;
    }
}
