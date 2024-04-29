package uta.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

import java.util.ArrayList;


public class App extends WebSocketServer {

    private Vector<SubLobby> ActiveGames = new Vector<>();
    private int GameId = 1;
    private int connectionId = 0;
    private MainLobby mainLobby = new MainLobby();
    private Event eventMaker = new Event();
    //setup file
    
        



    public App(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
       System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        boolean result = mainLobby.logOff(conn);
        if (result) {
            broadcast("A player has left the game.");
        } else {
            System.out.println("No player found for the closed connection.");
        }
    }
    
    

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("message received: " + message);
        // Parse JSON string
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
    
        // Process the type of request
        String type = json.get("type").getAsString();
        System.out.println("type: " + type);
    
        switch (type) {
            case "login":
                JsonObject eventData = json.getAsJsonObject("eventData");
                System.out.println("eventData: " + eventData);
                String username = eventData.get("username").getAsString();
                System.out.println("username: " + username);
    
                if (mainLobby.logIn(conn, username)) {
                    for (Player player : mainLobby.getPlayers()) {
                        System.out.println("players in mainLobby: " + player.getName());
                    }
                    eventMaker.loginSuccess(conn, username); // Send JSON message back to JS
                } else {
                    eventMaker.loginError(conn, "invalid username");
                }
                break;
    
            case "createSubLobby":
                eventData = json.getAsJsonObject("eventData");
                System.out.println("eventData2: " + eventData);
                int subLobbySize = eventData.get("subLobbySize").getAsInt();
                System.out.println("SubLobbysize: " + subLobbySize);
    
                Player newPlayer = mainLobby.findPlayerInMainLobby(conn);
                System.out.println("new player: " + newPlayer.getName());
    
                SubLobby subLobby = SubLobby.createOrJoinSubLobby(subLobbySize, ActiveGames, newPlayer);
                if (subLobby != null) {
                    eventMaker.joinedSubLobbySuccess(conn, subLobby.getPlayers());
                } else {
                    eventMaker.joinedSubLobbyError(conn);
                }
                for (SubLobby SL : ActiveGames) {
                    System.out.println("lobby: " + SL.getLobbyID());
                    for (Player player : SL.getPlayers()) {
                        System.out.println("Player: " + player.getName());
                    }
                }
                break;
    
            case "startGame":
                System.out.println("Server side game Started");
                // Additional game start logic here
                System.out.println("Game data sent to client");
                break;
    
            case "resetLobbyState":
                username = json.get("username").getAsString();
                if (mainLobby.resetUser(username)) {
                    System.out.println("Lobby state reset for " + username);
                    JsonObject response = new JsonObject();
                    response.addProperty("type", "resetConfirmation");
                    response.addProperty("status", "success");
                    conn.send(response.toString());
                } else {
                    JsonObject response = new JsonObject();
                    response.addProperty("type", "resetConfirmation");
                    response.addProperty("status", "failed");
                    conn.send(response.toString());
                }
                break;
    
            case "chatMessage":
                String playerName = json.get("playerName").getAsString();
                String chatMessage = json.get("message").getAsString();
                JsonObject chatJson = new JsonObject();
                chatJson.addProperty("type", "chatMessage");
                chatJson.addProperty("playerName", playerName);
                chatJson.addProperty("message", chatMessage);
                broadcast(chatJson.toString()); // This sends the chat message to all connected clients
                break;
    
            case "sendErrorMessage":
                eventData = json.getAsJsonObject("eventData");
                String errorMessage = eventData.get("errorMessage").getAsString();
                WebSocket playerConn = mainLobby.findPlayerWebSocket(eventData.get("username").getAsString());
                if (playerConn != null) {
                    JsonObject errorResponse = new JsonObject();
                    errorResponse.addProperty("type", "errorMessage");
                    errorResponse.addProperty("message", errorMessage);
                    playerConn.send(errorResponse.toString()); // Sends the error message only to the requesting client
                }
                break;
            case "toggleReady":
                username = json.get("username").getAsString();
                toggleReady(conn);
                break;
    
           
        }
    }
    

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started");
    }

    public void broadcast(String message){
        for(Player player : mainLobby.getPlayers()){
            player.getConn().send(message);
        }
    }    
    public void toggleReady(WebSocket conn) {
        // Loop through all sublobbies
        JsonObject json = new JsonObject();
    
        for (SubLobby subLobby : ActiveGames) {
            // Loop through all players in the current sublobby
            JsonArray playerArray = new JsonArray();
            for (Player player : subLobby.getPlayers()) {
                if (player.getConn().equals(conn)) { // Check if the player's connection matches
                    player.setReady(!player.isReady()); // Toggle the readiness state
    
                    System.out.println("ready: " + player.isReady());
                    // Check if all players are ready and start the game if they are
                    if (subLobby.allPlayersReady() && subLobby.getPlayers().size() == subLobby.getSubLobbySize()) {
                        startGameSilently(subLobby); // Start the game silently if all players are ready
                    } 
                    // else update ready in playerlist - change name to green
                    else {
                        json.addProperty("type", "toggleReady");
                        // Loop through all players in the current sublobby to construct the player array
                        for (Player playersinSub : subLobby.getPlayers()) {
                            JsonObject playerObject = new JsonObject();
                            playerObject.addProperty("name", playersinSub.getName());
                            playerObject.addProperty("ready", playersinSub.isReady()); 
                            playerArray.add(playerObject);
                        }
                        json.add("eventData", playerArray);
                        subLobby.broadcastToSubLobby(json.toString());
    
                        System.out.println("Ready Up Json: " + json);
                    }
                    return; // Exit as soon as the matching player is found and updated
                }
            }
        }
    }

    
    private void startGame(SubLobby subLobby) {
        // Logic to start the game goes here
        subLobby.getGameMatrix();// Assume this prepares the game
    }
    /*private String convertMatrixToJson(char[][] matrix) {
        JsonObject obj = new JsonObject();
        JsonArray rows = new JsonArray();
        for (char[] row : matrix) {
            JsonArray jsonRow = new JsonArray();
            for (char c : row) {
                jsonRow.add(Character.toString(c));
            }
            rows.add(jsonRow);
        }
        obj.add("grid", rows);
        obj.addProperty("type", "gameStateUpdate");
        return obj.toString();
    }*/
    private String convertMatrixToJson(char[][] matrix){
        Gson gson =new Gson();
        String json=gson.toJson(matrix);
        return json;
    }
    private void startGameSilently(SubLobby subLobby) { 
        JsonObject json = new JsonObject();
        json.addProperty("type", "matrixCreated");

        char[][] gameMatrix = subLobby.getGameMatrix(); 
        String matrixJson = convertMatrixToJson(gameMatrix);   // Convert matrix to JSON string
        json.addProperty("eventData", matrixJson);
        subLobby.broadcastToSubLobby(matrixJson);  // Send the game matrix to all players in sublobby
        System.out.println("WordGridJson: " + matrixJson);
    }
    

    
    

    public static void main(String[] args) {
        
    // Set the port for the Http server - 9017
        int Http_port = 9017;
        String portString = System.getenv("HTTP_PORT");
        if(portString != null){
            Http_port = Integer.parseInt(portString);
        }
        HttpServer Http = new HttpServer(Http_port, "./html");
        Http.start();
        System.out.println("http Server started on port:" + Http_port);
 
        
     // Set the port for the WebSocket server - 9117
        int Websocket_port = 9117;
        portString = System.getenv("WEBSOCKET_PORT");
        if(portString != null){
            Integer.parseInt(portString);
        }
        App app = new App(Websocket_port);
        app.setReuseAddr(true);
        app.start();
        System.out.println("WebSocket Server started on port: " + Websocket_port);
    }
}