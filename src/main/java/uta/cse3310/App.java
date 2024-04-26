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

    private Vector<SubLobby> ActiveGames = new Vector<>();
    private int GameId = 1;
    private int connectionId = 0;
    private MainLobby mainLobby = new MainLobby();
    private Event eventMaker = new Event();
    //setup file
    private Game gameBoard = new Game(
        "C:\\Users\\rohan\\OneDrive\\Documents\\2024 Spring\\CSE 3310\\Project_workspace\\New folder\\cse3310_sp24_group_17\\src\\main\\java\\uta\\cse3310\\wordsNew.txt", 
        10000, 50);
        



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
        }
        else if (type.equals("resetLobbyState")) {
            String username = json.get("username").getAsString();
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
        }else if (type.equals("sendChatMessage")) {
            JsonObject eventData = json.getAsJsonObject("eventData");
            String playerName = eventData.get("playerName").getAsString();
             message = eventData.get("message").getAsString();
            JsonObject chatMessage = new JsonObject();
            chatMessage.addProperty("type", "chatMessage");
            chatMessage.addProperty("playerName", playerName);
            chatMessage.addProperty("message", playerName + ": " + message);
            broadcast(chatMessage.toString()); // This sends the chat message to all connected clients
        }
        else if (type.equals("sendErrorMessage")) {
            JsonObject eventData = json.getAsJsonObject("eventData");
            String errorMessage = eventData.get("errorMessage").getAsString();
            WebSocket playerConn = mainLobby.findPlayerWebSocket(eventData.get("username").getAsString());
                if (playerConn != null) {
                    JsonObject errorResponse = new JsonObject();
                    errorResponse.addProperty("type", "errorMessage");
                    errorResponse.addProperty("message", errorMessage);
                    playerConn.send(errorResponse.toString()); // Sends the error message only to the requesting client
                }
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