package uta.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.util.Vector;

public class App extends WebSocketServer {

    private Vector<Game> ActiveGames = new Vector<>();
    private MainLobby mainLobby = new MainLobby();

    public App(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        JsonObject response = new JsonObject();
        response.addProperty("type", "welcome");
        response.addProperty("message", "Welcome to the server!");
        conn.send(response.toString());
        System.out.println("New connection: " + conn.getRemoteSocketAddress() + " - Welcome message sent");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        mainLobby.removePlayerFromMainLobby(conn);
        broadcastPlayerListUpdate();  // Broadcast the updated player list after removing a player
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            JsonObject json = new Gson().fromJson(message, JsonObject.class);
            String type = json.get("type").getAsString();
            handleType(conn, type, json);
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
            sendErrorMessage(conn, "Invalid JSON");
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
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
