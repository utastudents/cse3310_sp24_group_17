package uta.cse3310;

import java.util.Vector;
import java.util.ArrayList;
import java.util.List;

public class SubLobby{
    private static int lobbyCounter = 0;
    private String lobbyID;
    private int subLobbySize;
    private Player[] players;
    private int playerCount;

    public static final int MAX_ACTIVE_GAMES = 5;

    public SubLobby(int subLobbySize, Player player1){
        this.subLobbySize = subLobbySize;
        this.lobbyID = "lobby_"+(++lobbyCounter);
        this.players = new Player[4];
        this.players[0] = player1;
        this.playerCount = 1;
    }

    //adds player to sublobby if not full
    public void addPlayer(Player newPlayer){
        if (playerCount < subLobbySize) {
            players[playerCount] = newPlayer; // Add the new player to the next available slot
            playerCount++; // Increment the current player count
        }
    }

    //checks if sub lobby is full
    public boolean isSubLobbyFull(){
        return playerCount >= subLobbySize;
    }
    

    // creates a sublobby if other lobbies of the same type are full. joins a sublobby if existing lobby is not full
    public static SubLobby createOrJoinSubLobby(int subLobbySize, Vector<SubLobby> ActiveGames, Player newPlayer){
        for(SubLobby subLobby : ActiveGames){
            if(subLobby.getSubLobbySize() == subLobbySize && !subLobby.isSubLobbyFull()){
                subLobby.addPlayer(newPlayer);
                return subLobby;
            }
        }
        if(ActiveGames.size() <= MAX_ACTIVE_GAMES){
            SubLobby newSubLobby = new SubLobby(subLobbySize, newPlayer);
            ActiveGames.add(newSubLobby);

            return newSubLobby;
        }
        return null;   // max concurrent games reached
    }

    public List<Player> getPlayers(){
        List<Player> playerList = new ArrayList<>();
        for (Player player : players) {
            if (player != null) {
                playerList.add(player);
            }
        }
        return playerList;
    }

    public String getLobbyID(){
        return lobbyID;
    }

    public int getSubLobbySize(){
        return subLobbySize;
    }



}
