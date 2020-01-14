package controllers;

import models.MatchInformation;
import models.Message;
import models.Move;
import models.Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientController {
    private String name;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private MenuController menuController;
    private MainController mainController;
    private GameBoardController gameBoardController;
    private boolean listening = false;

    public ClientController() {

    }

    public void injectMenuController(MenuController menuController){
        this.menuController = menuController;
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void injectGameBoardController(GameBoardController gameBoardController){
        this.gameBoardController = gameBoardController;
    }

    private void establishConnectionToServer() throws IOException {
        this.socket = new Socket(Server.HOST, Server.PORT);
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void start() throws IOException {
        establishConnectionToServer();
        startListeningToTheServer();
    }

    public void ready(String name) {
        this.name = name;
        Message readyMessage = new Message("ready", this.name);
        try {
            sendToServer(readyMessage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void move(Move move) throws IOException {
        Message moveMessage = new Message("move", move);
        sendToServer(moveMessage);
    }

    public void leave() throws IOException {
        Message leavingMessage = new Message("leave", this.name);
        sendToServer(leavingMessage);
    }

    public void sendToServer(Message message) throws IOException {
        this.outputStream.writeObject(message);
        this.outputStream.flush();
        this.outputStream.reset();
    }

    public void disconnect() {
        try {
            this.stopListeningToServer();
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

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
                System.out.println("lost connection");
            } catch (IOException e) {
                // server probably closed
                System.out.println("io exception");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void stopListeningToServer() {
        this.listening = false;
    }

    private void handleMessage(Message message) {
        switch (message.getAction()) {
            case "startGame": {
                this.menuController.startOnlineGame((MatchInformation) message.getData());
                System.out.println("Game started...");
                break;
            }
            case "move": {
                 this.gameBoardController.executeMove((Move) message.getData());
                 break;
            }
            case "leave": {
                // TODO: implement this, @Trung
                 this.gameBoardController.handlePlayerLeaving((String) message.getData());

            }
        }
    }
}

