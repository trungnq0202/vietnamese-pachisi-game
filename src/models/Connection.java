package models;

import controllers.ServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Connection implements Runnable {
    private ServerController server;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private InetAddress inetAddress;
    private Player player;
    private boolean ready = false;

    public Connection(Socket socket, ServerController server) throws IOException {
        this.server = server;
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        this.inetAddress = socket.getInetAddress();
    }

    public void run() {
        System.out.printf("%s connected.\n", inetAddress.getHostAddress());

        try {
            while (true) {
                // listening to this client
                Message message = (Message) this.inputStream.readObject();
                handleMessage(message);
            }
        } catch (IOException e) {
            // client probably disconnected
            System.out.printf("%s disconnected.\n", this.inetAddress.getHostAddress());
        } catch (ClassNotFoundException e) {
            // shits gone wild, idk what happened
            e.printStackTrace();
        }
    }

    private void handleMessage(Message message) {
        switch (message.getAction()) {
            case "ready": {
                handleReadyMessage(message);
                break;
            }
            case "move": {
                handleMoveMessage(message);
                break;
            }
        }
    }

    private void handleReadyMessage(Message message) {
        this.player = constructNewPlayer((String) message.getData());
        this.ready = true;
        System.out.printf("(%s) %s is ready!\n", this.inetAddress.getHostAddress(),
                message.getData());
        if (this.server.isGameReady()) this.server.startGame();
    }

    private void handleMoveMessage(Message message) {
        this.server.broadcast(message, this);
    }

    public Player constructNewPlayer(String playerName) {
        String color = this.server.getAColor();
        return new Player(playerName, color);
    }

    public boolean isReady() {
        return this.ready;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void send(Object object) {
        try {
            this.outputStream.writeObject(object);
            this.outputStream.reset();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
