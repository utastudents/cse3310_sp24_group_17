package uta.cse3310;

import java.util.Vector;

import org.java_websocket.WebSocket;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class SubLobby{
    private static int lobbyCounter = 0;
    private String lobbyID;
    private int subLobbySize;
    private List<Player> players;

    public static final int MAX_ACTIVE_GAMES = 5;
    public Game game;


    public SubLobby(int subLobbySize, Player player1){
        this.subLobbySize = subLobbySize;
        this.lobbyID = "lobby_"+(++lobbyCounter); //creates unique id for each game
        this.players = new ArrayList<>(); //array for sublobby players
        this.players.add(player1); //sets the creater of lobby as the first player
        this.game = new Game("words.txt",200,50);
        player1.setColor("blue");
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

    // creates a sublobby if other lobbies of the same type are full. joins a sublobby if existing lobby is not full
    public static SubLobby createOrJoinSubLobby(int subLobbySize, Vector<SubLobby> ActiveGames, Player newPlayer){
        //loop through to see if player is already in a sublobby
        outerloop:
        for (SubLobby subLobby : ActiveGames){
            for (Player player : subLobby.getPlayers()){ 
                if (player.equals(newPlayer)){ 
                    subLobby.removePlayer(player); 
                    subLobby.reassignColors(subLobby);
                    break outerloop;
                }
            }
        }

        //goes through all sublobbies to check if one is open of the same type
        for(SubLobby subLobby : ActiveGames){
            if(subLobby.getSubLobbySize() == subLobbySize && !subLobby.isSubLobbyFull()){
                if (subLobby.getPlayers().size() == 0) {
                    newPlayer.setColor("blue");
                }
                else if (subLobby.getPlayers().size() == 1) {
                    newPlayer.setColor("pink");
                }
                else if (subLobby.getPlayers().size() == 2) {
                    newPlayer.setColor("green");
                }
                else{
                    newPlayer.setColor("orange");
                }

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
    public char[][] getGameMatrix(){
        return game.getMatrix();
    }

    public ArrayList<String> getGameMatrixWordList(){
        return game.getWordsInMatrix();
    }
    
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player player : players) {
            names.add(player.getName());
        }
        return names;
    }
    public boolean allPlayersReady() {
        for (Player player : players) {
            if (!player.isReady()) {
                return false;  // If any player is not ready, return false
            }
        }
        return true;  
    }
    public void broadcastToSubLobby(String message) {
        for (Player player : players) {
            WebSocket conn = player.getConn();
            if (conn != null && conn.isOpen()) {
                conn.send(message);
            }
        }
    }

    public void sendHintJson() {
    int[] arr = game.getRandomWordCoordinates();
    JsonObject hintMessage = new JsonObject();
    if (arr != null) {
        int row = arr[0];
        int col = arr[1];
        hintMessage.addProperty("type", "hint");
        hintMessage.addProperty("row", row);
        hintMessage.addProperty("col", col);
        System.out.println("Hint Sent");
    }
    broadcastToSubLobby(hintMessage.toString());
    }

    public void reassignColors(SubLobby subLobby){
        for (int i = 0; i < subLobby.getPlayers().size(); i++) {
            Player player = subLobby.getPlayers().get(i);
            if (i == 0) {
                player.setColor("blue");
            } 
            else if (i == 1) {
                player.setColor("pink");
            }
            else if(i == 2){
                player.setColor("green");
            }
            else{
                player.setColor("orange");
            }
        }
    }

}
