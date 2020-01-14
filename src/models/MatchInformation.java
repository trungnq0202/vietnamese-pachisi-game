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

import java.io.Serializable;
import java.util.ArrayList;

public class MatchInformation implements Serializable {
    // fields
    private ArrayList<Player> players = new ArrayList<>();

    // constructor
    public MatchInformation() { }

    // add player to the list of players
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    // get list of players
    public ArrayList<Player> getPlayers() {
        return this.players;
    }
}
