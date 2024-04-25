package uta.cse3310;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Vector;
import java.util.List;

import org.java_websocket.WebSocket;

public class Event {
    private int Game_ID;
    private String User_ID;
    private int button;



    public void loginSuccess(WebSocket connection){
        JsonObject json = new JsonObject();
        json.addProperty("type", "loginSuccess");
        connection.send(json.toString());
        System.out.println("json: " + json);
    }

    public void loginError(WebSocket connection, String message){
        JsonObject json = new JsonObject();
        json.addProperty("type", "error");
        json.addProperty("message", message);
        connection.send(json.toString());
    }

    public void joinedSubLobbySuccess(WebSocket connection, String lobbyID, List<Player> subLobbyPlayers){
        JsonObject json = new JsonObject();
        json.addProperty("type", "subLobbySuccess");
        json.addProperty("lobby", lobbyID);

        JsonArray players = new JsonArray();
        subLobbyPlayers.forEach(player -> players.add(player.getName()));
        json.add("players", players);

        connection.send(json.toString());
        System.out.println("json sublobby: " + json);
    }

    public void joinedSubLobbyError(WebSocket connection){
        JsonObject json = new JsonObject();
        json.addProperty("type", "subLobbyError");
        connection.send(json.toString());
        System.out.println("json sublobby: " + json);
    }



}
