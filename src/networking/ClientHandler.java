package networking;

import controllers.InteractionController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

class ClientHandler extends Thread {

    private Socket socket;
    private ObjectInputStream inputStream;
    private InetAddress inetAddress;
    private String threadName;
    private ServerActivityController serverActivityController;
    private InteractionController interactionController = InteractionController.getInteractionController();


    public ClientHandler(Socket socket) {
        this.socket  = socket;
        this.inetAddress = socket.getInetAddress();
        this.serverActivityController = ServerActivityController.getServerActivityController();
    }

    @Override
    public void run() {
        System.out.printf("%s just connected...\n", inetAddress.getHostAddress());
        Object fromClient;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                if ((fromClient = inputStream.readObject()) != null)
                {
                    interactionController.processInput(fromClient);
                    //this.serverActivityController.sendMessage(fromClient);
                }

            }
        } catch (IOException e) {
            // client probably disconnected
            System.out.printf("%s disconnected.\n", this.inetAddress.getHostAddress());
        } catch (ClassNotFoundException e) {
            // shits gone wild, idk what happened
            System.out.println(e.getMessage());
        }
    }


    public String getThreadName() {
        return threadName;
    }
}
