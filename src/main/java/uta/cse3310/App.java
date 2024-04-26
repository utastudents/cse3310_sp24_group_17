package uta.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Vector;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;


public class App extends WebSocketServer {

<<<<<<<<< Temporary merge branch 1
    private Vector<Game> ActiveGames = new Vector<>();
    private MainLobby mainLobby = new MainLobby();
=========
    private Vector<SubLobby> ActiveGames = new Vector<>();
    private int GameId = 1;
    private int connectionId = 0;
    private MainLobby mainLobby = new MainLobby();
    private Event eventMaker = new Event();
    //setup file
    private Game gameBoard = new Game(
        "C:\\Users\\rohan\\OneDrive\\Documents\\2024 Spring\\CSE 3310\\Project_workspace\\New folder\\cse3310_sp24_group_17\\src\\main\\java\\uta\\cse3310\\wordsNew.txt", 
        10000, 50);
        


>>>>>>>>> Temporary merge branch 2

    public App(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
<<<<<<<<< Temporary merge branch 1
        JsonObject response = new JsonObject();
        response.addProperty("type", "welcome");
        response.addProperty("message", "Welcome to the server!");
        conn.send(response.toString());
        System.out.println("New connection: " + conn.getRemoteSocketAddress() + " - Welcome message sent");
=========
       System.out.println("New connection: " + conn.getRemoteSocketAddress());
>>>>>>>>> Temporary merge branch 2
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
<<<<<<<<< Temporary merge branch 1
        mainLobby.removePlayerFromMainLobby(conn);
        broadcastPlayerListUpdate();  // Broadcast the updated player list after removing a player
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
=========
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        mainLobby.logOff(conn);
        broadcast("Player has left");
>>>>>>>>> Temporary merge branch 2
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
<<<<<<<<< Temporary merge branch 1
        try {
            JsonObject json = new Gson().fromJson(message, JsonObject.class);
            String type = json.get("type").getAsString();
            handleType(conn, type, json);
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
            sendErrorMessage(conn, "Invalid JSON");
=========
    
        System.out.println("message recieved: " + message);
        //Parse JSON string 
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();

        //Process the type of request
        String type = json.get("type").getAsString();
        System.out.println("type: " + type);

        if(type.equals("login")){
            //Parse JSON string for event data (username)
            JsonObject eventData = json.getAsJsonObject("eventData");
            System.out.println("eventData: " + eventData);
            String username = eventData.get("username").getAsString();
            System.out.println("username: " + username);

            //add new player to mainLobby - returns true if successfulky added
            if(mainLobby.logIn(conn, username)){
                System.out.println("mainlobby: "  + mainLobby);
                eventMaker.loginSuccess(conn); // send json message back to JS
            }
            
        }
        else if(type.equals("createSubLobby")){
            JsonObject eventData = json.getAsJsonObject("eventData");
            System.out.println("eventData2: " + eventData);
            int subLobbySize = eventData.get("subLobbySize").getAsInt();
            System.out.println("SubLobbysize: " + subLobbySize);

            Player newPlayer = mainLobby.findPlayerInMainLobby(conn);
            System.out.println("new player: " + newPlayer.getName());

            SubLobby subLobby = SubLobby.createOrJoinSubLobby(subLobbySize, ActiveGames, newPlayer);
            
            for(Player player : subLobby.getPlayers()){
                System.out.println("sublobby players: "+ player.getName());
            }

            if(subLobby != null){
                eventMaker.joinedSubLobbySuccess(conn, subLobby.getLobbyID(), subLobby.getPlayers());
            }
            else{
                // eventMaker.joinedSubLobbyError(conn);
            }
         
            
        }//sublobby end
        else if(type.equals("startGame")){
            System.out.println("Server side game Started");
                // Initialize game board or ensure it's ready
                /*
                 * check if the both players are ready case
                 * if not reroute to make sure both players are ready
                 * if Both are ready, move to initialize the game
                 */
                gameBoard.initializeMatrix();
                gameBoard.placeWords();
                gameBoard.fillWithrandom();
                 // Send back the game data to client side
                String gridData = gameBoard.getGridAndWordsAsJson();
                conn.send(gridData);  // Send grid data to the client who requested to start the game
                System.out.println("Game data send to client");
>>>>>>>>> Temporary merge branch 2
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
<<<<<<<<< Temporary merge branch 1
        System.out.println("WebSocket server started successfully on port: " + getPort());
    }

    private void handleType(WebSocket conn, String type, JsonObject json) {
        switch (type) {
            case "login":
                String username = json.get("username").getAsString();
                if (mainLobby.addPlayerToMainLobby(conn, username)) {
                    broadcastPlayerListUpdate();  // Broadcast the updated player list after adding a new player
                } else {
                    sendErrorMessage(conn, "Lobby is full");
                }
                break;
            // Add more case handlers as needed
        }
    }

    public void broadcast(String message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("type", "broadcast");
        jsonMessage.addProperty("content", message);
        String json = jsonMessage.toString();
        mainLobby.getPlayers().forEach(player -> player.sendMessage(json));
    }
    

    private void sendErrorMessage(WebSocket conn, String errorMessage) {
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("type", "error");
        errorResponse.addProperty("message", errorMessage);
        conn.send(errorResponse.toString());
    }

    private void broadcastPlayerListUpdate() {
        JsonObject response = new JsonObject();
        response.addProperty("type", "playerListUpdate");
        response.add("players", new Gson().toJsonTree(mainLobby.getPlayers().stream().map(Player::getName).toArray()));
        String responseString = response.toString();
        mainLobby.getPlayers().forEach(player -> player.sendMessage(responseString));
    }
    
    


    public static void main(String[] args) {
        // Set the port for the Http server - 9017
        Integer Http_port = 9017;
        String PortString = System.getenv("HTTP_PORT");
        if (PortString != null) {
           Http_port = Integer.parseInt(PortString);
        }
        HttpServer Http = new HttpServer(Http_port, "./html");
        Http.start();
        System.out.println("http Server started on port:" + Http_port);


     // Set the port for the WebSocket server - 9117
        Integer Websocket_port = 9117;
        PortString = System.getenv("WEBSOCKET_PORT");
        if (PortString != null) {
           Websocket_port = Integer.parseInt(PortString);
        }
        App app = new App(Websocket_port);
        app.start();
        System.out.println("WebSocket Server started on port: " + Websocket_port);
    }

}
=========
        System.out.println("WebSocket server started");
    }

    public void broadcast(String message){
        for(Player player : mainLobby.getPlayers()){
            player.getConn().send(message);
        }
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
>>>>>>>>> Temporary merge branch 2
