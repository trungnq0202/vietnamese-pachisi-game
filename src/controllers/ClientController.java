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
    private GameBoardController gameBoardController;

    public ClientController(String name) {
        this.name = name;
    }

    public void injectGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

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

    public void ready() throws IOException {
        Message readyMessage = new Message("ready", this.name);
        sendToServer(readyMessage);
    }

    public void move(Move move) throws IOException {
        Message moveMessage = new Message("move", move);
        sendToServer(moveMessage);
    }

    public void sendToServer(Message message) throws IOException {
        this.outputStream.writeObject(message);
        this.outputStream.reset();
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
            try {
                while (true) {
                    // listening to the server
                    Message message = (Message) this.inputStream.readObject();
                    handleMessage(message);
                }
            } catch (IOException e) {
                // server probably closed
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleMessage(Message message) {
        switch (message.getAction()) {
            case "startGame": {
                // this.gameBoardController.startOnlineGame((MatchInformation) message.getData());
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

