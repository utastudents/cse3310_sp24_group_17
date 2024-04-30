package uta.cse3310;

import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.WebSocket;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MainLobby { 
    
    private static final int MAX_PLAYERS = 20;
    private ArrayList<Player> players = new ArrayList<>();
    private Event eventmaker = new Event();


    // add new players to main lobby
    public boolean logIn(WebSocket conn, String name){
        //check for unique username
        if(name.equals("")){
            return false;
        }
        for(Player player : players){
            if(player.getName().equals(name)){
                return false;
            }
        }

        if(players.size() < MAX_PLAYERS){
            Player player = new Player(name, conn);
            players.add(player);
            return true;    //if # of players is not max, create player
        }
        else{
            return false;   // if # of players is max, don't create player
        }
    }

    public boolean logOff(WebSocket conn) {
        Player toRemove = null;
        for (Player player : players) {
            if (player.getConn().equals(conn)) {
                toRemove = player;
                break;
            }
            System.out.println("Mainlobby after remove: " + player);
        }
        if (toRemove != null) {
            players.remove(toRemove);
            System.out.println(toRemove.getName() + " has logged off.");
            return true;
        }
        return false;
    }

    // Reset user state in the lobby
    public boolean resetUser(String username) {
        Player toReset = findPlayerByUsername(username);
        if (toReset != null) {
            // Additional reset logic can go here if needed
            System.out.println("Resetting state for " + username);
            return true;
        }
        return false;
    }

    // Helper method to find a player by username
    public Player findPlayerByUsername(String username) {
        for (Player player : players) {
            if (player.getName().equals(username)) {
                return player;
            }
        }
        return null;
    }
    
    
    

    public Player findPlayerInMainLobby(WebSocket conn){
        for(Player player : players){
            if(player.getConn().equals(conn)){
                return player;
            }
        }
        return null;
    }

    
public WebSocket findPlayerWebSocket(String username) {
    
    for (Player player : players) {
        if (player.getName().equals(username)) {
            return player.getConn();  
        }
    }
    return null;  
}


    public List<Player> getPlayers(){
        return players;
    }


    public void removeFromSubLobby(Vector<SubLobby> ActiveGames, WebSocket conn){
        for(SubLobby subLobby : ActiveGames){
            System.out.println("lobby: " + subLobby.getLobbyID());
            for(Player player : subLobby.getPlayers()){
                if(player.getConn().equals(conn)){
                    subLobby.getPlayers().remove(player);
                    eventmaker.joinedSubLobbySuccess(conn, subLobby.getPlayers());
                    return;
                }
                System.out.println("SubLobby after remove: " + player);
            }
        }
    }
}

