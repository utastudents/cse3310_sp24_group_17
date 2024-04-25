package uta.cse3310;

import java.util.Vector;
import java.util.ArrayList;
import java.util.List;

public class SubLobby{
    private static int lobbyCounter = 0;
    private String lobbyID;
    private int subLobbySize;
    private List<Player> players;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    public static final int MAX_ACTIVE_GAMES = 5;

    public SubLobby(int subLobbySize, Player newPlayer){
        this.subLobbySize = subLobbySize;
        this.lobbyID = "lobby_"+(++lobbyCounter);
        this.players = new ArrayList<>();
        player1 = newPlayer;
        player2 = null;
        player3 = null;
        player4 = null;
    }

    //adds player to sublobby if not full
    public void addPlayer(Player player){
        if(!isSubLobbyFull()){
            if(player1 == null){
                player1 = player;
            }
            else if(subLobbySize >= 2 && player2 == null){
                player2 = player;
            }
            else if(subLobbySize >= 3 && player3 == null){
                player3 = player;
            }
            else if(subLobbySize == 4 && player4 == null){
                player4 = player;
            }
        }
        if(players.size() < subLobbySize){
            players.add(player);
        }
    }

    //checks if sub lobby is full
    public boolean isSubLobbyFull(){
        switch(subLobbySize){
            case 1:
                return player1 != null;
            case 2:
                return player1 != null && player2 != null;
            case 3:
                return player1 != null && player2 != null && player3 != null;
            case 4:
                return player1 != null && player2 != null && player3 != null && player4 != null;
            default:
                return false;
        }
    }

    // creates a sublobby if other lobbies of the same type are full. joins a sublobby if existing lobby is not full
    public static SubLobby createOrJoinSubLobby(int subLobbySize, Vector<SubLobby> ActiveGames, Player newPlayer){
        for(SubLobby subLobby : ActiveGames){
            if(subLobby.subLobbySize == subLobbySize && !subLobby.isSubLobbyFull()){
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
        return players;
    }

    public String getLobbyID(){
        return lobbyID;
    }

    public Player getPlayer1(){
        return player1; 
    }

    public Player getPlayer2(){
        return player1; 
    }

}
