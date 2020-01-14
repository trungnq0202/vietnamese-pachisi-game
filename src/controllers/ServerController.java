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

package controllers;

import models.Connection;
import models.MatchInformation;
import models.Message;
import models.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    // fields
    public static final int CONNECTIONS_COUNT_LIMIT = 4;
    private static final String[] COLORS = {"R", "B", "Y", "G"};
    private Server server = new Server();
    private boolean listening = true;
    private boolean gameStarted = false;

    // constructor
    public ServerController() { }

    // start listening the messages from clients
    public void startListening() {
        try {
            ServerSocket serverSocket = new ServerSocket(Server.PORT);
            System.out.println("server started");
            while (true) {
                // waiting for a client to connect
                Socket socket = serverSocket.accept();
                if (this.listening == false) {
                    socket.close();
                    continue;
                }
                if (this.server.countConnections() < CONNECTIONS_COUNT_LIMIT) {
                    // only allow maximum 4 connections at a time
                    // bingo! we got a connection
                    Connection newConnection = new Connection(socket, this);
                    // connectionPool is used in order to control
                    // the clients independently in the future
                    this.server.addConnection(newConnection);
                    new Thread(newConnection).start();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("server stopped");
    }

    // temporary stop listening to messages from clients
    public void stopListening() {
        this.listening = false;
    }

    // resume listening to messages from clients
    public void resumeListening() {
        this.listening = true;
    }

    // broadcast to all the clients in connectionPool except the sender
    public void broadcast(Object object, Connection sender) {
        for (Connection client:this.server.getConnectionPool()) {
            if (client != sender) {
                try {
                    client.send(object);
                } catch (IOException e) {
                    System.out.println("Unable to send message. " + e.getMessage());
                }
            }
        }
    }

    // get a color for a player
    public String getAColor() {
        return COLORS[this.server.countConnections() - 1];
    }

    // check if game is ready to play
    public boolean isGameReady() {
        // if there is one player, game isn't ready
        if (this.server.countConnections() <= 1) return false;
        // check if every player is ready
        for (Connection player:this.server.getConnectionPool()) {
            if (!player.isReady()) return false;
        }
        return true;
    }

    // check if game started
    public boolean isGameStarted() {
        return this.gameStarted;
    }

    // start the game by broadcasting 'startGame' messages to all players
    public void startGame() {
        MatchInformation match = constructNewMatchInformation();
        Message startGameMessage = new Message("startGame", match);
        System.out.printf("Game started with %d players...\n", this.server.countConnections());
        stopListening();
        this.gameStarted = true;
        // broadcast to everyone
        broadcast(startGameMessage, null);
    }

    // constructing new match information
    private MatchInformation constructNewMatchInformation() {
        MatchInformation match = new MatchInformation();
        for (Connection connection:this.server.getConnectionPool()) {
            match.addPlayer(connection.getPlayer());
        }
        return match;
    }

    // prepare for new game
    public void prepareForNewGame() {
        System.out.println("Game over, preparing for new game...");
        setAllConnectionsToNotReady();
        resumeListening();
        this.gameStarted = false;
    }

    // set all connections to not ready
    private void setAllConnectionsToNotReady() {
        for (Connection player:this.server.getConnectionPool()) {
            player.setReady(false);
        }
    }

    // disconnect a player from the server
    public void disconnectPlayer(Connection connection) {
        try {
            if (connection.isConnected()) connection.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        removeConnection(connection);
    }

    // remove a connection from connectionPool on server
    public void removeConnection(Connection connection) {
        this.server.deleteConnection(connection);
    }
}
