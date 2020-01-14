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

import models.MatchInformation;
import models.Message;
import models.Move;
import models.Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientController {
    // fields
    private String name;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private MenuController menuController;
    private GameBoardController gameBoardController;
    private boolean listening = false;

    // constructor
    public ClientController() {}

    // inject menu controller into this client controller
    public void injectMenuController(MenuController menuController){
        this.menuController = menuController;
    }

    // inject game board controller into this client controller
    public void injectGameBoardController(GameBoardController gameBoardController){
        this.gameBoardController = gameBoardController;
    }

    // start running client controller
    public void start() throws IOException {
        establishConnectionToServer();
        startListeningToTheServer();
    }

    // try to establish a connection to the server
    private void establishConnectionToServer() throws IOException {
        this.socket = new Socket(Server.HOST, Server.PORT);
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    // tell the server that this player is ready
    public void ready(String name) {
        this.name = name;
        Message readyMessage = new Message("ready", this.name);
        try {
            sendToServer(readyMessage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // send message to server
    public void sendToServer(Message message) throws IOException {
        this.outputStream.writeObject(message);
        this.outputStream.flush();
        this.outputStream.reset();
    }

    // disconnect from server
    public void disconnect() {
        try {
            this.stopListeningToServer();
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // start listening to messages sent from the server
    private void startListeningToTheServer() {
        this.listening = true;
        new Thread(() -> {
            try {
                while (this.listening) {
                    // listening to the server
                    Message message = (Message) this.inputStream.readObject();
                    handleMessage(message);
                }
            } catch (SocketException e) {
                // client disconnected
                System.out.println(e.getMessage());
            } catch (IOException e) {
                // server probably closed
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    // temporary stop listening to server
    private void stopListeningToServer() {
        this.listening = false;
    }

    // handle messages with different actions
    private void handleMessage(Message message) {
        switch (message.getAction()) {
            case "startGame": {
                this.menuController.startOnlineGame((MatchInformation) message.getData());
                break;
            }
            case "move": {
                 this.gameBoardController.executeMove((Move) message.getData());
                 break;
            }
            case "leave": {
                 this.gameBoardController.handlePlayerLeaving((String) message.getData());
                 break;
            }
        }
    }
}

