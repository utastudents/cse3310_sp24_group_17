package uta.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Vector;
import com.google.gson.JsonObject;
import java.util.ArrayList;


public class App extends WebSocketServer {

    private Vector<Game> ActiveGames = new Vector<>();
    private int GameId = 1;
    private int connectionId = 0;
    private MainLobby mainLobby = new MainLobby();

    public App(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if(mainLobby.addPlayerToMainLobby(conn, "random")){
            conn.send("Welcome");
        }
        else{
            conn.send("Game is full");
            conn.close();
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        mainLobby.removePlayerFromMainLobby(conn);
        broadcast("Player has left");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Recieved message from " + conn.getRemoteSocketAddress());

        JsonObject json = new JsonObject();

        String type = json.get("type").getAsString();
        String username = json.get("username").getAsString();

        if(type == "login"){
            Player player = new Player(username, conn);

            mainLobby.addPlayerToMainLobby(conn, username);
            System.out.println("players: " + mainLobby);
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
        int Http_port = Integer.parseInt(System.getenv("HTTP_PORT"));
        HttpServer Http = new HttpServer(Http_port, "./html");
        Http.start();
        System.out.println("http Server started on port:" + Http_port);
 
        
     // Set the port for the WebSocket server - 9117
        int Websocket_port = Integer.parseInt(System.getenv("WEBSOCKET_PORT"));
        App app = new App(Websocket_port);
        app.start();
        System.out.println("WebSocket Server started on port: " + Websocket_port);
    }
}