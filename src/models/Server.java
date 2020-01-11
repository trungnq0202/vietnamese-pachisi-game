package models;

import java.util.ArrayList;

public class Server {
    public static final String HOST = "localhost";
    public static final int PORT = 9999;
    private ArrayList<Connection> connectionPool = new ArrayList<>();

    public Server() { }

    public void addConnection(Connection connection) {
        this.connectionPool.add(connection);
    }

    public ArrayList<Connection> getConnectionPool() {
        return this.connectionPool;
    }

    public int countConnections() {
        return this.connectionPool.size();
    }

}

