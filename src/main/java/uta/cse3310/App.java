package uta.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.util.Vector;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;


public class App extends WebSocketServer {

    private Vector<Game> ActiveGames = new Vector<>();
    private int GameId = 1;
    private int connectionId = 0;

    public App(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        
    }

    public static void main(String[] args) {
        int port = 9880; // Set the port for the WebSocket server
        App app = new App(port);
        app.start();
        System.out.println("WebSocket Server started on port: " + Websocket_port);
    }

}