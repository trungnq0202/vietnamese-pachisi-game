package models;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchInformation implements Serializable {
    private ArrayList<Player> players = new ArrayList<>();

    public MatchInformation() { }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }
}
