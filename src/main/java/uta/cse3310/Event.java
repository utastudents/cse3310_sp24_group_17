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

<<<<<<<<< Temporary merge branch 1
    // Constructor
    public Event(int game_ID, String user_ID, int button) {
        this.Game_ID = game_ID;
        this.User_ID = user_ID;
        this.button = button;
    }

    // Getter for Game_ID
    public int getGame_ID() {
        return Game_ID;
    }

    // Setter for Game_ID
    public void setGame_ID(int game_ID) {
        this.Game_ID = game_ID;
    }

    // Getter for User_ID
    public String getUser_ID() {
        return User_ID;
    }

    // Setter for User_ID
    public void setUser_ID(String user_ID) {
        this.User_ID = user_ID;
    }

    // Getter for button
    public int getButton() {
        return button;
    }

    // Setter for button
    public void setButton(int button) {
        this.button = button;
    }
=========


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

    public void sendChatMessage(WebSocket conn, String playerName, String message) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "chatMessage");
        json.addProperty("playerName", playerName);
        json.addProperty("message", playerName + ": " + message);
        conn.send(json.toString());
    }
    
    



>>>>>>>>> Temporary merge branch 2
}
