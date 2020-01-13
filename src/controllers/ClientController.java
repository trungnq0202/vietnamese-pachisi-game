package controllers;

import models.MatchInformation;
import models.Message;
import models.Move;
import models.Server;

import java.io.*;
import java.net.Socket;

public class ClientController {
    private String name;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private MenuController menuController = MenuController.getMenuController();
    private boolean listening = false;

    public ClientController() { }

    private void establishConnectionToServer() throws IOException {
        this.socket = new Socket(Server.HOST, Server.PORT);
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void start() {
        try {
            establishConnectionToServer();
        } catch (IOException e) {
            // you probably forgot to turn on the server!!!
            e.printStackTrace();
            System.exit(1);
        }

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
            } catch (IOException e) {
                // server probably closed or client disconnected
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
                // TODO: implement this, @Trung
                // this.gameBoardController.executeMove((Move) message.getData());
            }
        }
    }
}

