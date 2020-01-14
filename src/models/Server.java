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

import java.util.ArrayList;

public class Server {
    // constant fields
    public static final String HOST = "localhost";
    public static final int PORT = 10000;

    // fields
    private ArrayList<Connection> connectionPool = new ArrayList<>();

    // constructor
    public Server() { }

    // add a connection to connectionPool
    public void addConnection(Connection connection) {
        this.connectionPool.add(connection);
    }

    // remove a connection from connectionPool
    public void deleteConnection(Connection connection) {
        this.connectionPool.remove(connection);
    }

    // get connectionPool
    public ArrayList<Connection> getConnectionPool() {
        return this.connectionPool;
    }

    // count connections in pool
    public int countConnections() {
        return this.connectionPool.size();
    }
}

