package networking;

import java.util.ArrayList;
import java.util.List;

public class ServerInfo {
    private static ServerInfo serverInfo;
    private static ArrayList<String> clientList;
    private String clientID;

    private ServerInfo() {
        clientList = new ArrayList<>();
    }

    public static ServerInfo getServerInfo() {
        if (serverInfo == null){
            serverInfo = new ServerInfo();
        }
        return serverInfo;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void addClient(String clientId) {
        clientList.add(clientId);
    }
    public List<String> getClientList() {
        return clientList;
    }
}
