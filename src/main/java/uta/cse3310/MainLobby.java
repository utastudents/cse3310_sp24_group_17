package uta.cse3310;

import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.WebSocket;

public class MainLobby{
    private static final int MAX_PLAYERS = 20;
    private ArrayList<Player> players = new ArrayList<>();


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

    public void logOff(WebSocket conn){
        Player remove = null;
        for(Player player : players){
            if(player.getConn() == conn){
                remove = player;
                break;
            }
        }
        if(remove != null){
            players.remove(remove);
        }
    }

    public Player findPlayerInMainLobby(WebSocket conn){
        for(Player player : players){
            if(player.getConn().equals(conn)){
                return player;
            }
        }
        return null;
    }

    public List<Player> getPlayers(){
        return players;
    }

    
}

