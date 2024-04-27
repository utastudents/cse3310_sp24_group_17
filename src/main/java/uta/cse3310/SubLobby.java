package uta.cse3310;

import java.util.Vector;
import java.util.ArrayList;
import java.util.List;

public class SubLobby{
    private static int lobbyCounter = 0;
    private String lobbyID;
    private int subLobbySize;
    private List<Player> players;

    public static final int MAX_ACTIVE_GAMES = 5;

    public SubLobby(int subLobbySize, Player player1){
        this.subLobbySize = subLobbySize;
        this.lobbyID = "lobby_"+(++lobbyCounter); //creates unique id for each game
        this.players = new ArrayList<>(); //array for sublobby players
        this.players.add(player1); //sets the creater of lobby as the first player
    }

    //adds player to sublobby
    public void addPlayer(Player newPlayer){
        players.add(newPlayer);
    }

    public void removePlayer(Player removePlayer){
        players.remove(removePlayer);
    }

    //checks if sub lobby is full
    public boolean isSubLobbyFull(){
        return players.size() >= subLobbySize;
    }

    public void findPlayerInSubLobby(Vector<SubLobby> ActiveGames, Player playerToFind){
        for (SubLobby subLobby : ActiveGames) {
            for (Player player : subLobby.getPlayers()){ 
                if (player.equals(playerToFind)) { // Assuming proper equals() implementation in Player class
                    subLobby.removePlayer(player); // Remove the player from the existing sub-lobby
                    return;
                }
            }
        }
    }
    

    // creates a sublobby if other lobbies of the same type are full. joins a sublobby if existing lobby is not full
    public static SubLobby createOrJoinSubLobby(int subLobbySize, Vector<SubLobby> ActiveGames, Player newPlayer){
        //loop through to see if player is already in a sublobby
        outerloop:
        for (SubLobby subLobby : ActiveGames) {
            for (Player player : subLobby.getPlayers()){ 
                if (player.equals(newPlayer)) { // Assuming proper equals() implementation in Player class
                    subLobby.removePlayer(player); // Remove the player from the existing sub-lobby
                    break outerloop;
                }
            }
        }

        //goes through all sublobbies to check if one is open of the same type
        for(SubLobby subLobby : ActiveGames){
            if(subLobby.getSubLobbySize() == subLobbySize && !subLobby.isSubLobbyFull()){
                subLobby.addPlayer(newPlayer);
                return subLobby;
            }
        }
        //if all full create a new sublobby only if max games are not reached
        if(ActiveGames.size() <= MAX_ACTIVE_GAMES){
            SubLobby newSubLobby = new SubLobby(subLobbySize, newPlayer);
            ActiveGames.add(newSubLobby);

            return newSubLobby;
        }
        return null;   // max concurrent games reached
    }

    //Getter for players list
    public List<Player> getPlayers(){
        return players;
    }

    //Getter for lobby ID
    public String getLobbyID(){
        return lobbyID;
    }

    //Getter for lobby size
    public int getSubLobbySize(){
        return subLobbySize;
    }



}