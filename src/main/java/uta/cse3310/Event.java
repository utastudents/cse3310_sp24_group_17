package uta.cse3310;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Vector;
import java.util.List;

import org.java_websocket.WebSocket;

public class Event {
    public void broadcast(String message, SubLobby subLobby){
        for(Player player : subLobby.getPlayers()){
            player.getConn().send(message);
        }
    }    

    public void loginSuccess(WebSocket connection, String username){
        JsonObject json = new JsonObject();
        json.addProperty("type", "loginSuccess");
        json.addProperty("username", username);  // Add the username to the JSON response
        connection.send(json.toString());
        System.out.println("loginSuccess JSON: " + json);
    }

    //Sends json for invalid username login
    public void loginError(WebSocket connection, String message){
        JsonObject json = new JsonObject();
        json.addProperty("type", "loginError");
        json.addProperty("message", message);
        connection.send(json.toString());
    }

    //Sends json for player has successfully joined the sublobby they selected - broadcasts to all players in sublobby
    public void joinedSubLobbySuccess(WebSocket connection, List<Player> subLobbyPlayers){
        JsonObject json = new JsonObject();
        json.addProperty("type", "subLobbySuccess");

        JsonArray players = new JsonArray();
        subLobbyPlayers.forEach(player -> players.add(player.getName()));
        json.add("players", players);

        for(Player player : subLobbyPlayers){
            player.getConn().send(json.toString());
        }
        System.out.println("json sublobby: " + json);
    }

    //Sends json for max games reached
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
    
    

}
