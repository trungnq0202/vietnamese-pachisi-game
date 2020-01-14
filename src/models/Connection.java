package models;

import controllers.ServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Connection implements Runnable {
    private ServerController serverController;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private InetAddress inetAddress;
    private Player player;
    private boolean ready = false;

    public Connection(Socket socket, ServerController serverController) throws IOException {
        this.serverController = serverController;
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
            this.serverController.disconnectPlayer(this);
            if (this.serverController.isGameStarted()) {
                Message leavingMessage = new Message("leave", this.player.getName());
                this.serverController.broadcast(leavingMessage, this);
                this.serverController.prepareForNewGame();
            }
            System.out.printf("%s disconnected.\n", this.inetAddress.getHostAddress());
        } catch (ClassNotFoundException e) {
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
            case "leave": {
                handleLeaveMessage(message);
                break;
            }
        }
    }

    private void handleReadyMessage(Message message) {
        this.player = constructNewPlayer((String) message.getData());
        this.ready = true;
        System.out.printf("(%s) %s is ready!\n", this.inetAddress.getHostAddress(), this.player.getName());
        if (this.serverController.isGameReady()) this.serverController.startGame();
    }

    private void handleMoveMessage(Message message) {
        this.serverController.broadcast(message, this);
        Move move = (Move) message.getData();
        if (move.isGameOver()) {
            this.serverController.prepareForNewGame();
        }
    }

    private void handleLeaveMessage(Message message) {
        this.serverController.broadcast(message, this);
        this.serverController.prepareForNewGame();
    }

    public Player constructNewPlayer(String playerName) {
        String color = this.serverController.getAColor();
        return new Player(playerName, color);
    }

    public boolean isReady() {
        return this.ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isConnected() {
        return this.socket.isConnected();
    }

    public Player getPlayer() {
        return this.player;
    }

    public void send(Object object) throws IOException {
        this.outputStream.writeObject(object);
        this.outputStream.flush();
        this.outputStream.reset();
    }

    public void disconnect() throws IOException {
        this.socket.close();
    }
}
