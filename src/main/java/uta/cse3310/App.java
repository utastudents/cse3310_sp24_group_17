package uta.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.Iterator;

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
    
      public static String VERSION;  



    public App(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
       System.out.println("New connection: " + conn.getRemoteSocketAddress());
       Gson gson = new Gson();
       conn.send(gson.toJson(VERSION));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        mainLobby.removeFromSubLobby(ActiveGames, conn);
        mainLobby.logOff(conn);
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
                startGame(conn);
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

            case "leaveSubLobby":
                mainLobby.removeFromSubLobby(ActiveGames, conn);
                break;

            case "highlight":
            handleHighlightMessage(conn , json);
            break;

            case "giveMehint":
            sendHint(conn);
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
                    /*if (subLobby.allPlayersReady() && subLobby.getPlayers().size() == subLobby.getSubLobbySize()) {
                        startGameSilently(subLobby); // Start the game silently if all players are ready
                    } */
                    // else update ready in playerlist - change name to green
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
                        
                    
                    return; // Exit as soon as the matching player is found and updated
                }
            }
        }
    }

    
    public void startGame(WebSocket conn) {
        for (SubLobby subLobby : ActiveGames) {
            // Loop through all players in the current sublobby
            for (Player player : subLobby.getPlayers()) {
                if (player.getConn().equals(conn)) {
                    if (subLobby.allPlayersReady() && subLobby.getPlayers().size() == subLobby.getSubLobbySize()) {
                        startGameSilently(subLobby);
                        return;
                    }
                }
            }
        }  
    }
   
    public String convertMatrixToJson(char[][] matrix){
        Gson gson = new Gson();
        String json = gson.toJson(matrix);
        return json;
    }

    public String convertWordListToJson(ArrayList<String> wordList){
        Gson gson = new Gson();
        String json = gson.toJson(wordList);
        System.out.println("wordlist: " + wordList);
        return json;
    }

    public void startGameSilently(SubLobby subLobby) { 
        JsonObject json = new JsonObject();
        json.addProperty("type", "matrixCreated");

        char[][] gameMatrix = subLobby.getGameMatrix(); 
        String matrixJson = convertMatrixToJson(gameMatrix);   // Convert matrix to JSON string
        json.addProperty("eventData", matrixJson);

        ArrayList<String> wordStrings = subLobby.getGameMatrixWordList();
        String wordListJson = convertWordListToJson(wordStrings);
        json.addProperty("eventData2", wordListJson);

        subLobby.broadcastToSubLobby(json.toString());  // Send the game matrix to all players in sublobby
        System.out.println("WordGridJson: " + json);
    }

    private void handleHighlightMessage(WebSocket conn, JsonObject rjson) {
        JsonObject eventData = rjson.getAsJsonObject("eventData");
        if (eventData == null) {
            System.err.println("Error: eventData is missing.");
            return; // Exit if there is no eventData
        }
    
        try {
            String playerName = eventData.get("playerName").getAsString();
            String word = eventData.get("word").getAsString();
        
            Player player = findPlayerByName(playerName);
            if (player != null) {
                SubLobby subLobby = findSubLobbyContainingPlayer(player);
                if (subLobby != null) {

                    //Remove found word from list
                    Iterator<String> iterator = subLobby.getGameMatrixWordList().iterator();
                    while (iterator.hasNext()) {
                        String words = iterator.next();
                        if (words.equals(word)) {
                            iterator.remove();
                            break;
                        }
                    }

                    player.setInGameScore(player.getInGameScore() + 1); // Update score
    
                    // Create and broadcast the highlight message to all clients in the sub-lobby
                    JsonObject json = new JsonObject();
                    json.addProperty("type", "highlightSuccess");
                    json.add("start", eventData.get("start"));
                    json.add("end", eventData.get("end"));
                    json.addProperty("playerName", playerName);
                    json.addProperty("color", player.getColor());

                    ArrayList<String> wordStrings = subLobby.getGameMatrixWordList();
                    String wordListJson = convertWordListToJson(wordStrings);
                    json.addProperty("updatedWords", wordListJson);
                    subLobby.broadcastToSubLobby(json.toString());
                    System.out.println(json);
    
                    // Send updated scores to all clients in the sub-lobby
                    sendUpdatedScores(subLobby);
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing highlight message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private Player findPlayerByName(String playerName) {
        for (SubLobby subLobby : ActiveGames) {
            for (Player player : subLobby.getPlayers()) {
                if (player.getName().equals(playerName)) {
                    return player;
                }
            }
        }
        return null;
    }

    private void sendUpdatedScores(SubLobby subLobby) {
        JsonArray scoresArray = new JsonArray();
        for (Player p : subLobby.getPlayers()) {
            JsonObject scoreDetail = new JsonObject();
            scoreDetail.addProperty("name", p.getName());
            scoreDetail.addProperty("score", p.getInGameScore());
            scoresArray.add(scoreDetail);
        }
        JsonObject scoreUpdate = new JsonObject();
        scoreUpdate.addProperty("type", "updateScoreboard");
        scoreUpdate.add("scores", scoresArray);
        subLobby.broadcastToSubLobby(scoreUpdate.toString());
    }
    

    private SubLobby findSubLobbyContainingPlayer(Player player) {
        for (SubLobby subLobby : ActiveGames) {
            if (subLobby.getPlayers().contains(player)) {
                return subLobby;
            }
        }
        return null;
    }
    
    public void sendHint(WebSocket conn) {
        
        for (SubLobby subLobby : ActiveGames) {
            
            for (Player player : subLobby.getPlayers()) {
                if (player.getConn().equals(conn)) {  
                   subLobby.sendHintJson();
                    return;  
                }
            }
        }
        System.out.println("Request for hint from a connection not in any active sub-lobby.");
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
        String version = System.getenv("VERSION");
        VERSION = version;
        if(portString != null){
            Integer.parseInt(portString);
        }
        App app = new App(Websocket_port);
        app.setReuseAddr(true);
        app.start();
        System.out.println("WebSocket Server started on port: " + Websocket_port);
    }
}
