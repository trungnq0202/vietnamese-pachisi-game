/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2019C
  Assessment: Final Project
  Created date: 01/01/2020
  By: Group 10 (3426353,3791159,3742774,3748575,3695662)
  Last modified: 14/01/2020
  By: Group 10 (3426353,3791159,3742774,3748575,3695662)
  Acknowledgement: none.
*/

package models;

import controllers.ServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Connection implements Runnable {
    // fields
    private ServerController serverController;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private InetAddress inetAddress;
    private Player player;
    private boolean ready = false;

    // constructor
    public Connection(Socket socket, ServerController serverController) throws IOException {
        this.serverController = serverController;
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        this.inetAddress = socket.getInetAddress();
    }

    // start listening to the server messages
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

    // handle the message sent from the server
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
            case "gameOver": {
                handleGameOverMessage(message);
                break;
            }
            case "leave": {
                handleLeaveMessage(message);
                break;
            }
        }
    }

    // handle ready message
    private void handleReadyMessage(Message message) {
        this.player = constructNewPlayer((String) message.getData());
        this.ready = true;
        System.out.printf("(%s) %s is ready!\n", this.inetAddress.getHostAddress(), this.player.getName());
        if (this.serverController.isGameReady()) this.serverController.startGame();
    }

    // handle move message
    private void handleMoveMessage(Message message) {
        this.serverController.broadcast(message, this);
    }

    // handle game over message
    private void handleGameOverMessage(Message message) {
        this.serverController.prepareForNewGame();
    }

    // handle leave message
    private void handleLeaveMessage(Message message) {
        this.serverController.broadcast(message, this);
        this.serverController.prepareForNewGame();
    }

    // construct new player
    public Player constructNewPlayer(String playerName) {
        String color = this.serverController.getAColor();
        return new Player(playerName, color);
    }

    // check if player is ready
    public boolean isReady() {
        return this.ready;
    }

    // set the ready status of this player
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    // check if this player is connected to server
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    // check player information of this connection
    public Player getPlayer() {
        return this.player;
    }

    // send message to the server
    public void send(Object object) throws IOException {
        this.outputStream.writeObject(object);
        this.outputStream.flush();
        this.outputStream.reset();
    }

    // disconnect from the server
    public void disconnect() throws IOException {
        this.socket.close();
    }
}
